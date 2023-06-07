package com.payfast.Services;


import com.payfast.exceptions.InvalidRequestException;

import java.util.HashMap;
import java.util.Map;
public class Refunds {
    private static final String PATH = "refunds";

    /**
     * Refunds constructor.
     * @throws InvalidRequestException
     */
    public Refunds() throws InvalidRequestException {
        if (PayFastApi.testMode) {
            throw new InvalidRequestException("Sorry but Refunds is not available in Sandbox mode", 400);
        }
    }

    /**
     * Fetch a refund
     * $payfast->refunds->fetch('dc0521d3-55fe-269b-fa00-b647310d760f');
     * @param $id
     * @return Map<String, Object>
     * @throws InvalidRequestException
     */
    public Map<String, Object> fetch(String id) throws InvalidRequestException {
        if (id == null) {
            throw new InvalidRequestException("Required \"id\" parameter missing", 400);
        }
        try {
            String response = Request.sendApiRequest("GET", PATH + "/" + id);
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            return gson.fromJson(jsonObject, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new refund
     * $payfast->refunds->create('2afa4575-5628-051a-d0ed-4e071b56a7b0', ['amount' => 50, 'reason' => 'Product returned', 'acc_type' => 'savings']);
     * @param $id
     * @param array $options
     * @return Map<String, Object>
     * @throws InvalidRequestException
     */
    public Map<String, Object> create(String id, HashMap<String, Object> options) throws InvalidRequestException {
        if (id == null) {
            throw new InvalidRequestException("Required \"id\" parameter missing", 400);
        }
        if (!options.containsKey("notify_buyer")) {
            options.put("notify_buyer", 1);
        }
        try {
            Validate.validateOptions(options, new HashMap<String, String>() {{
                put("amount", "int");
                put("acc_type", "accType");
            }});
            String response = Request.sendApiRequest("POST", PATH + "/" + id, new HashMap<String, String>(), options);
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            return gson.fromJson(jsonObject, Map.class);
        } catch (InvalidRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
