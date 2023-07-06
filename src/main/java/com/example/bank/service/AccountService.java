package com.example.bank.service;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.example.bank.domain.Account;
import com.example.bank.domain.Customer;
import com.example.bank.dto.CreateAccountDto;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.CustomerNotFoundException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Interface for {@link Account} banking operations.
 * 
 * Basic DTO validation occurs on service layer via {@link Validated} and
 * {@link Valid} annotations.
 */
@Validated
public interface AccountService {

        /**
         * Create a new {@link Account} for banking operations.
         * 
         * @param createAccountDto the {@link CreateAccountDto} to process
         * @return the newly created {@link Account} account
         * @throws ConstraintViolationException validation failure on
         *                                      {@link CreateAccountDto}
         * @throws CustomerNotFoundException    given DTO {@link Customer} is not found
         */
        Account create(@NotNull(message = "Missing create account DTO") @Valid CreateAccountDto createAccountDto);

        /**
         * @param accountId
         * @return true if the {@link Account} with accountId exists
         */
        boolean exists(@Min(value = 1L, message = "Invalid account ID") @NotNull(message = "Missing account ID") Long accountId);

        /**
         * @param customerId
         * @return the {@link Account} object(s) for the given customerId
         * @throws AccountNotFoundException     if {@link Account} object(s) not found
         * @throws ConstraintViolationException validation failure
         * @throws CustomerNotFoundException    given customerId is not found
         */
        List<Account> findAccountsByCustomerId(
                        @Min(value = 1L, message = "Invalid customer ID") @NotNull(message = "Missing customer ID") Long customerId);

        /**
         * @param accountId
         * @return the {@link Account} for the given accountId
         * @throws AccountNotFoundException     if {@link Account} object(s) not found
         * @throws ConstraintViolationException if validation fails
         */
        Account findById(
                        @Min(value = 1L, message = "Invalid account ID") @NotNull(message = "Missing account ID") Long accountId);

}
