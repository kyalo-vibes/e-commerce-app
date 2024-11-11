package com.kyalo.ecommerce.product;

import com.kyalo.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public Integer createProduct(@Valid ProductRequest productRequest) {
        var product = mapper.toProduct(productRequest);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds = request
                .stream().map(ProductPurchaseRequest::productId)
                .toList();
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);
        if(productIds.size() != storedProducts.size()){
            throw new ProductPurchaseException("One or more products do not exists");
        }

        var storedRequests = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequests.get(i);
            if (product.getAvailableQuantity() < productRequest.quantity()){
                throw new ProductPurchaseException("Insufficient stock quantity for product with id :: " + productRequest.productId());
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);
            purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts;
    }

    public ProductResponse findById(Integer id) {
        return productRepository.findById(id)
                .map(mapper::toProductResponse)
                .orElseThrow(()->new EntityNotFoundException("Cannot find customer with id %s :: " + id));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
