package com.kyalo.ecommerce.email;

import lombok.Getter;

public enum EmailTemplates {
    PAYMENT_SUCCESS("payment-success.html", "Payment Successfully Processed"),
    ORDER_CONFIRMATION("order-confirmation.html", "Order Confirmation Processed");

    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
