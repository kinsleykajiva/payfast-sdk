package com.payfast.paymentintegrations;

import java.util.Map;
import java.util.HashMap;

import com.payfast.Auth;
import com.payfast.PayFastPayment;
import com.payfast.exceptions.InvalidRequestException;

public class OnsiteIntegration {
    private static final String PATH = "onsite/process";

    private String dataToString(Map<String, String> data) {
        // Create parameter string
        StringBuilder pfOutput = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String val = entry.getValue().trim();
            if (!val.isEmpty()) {
                pfOutput.append(entry.getKey()).append("=").append(val).append("&");
            }
        }
        // Remove last ampersand
        return pfOutput.substring(0, pfOutput.length() - 1);
    }

    /**
     * Generate payment identifier
     * @param data
     * @return String|null
     * @throws InvalidRequestException
     */
    public String generatePaymentIdentifier(Map<String, String> data) throws InvalidRequestException {
        if (PayFastPayment.testMode) {
            throw new InvalidRequestException("Sorry but Onsite is not available in Sandbox mode", 400);
        }
        Map<String, String> authDetails = new HashMap<>();
        authDetails.put("merchant_id", PayFastPayment.merchantId);
        authDetails.put("merchant_key", PayFastPayment.merchantKey);
        data.putAll(authDetails);
        // Generate signature
        data.put("signature", Auth.generateSignature(data, PayFastPayment.passPhrase));
        // Convert the data map to a string
        String pfParamString = dataToString(data);
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, pfParamString);
            Request request = new Request.Builder()
                    .url(PayFastPayment.baseUrl + "/" + PATH)
                    .method("POST", body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (jsonObject.has("uuid")) {
                return jsonObject.get("uuid").getAsString();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
