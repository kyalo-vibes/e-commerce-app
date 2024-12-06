package com.kyalo.ecommerce.order;

import com.kyalo.ecommerce.customer.CustomerClient;
import com.kyalo.ecommerce.exception.BusinessException;
import com.kyalo.ecommerce.kafka.OrderConfirmation;
import com.kyalo.ecommerce.kafka.OrderProducer;
import com.kyalo.ecommerce.orderline.OrderLineRequest;
import com.kyalo.ecommerce.orderline.OrderLineService;
import com.kyalo.ecommerce.payment.PaymentClient;
import com.kyalo.ecommerce.payment.PaymentRequest;
import com.kyalo.ecommerce.product.ProductClient;
import com.kyalo.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(@Valid OrderRequest orderRequest) {
        // check the customer --> OpenFeign
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No customer exists for provided id " + orderRequest.customerId()));
        // purchase the product --> product-ms
        var purchasedProducts = this.productClient.purchaseProducts(orderRequest.products());
        // persist order lines
        var order = this.repository.save(mapper.toOrder(orderRequest));

        for (PurchaseRequest purchaseRequest : orderRequest.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        // todo start payment process
        var paymentRequest = new PaymentRequest(
            orderRequest.amount(),
            orderRequest.paymentMethod(),
                orderRequest.id(),
                orderRequest.reference(),
                customer
        );

        paymentClient.requestOrderPayment(paymentRequest);

        // send the confirmation  --> notification-ms (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequest.reference(),
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        customer,
                        purchasedProducts

                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findOrder(Integer id) {
        return repository.findById(id)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order not found with id:: %d", id)));
    }
}
