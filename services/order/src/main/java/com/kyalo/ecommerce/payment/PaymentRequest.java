package com.kyalo.ecommerce.payment;

import com.kyalo.ecommerce.customer.CustomerResponse;
import com.kyalo.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
