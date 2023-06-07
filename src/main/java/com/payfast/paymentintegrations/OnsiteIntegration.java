package com.payfast.paymentintegrations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payfast.Auth;
import com.payfast.HttpUtils;
import com.payfast.PayFast;
import com.payfast.PayFastPayment;
import com.payfast.exceptions.InvalidRequestException;

import javax.swing.text.html.Option;

public class OnsiteIntegration {
    private static final String PATH = "onsite/process";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generate payment identifier
     *
     * @param data Map containing payment data
     * @return Optional String representing the payment identifier, or Optional.empty() if not found
     * @throws InvalidRequestException if the request is invalid
     */
    public Optional<String> generatePaymentIdentifier(Map<String, String> data) throws InvalidRequestException {
        if (PayFast.isAppInTestMode()) {
            throw new InvalidRequestException("Sorry but Onsite is not available in Sandbox mode", 400);
        }

        // Add authentication details to the data
        Map<String, String> authDetails = new HashMap<>();
        authDetails.put("merchant_id", String.valueOf(PayFast.MERCHANT_ID.get()));
        authDetails.put("merchant_key", PayFast.MERCHANT_KEY.get());
        data.putAll(authDetails);

        // Generate signature
        data.put("signature", Auth.generateSignature(data, PayFast.PASS_PHRASE.get()));

        try {
            // Send HTTP POST request
            String responseJson = HttpUtils.sendHttpPostRequest(PayFast.getApiUrl() + "/" + PATH, data);

            // Parse response as JSON
            JsonNode jsonNode = objectMapper.readTree(responseJson);

            // Check if "uuid" exists in the JSON response
            if (jsonNode.has("uuid")) {
                return Optional.of(jsonNode.get("uuid").asText());
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }


}
