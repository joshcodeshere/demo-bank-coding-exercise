package com.example.bank.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.domain.TransactionTransferType;
import com.example.bank.dto.CreateDepositDto;
import com.example.bank.dto.CreateTransferDto;
import com.example.bank.dto.CreateWithdrawlDto;
import com.example.bank.exception.AccountDoesNotBelongToCustomerException;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.DepositMustBeGreaterThanZeroException;
import com.example.bank.exception.TransactionHasInsufficientFundsException;
import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.exception.TransactionSourceCannotEqualDestination;
import com.example.bank.exception.TransferMustBeGreaterThanZeroException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Interface for {@link Transaction} banking operations.
 * 
 * Basic DTO validation occurs on service layer via {@link Validated} and
 * {@link Valid} annotations.
 */
@Validated
public interface TransactionService {

        /**
         * @param transactionId
         * @return the {@link Transaction} for the given transactionId
         * @throws TransactionNotFoundException if {@link Transaction} does not exist
         */
        Transaction findById(
                        @Min(value = 1L, message = "Invalid transaction ID") @NotNull(message = "Missing transaction ID") Long transactionId);

        /**
         * Check the current active {@link Transaction} for the {@link Account} current
         * balance.
         * 
         * @param accountId
         * @return the current {@link Account} balance based upon the
         *         {@link Transaction#getIsCurrent()}
         */
        BigDecimal getCurrentAccountBalance(
                        @Min(value = 1L, message = "Invalid account ID") @NotNull(message = "Missing account ID") Long accountId);

        /**
         * @param accountId
         * @return {@link Transaction} object(s) for the given accountId
         */
        List<Transaction> findTransactionsByAccountId(
                        @Min(value = 1L, message = "Invalid account ID") @NotNull(message = "Missing account ID") Long accountId);

        /**
         * Process a {@link CreateDepositDto}.
         * 
         * @param createDepositDto the {@link CreateDepositDto} to process
         * @return the newly created, and current, {@link Transaction} account
         * @throws AccountDoesNotBelongToCustomerException validation failure where
         *                                                 {@link CreateDepositDto#getAccountId()}
         *                                                 does not align with
         *                                                 {@link CreateDepositDto#getCustomerId()}
         * @throws AccountNotFoundException                if {@link Account} object(s)
         *                                                 not found
         * @throws ConstraintViolationException            validation failure on
         *                                                 {@link CreateDepositDto}
         * @throws DepositMustBeGreaterThanZeroException   {@link CreateDepositDto#getAmount()}
         *                                                 is not greater than zero
         */
        Transaction process(@NotNull(message = "Missing create deposit DTO") @Valid CreateDepositDto createDepositDto);

        /**
         * Process a {@link CreateTransferDto}.
         * 
         * @param createTransferDto the {@link CreateTransferDto} to process
         * @return the newly created, and current, {@link Transaction} objects for both
         *         {@link TransactionTransferType#SOURCE} and
         *         {@link TransactionTransferType#DESTINATION}
         * @throws AccountDoesNotBelongToCustomerException  validation failure where
         *                                                  {@link CreateTransferDto#getAccountId()}
         *                                                  does not align with
         *                                                  {@link CreateTransferDto#getCustomerId()}
         * @throws AccountNotFoundException                 if {@link Account} object(s)
         *                                                  not found
         * @throws ConstraintViolationException             validation failure on
         *                                                  {@link CreateTransferDto}
         * @throws TransactionHasInsufficientFundsException validation failure where the
         *                                                  soure
         *                                                  {@link CreateTransferDto#getAccountId()}
         *                                                  does not have enough funds
         *                                                  to support the transfer
         * @throws TransferMustBeGreaterThanZeroException   {@link CreateTransferDto#getAmount()}
         *                                                  is not greater than zero
         * @throws TransactionSourceCannotEqualDestination  validation failure where
         *                                                  {@link CreateTransferDto#getAccountId()}
         *                                                  attempt transfer into
         *                                                  {@link CreateTransferDto#getDestinationAccountId()}
         */
        List<Transaction> process(
                        @NotNull(message = "Missing create transfer DTO") @Valid CreateTransferDto createTransferDto);

        /**
         * Process a {@link CreateWithdrawlDto}.
         * 
         * @param createWithdrawlDto the {@link CreateWithdrawlDto} to process
         * @return the newly created, and current, {@link Transaction}
         *         object
         * @throws AccountDoesNotBelongToCustomerException  validation failure where
         *                                                  {@link CreateWithdrawlDto#getAccountId()}
         *                                                  does not align with
         *                                                  {@link CreateWithdrawlDto#getCustomerId()}
         * @throws ConstraintViolationException             validation failure on
         *                                                  {@link CreateWithdrawlDto}
         * @throws TransactionHasInsufficientFundsException {@link CreateWithdrawlDto#getAccountId()}
         *                                                  does not have enough funds
         *                                                  to support the transfer
         * @throws WithdrawlMustBeGreaterThanZeroException  {@link CreateWithdrawlDto#getAmount()}
         *                                                  is not greater than zero
         */
        Transaction process(
                        @NotNull(message = "Missing create withdrawl DTO") @Valid CreateWithdrawlDto createWithdrawlDto);

}
