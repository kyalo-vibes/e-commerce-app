package com.kyalo.ecommerce.kafka.order;

import com.kyalo.ecommerce.kafka.payment.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal amount,
        PaymentMethod method,
        Customer customer,
        List<Product> products
) {
}
