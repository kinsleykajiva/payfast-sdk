package com.payfast;

import com.payfast.exceptions.InvalidRequestException;

import java.util.Map;
import java.util.Optional;

public class PayFastApi {

    public static class Builder {

        public Builder() {
        }
        public Builder merchantId(Integer merchantId) throws InvalidRequestException {
            if(merchantId == null){
                throw new InvalidRequestException("Missing parameter \"merchantId\"", 400);
            }
            PayFast.MERCHANT_ID = Optional.of(merchantId);
            return this;
        }
        public Builder passPhrase(String passPhrase) {
            passPhrase = passPhrase == null ? "" : passPhrase;
            PayFast.PASS_PHRASE = Optional.of(passPhrase);
            return this;
        }
        public Builder mechantKey(String merchantKey) {
            PayFast.MERCHANT_KEY = Optional.of(merchantKey);
            return this;
        }
        public Builder testMode(boolean testMode) {
            PayFast.IS_TEST_MODE =testMode;
            return this;
        }
        public PayFastApi build() {
            return new PayFastApi();
        }
    }
}
