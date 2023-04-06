package com.payfast;

import java.util.HashMap;
import java.util.Map;


/*
import PayFast.PaymentIntegrations.CustomIntegration;
import PayFast.PaymentIntegrations.Notification;
import PayFast.PaymentIntegrations.OnsiteIntegration;
import PayFast.Services.CreditCardTransactions;
import PayFast.Services.Refunds;
import PayFast.Services.Subscriptions;
import PayFast.Services.TransactionHistory;
*/

public class ServiceMapper {
    private static final Map<String, Class<?>> MAP = new HashMap<>();

    static {
        MAP.put("custom", CustomIntegration.class);
        MAP.put("onsite", OnsiteIntegration.class);
        MAP.put("notification", Notification.class);
        MAP.put("transactionHistory", TransactionHistory.class);
        MAP.put("subscriptions", Subscriptions.class);
        MAP.put("creditCardTransactions", CreditCardTransactions.class);
        MAP.put("refunds", Refunds.class);
    }

    public static Class<?> getClass(String name) {
        return MAP.getOrDefault(name, null);
    }
}
