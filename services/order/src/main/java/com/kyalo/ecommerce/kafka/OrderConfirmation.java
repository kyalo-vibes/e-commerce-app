package com.kyalo.ecommerce.kafka;

import com.kyalo.ecommerce.customer.CustomerResponse;
import com.kyalo.ecommerce.order.PaymentMethod;
import com.kyalo.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
