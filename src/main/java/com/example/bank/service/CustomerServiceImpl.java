package com.example.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bank.domain.Customer;
import com.example.bank.dto.CreateCustomerDto;
import com.example.bank.exception.CustomerNotFoundException;
import com.example.bank.exception.CustomerExistsWithSsnException;
import com.example.bank.repository.CustomerRepository;

/**
 * Business logic for customers.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer create(CreateCustomerDto createCustomerDto) {
        if (repository.existsCustomerBySsn(createCustomerDto.getSsn())) {
            throw new CustomerExistsWithSsnException();
        }
        return repository
                .save(Customer.builder().name(createCustomerDto.getName()).ssn(createCustomerDto.getSsn()).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(Long customerId) {
        return repository.existsById(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> findAll() {
        List<Customer> customers = repository.findAll();
        if (customers == null || customers.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        return customers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer findById(Long customerId) {
        Optional<Customer> customer = repository.findById(customerId);
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException();
        }
        return customer.get();
    }

}
