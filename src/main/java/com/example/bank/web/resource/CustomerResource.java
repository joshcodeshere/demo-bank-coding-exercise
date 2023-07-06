package com.example.bank.web.resource;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.example.bank.domain.Customer;

/**
 * {@link Customer} HATEOAS resource.
 */
public class CustomerResource extends RepresentationModel<CustomerResource> {

    /**
     * Prevent domain model to be exposed fully on REST API.
     */
    private Customer customer;

    public CustomerResource(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("No customer to build resource");
        }
        this.customer = customer;
    }

    public LocalDateTime getDateCreated() {
        return this.customer.getDateCreated();
    }

    public LocalDateTime getDateUpdated() {
        return this.customer.getDateUpdated();
    }

    public Long getId() {
        return this.customer.getId();
    }

    public String getName() {
        return this.customer.getName();
    }

}
