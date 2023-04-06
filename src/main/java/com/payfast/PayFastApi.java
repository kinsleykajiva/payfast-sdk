package com.payfast;

import com.payfast.exceptions.InvalidRequestException;

import java.util.Map;
public class PayFastApi {

    /** Base URL for the API */
    public static String apiUrl = "https://api.payfast.co.za";
    /** The merchant ID as given by the PayFast system */
    public static Integer merchantId;
    /** The passphrase is used to salt the signature */
    public static String passPhrase;
    /** The API version used for API requests */
    public static String version;
    /** Test / sandbox mode */
    public static Boolean testMode;

    private PayFastApi(Builder builder) throws InvalidRequestException {
        if (builder.merchantId != null) {
            merchantId = builder.merchantId;
        } else {
            throw new InvalidRequestException("Missing parameter \"merchantId\"", 400);
        }

        passPhrase = builder.passPhrase;
        testMode = builder.testMode;
        version = builder.version;
    }

   /* public Object __get(String property) throws Exception {
        Services service = ServiceMapper.getService(property);
        if (service != null) {
            return service.create();
        }

        throw new RuntimeException("Unknown method");
    }*/

    public static class Builder {
        private Integer merchantId;
        private String passPhrase;
        private Boolean testMode;
        private String version;

        public Builder() {
            // Set default values for optional parameters
            testMode = false;
            version = "v1";
        }

        public Builder merchantId(Integer merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public Builder passPhrase(String passPhrase) {
            this.passPhrase = passPhrase;
            return this;
        }

        public Builder testMode(Boolean testMode) {
            this.testMode = testMode;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public PayFastApi build() throws InvalidRequestException {
            return new PayFastApi(this);
        }
    }
}
