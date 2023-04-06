package com.payfast.Services;
import com.payfast.exceptions.InvalidRequestException;

import java.util.HashMap;
import java.util.Map;
public class CreditCardTransactions {

    private static final String PATH = "process/query";

    /**
     * Query a credit card transaction
     * $payfast->creditCardTransactions->fetch('1124148');
     * @param $token
     * @return Map<String, Object>
     * @throws InvalidRequestException
     */
    public Map<String, Object> fetch(String token) throws InvalidRequestException {
        if (token == null) {
            throw new InvalidRequestException("Required \"token\" parameter missing", 400);
        }
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            String response = Request.sendApiRequest("GET", PATH + "/" + token, headers, null);
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            return gson.fromJson(jsonObject, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
