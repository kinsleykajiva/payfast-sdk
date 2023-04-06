package com.payfast;

import com.payfast.exceptions.InvalidRequestException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Auth {
    /**
     * Generate signature for API
     *
     * @param pfData  (all of the header and body values to be sent to the API)
     * @param passPhrase
     */
    public static String generateApiSignature(Map<String, String> pfData, String passPhrase) {
        // Construct variables
        Map<String, String> data = new HashMap<String, String>(pfData); // Use copy of original map to prevent modifying original map
        data.forEach((key1, value) -> data.put(key1, value.replaceAll("\\\\", "")));

        if (passPhrase != null && !passPhrase.isEmpty()) { // Use !isEmpty() for string comparison
            data.put("passphrase", passPhrase); // Use 'data' map instead of 'pfData'
        }

        // Sort the array by key, alphabetically
        List<String> keys = new ArrayList<>(data.keySet());
        Collections.sort(keys);

        // Normalise the array into a parameter string
        StringBuilder pfParamString = new StringBuilder(); // Use StringBuilder for better performance
        for (String key : keys) {
            if (!key.equalsIgnoreCase("signature")) {
                pfParamString.append(key).append("=").append(data.get(key)).append("&");
            }
        }

        // Remove the last '&' from the parameter string
        if (pfParamString.length() > 0) { // Check if StringBuilder is not empty before removing last character
            pfParamString.deleteCharAt(pfParamString.length() - 1);
        }

        return MD5(pfParamString.toString()); // Convert StringBuilder to String before passing to MD5 function
    }


    public static String generateSignature(Map<String, String> data, String passPhrase) throws InvalidRequestException {
        String[] fields = {
                "merchant_id",
                "merchant_key",
                "return_url",
                "cancel_url",
                "notify_url",
                "notify_method",
                "name_first",
                "name_last",
                "email_address",
                "cell_number",
                "m_payment_id",
                "amount",
                "item_name",
                "item_description",
                "custom_int1",
                "custom_int2",
                "custom_int3",
                "custom_int4",
                "custom_int5",
                "custom_str1",
                "custom_str2",
                "custom_str3",
                "custom_str4",
                "custom_str5",
                "email_confirmation",
                "confirmation_address",
                "currency",
                "payment_method",
                "subscription_type",
                "passphrase",
                "billing_date",
                "recurring_amount",
                "frequency",
                "cycles",
                "subscription_notify_email",
                "subscription_notify_webhook",
                "subscription_notify_buyer"
        };

        Map<String, String> sortAttributes = new HashMap<>();
        for (String field : fields) {
            if (data.containsKey(field) && !data.get(field).isEmpty()) {
                sortAttributes.put(field, data.get(field));
            }
        }

        if (passPhrase != null && !passPhrase.isEmpty()) {
            sortAttributes.put("passphrase", passPhrase);
        }

        // Some functionality requires the passphrase to be set
        if (sortAttributes.containsKey("subscription_type") && (passPhrase == null || passPhrase.isEmpty())) {
            throw new InvalidRequestException("Subscriptions require a passphrase to be set", 400);
        }

        // Create parameter string
        StringBuilder pfOutput = new StringBuilder();
        for (String attribute : fields) {
            if (sortAttributes.containsKey(attribute)) {
                pfOutput.append(attribute).append("=").append(sortAttributes.get(attribute).trim()).append("&");
            }
        }

        // Remove last ampersand
        String getString = pfOutput.substring(0, pfOutput.length() - 1);

        return MD5(getString);
    }

    private static String MD5(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(md5.getBytes(StandardCharsets.UTF_8));
            BigInteger bigInt = new BigInteger(1, digest);
            StringBuilder md5Hash = new StringBuilder(bigInt.toString(16));
            // Pad with leading zeros if the hash has less than 32 characters
            while (md5Hash.length() < 32) {
                md5Hash.insert(0, "0");
            }
            return md5Hash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}

