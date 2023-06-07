package com.payfast;

import com.payfast.Services.CreditCardTransactions;
import com.payfast.Services.Refunds;
import com.payfast.Services.Subscriptions;
import com.payfast.Services.TransactionHistory;
import com.payfast.paymentintegrations.CustomIntegration;
import com.payfast.paymentintegrations.Notification;
import com.payfast.paymentintegrations.OnsiteIntegration;

import java.util.HashMap;
import java.util.Map;



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
