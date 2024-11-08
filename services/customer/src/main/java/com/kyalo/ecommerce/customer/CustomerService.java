package com.kyalo.ecommerce.customer;

import com.kyalo.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void updateCustomer(CustomerRequest request) {
        var customer = repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot update customer :: No customer with id %s", request.id()))
                );
        mergeCustomer(customer, request);
        repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if(StringUtils.isNotBlank(request.lastName())) {
            customer.setLastName(request.lastName());
        }
        if(StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if(request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> getCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::toCustomerResponse)
                .collect(Collectors.toList());
    }

    public Boolean customerExists(String customerId) {
        return repository.existsById(customerId);
    }

    public CustomerResponse getCustomerById(String customerId) {
        var customer = repository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot get customer :: No customer with id %s", customerId)));
        return mapper.toCustomerResponse(customer);
    }

    public void deleteCustomer(String customerId) {
        repository.deleteById(customerId);
    }
}
