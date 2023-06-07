package com.payfast.paymentintegrations;

import com.payfast.Auth;
import com.payfast.PayFast;
import com.payfast.exceptions.InvalidRequestException;

import java.util.Map;

public class CustomIntegration {

    /**
     * Payment form data
     *
     */
    public String createFormFields(Map<String, String> data, Map<String, String> buttonParams) throws InvalidRequestException {
        if (!data.containsKey("amount")) {
            throw new InvalidRequestException("Required 'amount' parameter missing", 400);
        }
        double amount = Double.parseDouble(data.get("amount"));
        data.put("amount", String.format("%.2f", amount));

        if (!data.containsKey("item_name")) {
            throw new InvalidRequestException("Required 'item_name' parameter missing", 400);
        }

        data.put("merchant_id", String.valueOf(PayFast.MERCHANT_ID.get()));
        data.put("merchant_key", PayFast.MERCHANT_KEY.get());

        String signature = Auth.generateSignature(data, PayFast.PASS_PHRASE.get());
        data.put("signature", signature);

        StringBuilder htmlForm = new StringBuilder(String.format("<form action=\"%s/eng/process\" method=\"post\">", PayFast.getApiUrl()));
        for (Map.Entry<String, String> entry : data.entrySet()) {
            htmlForm.append(String.format("<input name=\"%s\" type=\"hidden\" value=\"%s\" />", entry.getKey(), entry.getValue()));
        }

        String buttonValue = buttonParams.getOrDefault("value", "Pay Now");
        StringBuilder additionalOptions = new StringBuilder();
        for (Map.Entry<String, String> entry : buttonParams.entrySet()) {
            if (!"value".equals(entry.getKey())) {
                additionalOptions.append(String.format("%s=\"%s\" ", entry.getKey(), entry.getValue()));
            }
        }

        htmlForm.append(String.format("<input type=\"submit\" value=\"%s\" %s/>", buttonValue, additionalOptions));

        htmlForm.append("</form>");
        return htmlForm.toString();
    }


    /**
     * Create a card update link
     *
     * @param token       The token associated with the card to be updated
     * @param returnUrl   The URL to return to after card update (optional)
     * @param linkText    The text to display for the link
     * @param linkParams  Additional parameters for the link (optional)
     * @return The HTML code for the card update link
     * @throws InvalidRequestException If required parameters are missing
     */
    public String createCardUpdateLink(String token, String returnUrl, String linkText, Map<String, String> linkParams)
            throws InvalidRequestException {
        if (token == null) {
            throw new InvalidRequestException("Required 'token' parameter missing", 400);
        }

        if (PayFast.isAppInTestMode()) {
            throw new InvalidRequestException("Sorry, but this feature is not available in Sandbox mode", 400);
        }

        StringBuilder htmlLink = new StringBuilder("<a href=\"");
        htmlLink.append(PayFast.getApiUrl()).append("/eng/recurring/update/").append(token);

        if (returnUrl != null) {
            htmlLink.append("?return=").append(returnUrl);
        }

        htmlLink.append("\"");

        for (Map.Entry<String, String> entry : linkParams.entrySet()) {
            htmlLink.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }

        htmlLink.append(">").append(linkText).append("</a>");

        return htmlLink.toString();
    }

}
