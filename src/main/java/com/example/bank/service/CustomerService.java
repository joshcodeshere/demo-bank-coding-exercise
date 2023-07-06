package com.example.bank.service;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.example.bank.domain.Customer;
import com.example.bank.dto.CreateCustomerDto;
import com.example.bank.exception.CustomerExistsWithSsnException;
import com.example.bank.exception.CustomerNotFoundException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Interface for {@link Customer} banking operations.
 * 
 * Basic DTO validation occurs on service layer via {@link Validated} and
 * {@link Valid} annotations.
 */
@Validated
public interface CustomerService {

        /**
         * Create a new {@link Customer} for banking operations.
         * 
         * @param createCustomerDto the {@link CreateCustomerDto} to process
         * @return The newly created {@link Customer}.
         * @throws ConstraintViolationException   validation failure on
         *                                        {@link CreateCustomerDto}
         * @throws CustomerExistsWithSsnException a {@link Customer} with
         *                                        {@link Customer#getSsn()} already
         *                                        exists
         */
        Customer create(@NotNull(message = "Missing create customer DTO") @Valid CreateCustomerDto createCustomerDto);

        /**
         * @param customerId
         * @return true if the {@link Customer} with customerId exists
         */
        boolean exists(@Min(value = 1L, message = "Invalid customer ID") @NotNull(message = "Missing customer ID") Long customerId);

        /**
         * @return all {@link Customer} object(s)
         * @throws CustomerNotFoundException if no {@link Customer} exists
         */
        List<Customer> findAll();

        /**
         * @param customerId
         * @return the {@link Customer} for the given customerId
         * @throws CustomerNotFoundException if {@link Customer} does not exist
         */
        Customer findById(
                        @Min(value = 1L, message = "Invalid customer ID") @NotNull(message = "Missing customer ID") Long customerId);

}
