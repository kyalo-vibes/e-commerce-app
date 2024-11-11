package com.kyalo.ecommerce.product;

import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(
        @NotNull(message = "Product id is mandatory")
        Integer productId,
        @NotNull(message = "Product quantity is mandatory")
        double quantity
) {
}
