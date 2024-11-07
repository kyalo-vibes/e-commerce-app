package com.kyalo.ecommerce.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    public String createCustomer(CustomerRequest request) {
        var customer = mapper.toCustomer(request);
        repository.save(customer);
        return customer.getId();
    }
}
