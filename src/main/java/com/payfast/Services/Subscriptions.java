package com.payfast.Services;

import com.payfast.Validate;
import com.payfast.exceptions.InvalidRequestException;

import java.util.Map;

public class Subscriptions {
    private static final String PATH = "subscriptions";

    /**
     * Fetch a subscription
     * $payfast->subscriptions->fetch('dc0521d3-55fe-269b-fa00-b647310d760f');
     *
     * @param token
     * @return Map<Object, Object>
     * @throws Exception
     */
    public Map<Object, Object> fetch(String token) throws Exception {
        if (token == null || token.isEmpty()) {
            throw new InvalidRequestException("Required \"token\" parameter missing", 400);
        }
        try {
            String response = Request.sendApiRequest("GET", PATH + "/" + token + "/fetch");
            return new Gson().fromJson(response, Map.class);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Pause a subscription
     * $payfast->subscriptions->pause('2afa4575-5628-051a-d0ed-4e071b56a7b0', ['cycles' => 1]);
     *
     * @param token
     * @param options
     * @return Map<Object, Object>
     * @throws Exception
     */
    public Map<Object, Object> pause(String token, Map<String, Object> options) throws Exception {
        if (token == null || token.isEmpty()) {
            throw new InvalidRequestException("Required \"token\" parameter missing", 400);
        }
        try {
            Validate.validateOptions(options, new String[]{"cycles:int"});
            String response = Request.sendApiRequest("PUT", PATH + "/" + token + "/pause", options);
            return new Gson().fromJson(response, Map.class);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Unpause a subscription
     * $payfast->subscriptions->unpause('2afa4575-5628-051a-d0ed-4e071b56a7b0');
     *
     * @param token
     * @return Map<Object, Object>
     * @throws Exception
     */
    public Map<Object, Object> unpause(String token) throws Exception {
        if (token == null || token.isEmpty()) {
            throw new InvalidRequestException("Required \"token\" parameter missing", 400);
        }
        try {
            String response = Request.sendApiRequest("PUT", PATH + "/" + token + "/unpause");
            return new Gson().fromJson(response, Map.class);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Cancel a subscription
     * $payfast->subscriptions->cancel('2afa4575-5628-051a-d0ed-4e071b56a7b0');
     *
     * @param token
     * @return Map<Object, Object>
     * @throws Exception
     */
    public Map<Object, Object> cancel(String token) throws Exception {
        if (token == null || token.isEmpty()) {
            throw new InvalidRequestException("Required \"token\" parameter missing", 400);
        }
        try {
            String response = Request.sendApiRequest("PUT", PATH + "/" + token + "/cancel");
            return new Gson().fromJson(response, Map.class);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Update a subscription
     * $payfast->subscriptions->update('2afa4575-5628-051a-d0ed-4e071b56a7b0', ['cycles' => 1]);
     *
     * @param token
     * @param options
     * @return Map<Object, Object>
     * @throws Exception
     */
    public Map<Object, Object> update(String token, Map<String, Object> options) throws Exception {
        if (token == null || token.isEmpty()) {
            throw new InvalidRequestException("Required \"token\" parameter missing", 400);
        }
        try {
            Validate.validateOptions(options, new String[]{"cycles:int", "frequency:int", "run_date:date", "amount:int"});
            String response = Request.sendApiRequest("PATCH", PATH + "/" + token + "/update", options);
            return new Gson().fromJson(response, Map.class);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Charge a tokenization payment
     * $payfast->subscriptions->adhoc('290ac9a6-25f1-cce4-5801-67a644068818', ['amount' => 500, 'item_name' => 'Test adhoc']);
     *
     * @param token
     * @param options
     * @return Map<Object, Object>
     * @throws Exception
     */
    public Map<Object, Object> adhoc(String token, Map<String, Object> options) throws Exception {
        if (token == null || token.isEmpty()) {
            throw new InvalidRequestException("Required \"token\" parameter missing", 400);
        }
        if (options == null || !options.containsKey("amount") || options.get("amount").toString().isEmpty()) {
            throw new InvalidRequestException("Required \"amount\" parameter missing", 400);
        }
        if (options == null || !options.containsKey("item_name") || options.get("item_name").toString().isEmpty()) {
            throw new InvalidRequestException("Required \"item_name\" parameter missing", 400);
        }
        try {
            Validate.validateOptions(options, new String[]{"amount:int", "cc_cvv:int"});
            String response = Request.sendApiRequest("POST", PATH + "/" + token + "/adhoc", options);
            return new Gson().fromJson(response, Map.class);
        } catch (Exception e) {
            throw e;
        }
    }

}
