package com.payfast.paymentintegrations;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.payfast.PayFastPayment;
import com.payfast.exceptions.InvalidRequestException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
public class Notification {


    /**
     * Check if the payment notification is valid
     *
     * @param pfData The data posted to the notification
     * @param checks Array of data validation checks to run
     * @throws InvalidRequestException
     */
    public static boolean isValidNotification(Map<String, String[]> pfData, Map<String, String> checks)
            throws InvalidRequestException {
        // Clean notification data
        pfData = cleanNotificationData(pfData);

        String pfParamString = dataToString(pfData);

        boolean check1 = pfValidSignature(pfData, pfParamString, PayFastPayment.passPhrase);
        boolean check2 = pfValidIP();
        boolean check3 = pfValidData(pfData, checks);
        boolean check4 = pfValidServerConfirmation(pfParamString);

        return check1 && check2 && check3 && check4;
    }


    /**
     * Clean notification data
     *
     * @param pfData
     * @return
     */
    private static Map<String, String[]> cleanNotificationData(Map<String, String[]> pfData) {
        // Strip any slashes in data
        for (String key : pfData.keySet()) {
            String[] val = pfData.get(key);
            for (int i = 0; i < val.length; i++) {
                val[i] = val[i].replaceAll("(\\\\)([rnt\"'])", "$2");
            }
            pfData.put(key, val);
        }
        return pfData;
    }


    /**
     * Convert posted variables to a string
     *
     * @param pfData
     * @return
     */
    private static String dataToString(Map<String, String[]> pfData) {
        StringBuilder postData = new StringBuilder();
        for (String key : pfData.keySet()) {
            if (!key.equals("signature")) {
                String[] valueArr = pfData.get(key);
                if (valueArr.length > 0) {
                    postData.append(key).append("=");
                    for (String value : valueArr) {
                        postData.append(value).append(",");
                    }
                    postData.deleteCharAt(postData.length() - 1);
                    postData.append("&");
                }
            } else {
                break;
            }
        }
        postData.deleteCharAt(postData.length() - 1);
        return postData.toString();
    }

    /**
     * Verify the signature
     *
     * @param pfData
     * @param pfParamString
     * @param pfPassphrase
     * @return
     */
    private static boolean pfValidSignature(Map<String, String[]> pfData, String pfParamString, String pfPassphrase) {
        if (pfData.get("signature") == null || pfData.get("signature").length == 0) {
            PayFastPayment.errorMsg.add("Invalid signature");
            return false;
        }

        // Calculate security signature
        String signature;
        if (pfPassphrase == null)
            signature = md5(pfParamString);
        else
            signature = md5(pfParamString + "&passphrase=" + pfPassphrase);

        if (!pfData.get("signature")[0].equals(signature)) {
            PayFastPayment.errorMsg.add("Invalid signature");
        }

        return pfData.get("signature")[0].equals(signature);
    }

    private static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes("UTF-8"));
            return convertByteToHex(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    private static String convertByteToHex(byte[] byteData) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Check that the notification has come from a valid PayFast domain
     *
     * @return
     */
    private static boolean pfValidIP() {
        if (!PayFastBase.isNotLocalHost()) {
            return true;
        }

        if (PayFastBase.isValidRemoteIP()) {
            return true;
        }

        PayFastPayment.errorMsg.add("This notification does not come from a valid PayFast domain");
        return false;
    }
    /**
     * Compare returned data
     *
     * @param pfData
     * @param checks
     * @return
     */
    private static boolean pfValidData(Map<String, String[]> pfData, Map<String, String> checks) {

        if (!checks.isEmpty()) {
            for (Map.Entry<String, String> entry : checks.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals("amount_gross")) {
                    if (!Arrays.asList(pfData.get("amount_gross")).contains(value)) {
                        PayFastPayment.errorMsg.add(
                                "Parameter 'amount_gross' does not exist in the post data or does not match expected value");
                        return false;
                    }
                } else if (pfData.get(key) == null || pfData.get(key).length == 0
                        || !Arrays.asList(pfData.get(key)).contains(value)) {
                    PayFastPayment.errorMsg.add(
                            "Parameter '" + key + "' does not exist in the post data or does not match expected value");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Perform a server request to confirm the details
     *
     * @param pfParamString
     * @return
     * @throws InvalidRequestException
     */
    private static boolean pfValidServerConfirmation(String pfParamString) throws InvalidRequestException {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(PayFastPayment.baseUrl + "/query/validate");
            List<NameValuePair> params = new ArrayList<>();
            for (String s : pfParamString.split("&")) {
                String[] param = s.split("=");
                params.add(new BasicNameValuePair(param[0], param[1]));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = client.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            if (!responseBody.equals("VALID")) {
                PayFastPayment.errorMsg.add("Invalid server confirmation");
                return false;
            }
        } catch (Exception e) {
            if (e.getCause() instanceof java.net.SocketTimeoutException) {
                throw new InvalidRequestException("Server request timeout.", 408);
            }
            PayFastPayment.errorMsg.add("Invalid server confirmation");
            throw new RuntimeException(e);
        }
        return true;
    }
}
