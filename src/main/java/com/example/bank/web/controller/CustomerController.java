package com.example.bank.web.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.domain.Customer;
import com.example.bank.dto.CreateCustomerDto;
import com.example.bank.service.CustomerService;
import com.example.bank.web.exception.GlobalExceptionHandling;
import com.example.bank.web.resource.CustomerResource;

import io.swagger.v3.oas.annotations.Operation;

/**
 * Customer REST API operations.
 * 
 * @see CustomerService for service layer business logic
 * @see GlobalExceptionHandling for exception handling
 */
@RestController
@RequestMapping("api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Create a new customer")
    @PostMapping(path = "customers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomerResource> createCustomer(CreateCustomerDto createCustomerDto) {
        CustomerResource customer = new CustomerResource(customerService.create(createCustomerDto));

        Link selfRelLink = linkTo(methodOn(CustomerController.class).getCustomer(customer.getId())).withSelfRel();
        customer.add(selfRelLink);
        customer.add(getCommonLinks(customer.getId()));

        return ResponseEntity.created(selfRelLink.toUri()).body(customer);
    }

    @Operation(summary = "Get all customers")
    @GetMapping("customers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CustomerResource>> getAllCustomer() {
        List<Customer> customers = customerService.findAll();

        List<CustomerResource> resources = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerResource resource = new CustomerResource(customer);

            resource.add(linkTo(methodOn(CustomerController.class).getCustomer(customer.getId())).withSelfRel());
            resource.add(getCommonLinks(customer.getId()));

            resources.add(resource);
        }

        return ResponseEntity.ok().body(resources);
    }

    @Operation(summary = "Get a customer by ID")
    @GetMapping("customers/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CustomerResource> getCustomer(@PathVariable Long customerId) {
        CustomerResource customer = new CustomerResource(customerService.findById(customerId));

        customer.add(linkTo(methodOn(CustomerController.class).getCustomer(customer.getId())).withSelfRel());
        customer.add(getCommonLinks(customer.getId()));

        return ResponseEntity.ok().body(customer);
    }

    private List<Link> getCommonLinks(Long customerId) {
        List<Link> accountLinks = new ArrayList<>();
        accountLinks.add(
                linkTo(methodOn(AccountController.class).getAccounts(customerId)).withRel(IanaLinkRelations.RELATED));
        return accountLinks;
    }

}
