package com.payfast;

import com.payfast.exceptions.InvalidRequestException;

import java.util.Optional;

public abstract class PayFast {

    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 80 * 1000;
    public static final String API_VERSION = "v0.1.0";
    public static boolean IS_TEST_MODE = false;
    private static final String LIVE_API_URL = "https://api.payfast.co.za";
    private static final String TEST_API_URL = "https://sandbox.payfast.co.za";
    private static final String API_URL = IS_TEST_MODE ? TEST_API_URL : LIVE_API_URL;
    public static volatile Optional<Integer> MERCHANT_ID = Optional.empty();
    public static volatile Optional<String> MERCHANT_KEY = Optional.empty();
    public static volatile Optional<String> PASS_PHRASE = Optional.empty();

    /**
     * Get the API URL based on the test mode flag
     *
     * @return the API URL
     */
    public static String getApiUrl() {
        return API_URL;
    }

    /**
     * @return is Access is in sandbox or live mode
     */
     public static boolean isAppInTestMode() {
        return IS_TEST_MODE;
    }



}
