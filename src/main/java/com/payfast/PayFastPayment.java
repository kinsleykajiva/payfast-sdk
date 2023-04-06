package com.payfast;

import com.payfast.exceptions.InvalidRequestException;

import java.util.Map;

public class PayFastPayment {
    /** Base URL for the API */
    public static String baseUrl;
    /** The merchant ID as given by the PayFast system */
    public static Integer merchantId;
    /** The merchant Key as given by the PayFast system */
    public static String merchantKey;
    /** The passphrase is used to salt the signature */
    public static String passPhrase;
    /** Test / sandbox mode */
    public static Boolean testMode;
    /** Error messages */
    public static String[] errorMsg;

    public PayFastPayment(Map<String, Object> setup) throws InvalidRequestException {
        if (setup.containsKey("merchantId")) {
            merchantId = Integer.parseInt(setup.get("merchantId").toString());
        } else {
            throw new InvalidRequestException("Missing parameter \"merchantId\"", 400);
        }

        if (setup.containsKey("merchantKey")) {
            merchantKey = setup.get("merchantKey").toString();
        } else {
            throw new InvalidRequestException("Missing parameter \"merchantKey\"", 400);
        }

        passPhrase = setup.containsKey("passPhrase") ? setup.get("passPhrase").toString() : null;
        testMode = setup.containsKey("testMode") ? Boolean.parseBoolean(setup.get("testMode").toString()) : false;
        baseUrl = testMode ? "https://sandbox.payfast.co.za" : "https://www.payfast.co.za";
    }

  /*  public Object __get(String property) throws Exception {
        Services service = ServiceMapper.getService(property);
        if (service != null) {
            return service.create();
        }

        throw new Exception("Unknown method");
    }*/

    public static void setTestMode(Boolean value) {
        testMode = value;
        baseUrl = testMode ? "https://sandbox.payfast.co.za" : "https://www.payfast.co.za";
    }
}
