package com.kyalo.ecommerce.order;

import com.kyalo.ecommerce.customer.CustomerClient;
import com.kyalo.ecommerce.exception.BusinessException;
import com.kyalo.ecommerce.product.ProductClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    public Integer createOrder(@Valid OrderRequest orderRequest) {
        // check the customer --> OpenFeign
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No customer exists for provided id " + orderRequest.customerId()));
        // purchase the product --> product-ms

        // persist order

        // persist order lines

        // start payment process

        // send the confirmation  --> notification-ms (kafka)
        return null;
    }
}
