package com.kyalo.ecommerce.customer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Customer {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
}
