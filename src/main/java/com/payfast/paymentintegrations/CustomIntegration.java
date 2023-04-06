package com.payfast.paymentintegrations;

import com.payfast.exceptions.InvalidRequestException;

import java.util.Map;

public class CustomIntegration {

    /**
     * Payment form data
     *
     * @param data
     * @param buttonParams
     * @return
     * @throws InvalidRequestException
     */
    public String createFormFields(Map<String, String> data, Map<String, String> buttonParams)
            throws InvalidRequestException {
        if (!data.containsKey("amount")) {
            throw new InvalidRequestException("Required 'amount' parameter missing", 400);
        }
        double amount = Double.parseDouble(data.get("amount"));
        data.put("amount", String.format("%.2f", amount));

        if (!data.containsKey("item_name")) {
            throw new InvalidRequestException("Required 'item_name' parameter missing", 400);
        }

        data.put("merchant_id", PayFastPayment.getMerchantId());
        data.put("merchant_key", PayFastPayment.getMerchantKey());

        String signature = Auth.generateSignature(data, PayFastPayment.getPassPhrase());
        data.put("signature", signature);

        StringBuilder htmlForm = new StringBuilder("<form action=\"" + PayFastPayment.getBaseUrl() + "/eng/process\" method=\"post\">");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            htmlForm.append("<input name=\"").append(entry.getKey()).append("\" type=\"hidden\" value=\"").append(entry.getValue()).append("\" />");
        }

        String buttonValue = "Pay Now";
        if (!buttonParams.isEmpty() && buttonParams.containsKey("value")) {
            buttonValue = buttonParams.get("value");
        }
        String additionalOptions = "";
        for (Map.Entry<String, String> entry : buttonParams.entrySet()) {
            additionalOptions += entry.getKey() + "=\"" + entry.getValue() + "\" ";
        }

        htmlForm.append("<input type=\"submit\" value=\"").append(buttonValue).append("\" ").append(additionalOptions).append("/>");

        htmlForm.append("</form>");
        return htmlForm.toString();
    }

    /**
     * @param token
     * @param returnUrl
     * @param linkText
     * @param linkParams
     * @return
     * @throws InvalidRequestException
     */
    public String createCardUpdateLink(String token, String returnUrl, String linkText, Map<String, String> linkParams)
            throws InvalidRequestException {
        if (token == null) {
            throw new InvalidRequestException("Required 'token' parameter missing", 400);
        }

        if (PayFastPayment.isTestMode()) {
            throw new InvalidRequestException("Sorry but this feature is not available in Sandbox mode", 400);
        }

        String additionalOptions = "";
        for (Map.Entry<String, String> entry : linkParams.entrySet()) {
            additionalOptions += entry.getKey() + "=\"" + entry.getValue() + "\" ";
        }

        String url = PayFastPayment.getBaseUrl() + "/eng/recurring/update/" + token;
        if (returnUrl != null) {
            url += "?return=" + returnUrl;
        }

        return "<a href=\"" + url + "\" " + additionalOptions + ">" + linkText + "</a>";
    }
}
