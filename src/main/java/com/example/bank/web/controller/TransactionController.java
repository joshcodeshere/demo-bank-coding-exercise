package com.example.bank.web.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.domain.Transaction;
import com.example.bank.domain.TransactionTransferType;
import com.example.bank.dto.CreateDepositDto;
import com.example.bank.dto.CreateTransferDto;
import com.example.bank.dto.CreateWithdrawlDto;
import com.example.bank.exception.TransactionDoesNotBelongToAccountException;
import com.example.bank.exception.TransactionDoesNotBelongToCustomerException;
import com.example.bank.service.TransactionService;
import com.example.bank.web.exception.GlobalExceptionHandling;
import com.example.bank.web.resource.TransactionResource;

import io.swagger.v3.oas.annotations.Operation;

/**
 * Transaction REST API operations.
 * 
 * @see TransactionService for service layer business logic
 * @see GlobalExceptionHandling for exception handling
 */
@RestController
@RequestMapping("api")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Operation(summary = "Get a transaction by ID")
    @GetMapping("customers/{customerId}/accounts/{accountId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResource> getTransaction(@PathVariable Long accountId,
            @PathVariable Long customerId,
            @PathVariable Long transactionId) {

        Transaction transaction = service.findById(transactionId);

        if (!accountId.equals(transaction.getAccount().getId())) {
            throw new TransactionDoesNotBelongToAccountException();
        }

        if (!customerId.equals(transaction.getCustomer().getId())) {
            throw new TransactionDoesNotBelongToCustomerException();
        }

        TransactionResource resource = new TransactionResource(transaction);

        resource.add(linkTo(methodOn(TransactionController.class).getTransaction(accountId, customerId, transactionId))
                .withSelfRel());

        return ResponseEntity.ok().body(resource);
    }

    @Operation(summary = "Get all transactions for account ID")
    @GetMapping("customers/{customerId}/accounts/{accountId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TransactionResource>> getAllTransactions(@PathVariable Long accountId,
            @PathVariable Long customerId) {
        List<Transaction> transactions = service.findTransactionsByAccountId(accountId);

        List<TransactionResource> resources = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (!accountId.equals(transaction.getAccount().getId())) {
                throw new TransactionDoesNotBelongToAccountException();
            }

            if (!customerId.equals(transaction.getCustomer().getId())) {
                throw new TransactionDoesNotBelongToCustomerException();
            }

            TransactionResource resource = new TransactionResource(transaction);

            resource.add(linkTo(
                    methodOn(TransactionController.class).getTransaction(accountId, customerId, transaction.getId()))
                    .withSelfRel());

            resources.add(resource);
        }

        return ResponseEntity.ok().body(resources);
    }

    @Operation(summary = "Process an account deposit")
    @PostMapping("customers/{customerId}/accounts/{accountId}/transactions/deposits")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResource> processDeposit(@PathVariable Long accountId,
            @PathVariable Long customerId,
            CreateDepositDto createDepositDto) {

        createDepositDto.setAccountId(accountId);
        createDepositDto.setCustomerId(customerId);

        Transaction transaction = service.process(createDepositDto);

        TransactionResource resource = new TransactionResource(transaction);

        Link selfRelLink = linkTo(methodOn(TransactionController.class).getTransaction(accountId, customerId,
                transaction.getId())).withSelfRel();
        resource.add(selfRelLink);
        resource.add(getCommonLinks(transaction.getCustomer().getId(),
                transaction.getId()));

        return ResponseEntity.created(selfRelLink.toUri()).body(resource);
    }

    @Operation(summary = "Process a transfer from one account to another")
    @PostMapping("customers/{customerId}/accounts/{accountId}/transactions/transfers/{destinationAccountId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TransactionResource>> processTransfer(@PathVariable Long accountId,
            @PathVariable Long customerId,
            @PathVariable Long destinationAccountId,
            CreateTransferDto createTransferDto) {

        createTransferDto.setAccountId(accountId);
        createTransferDto.setCustomerId(customerId);
        createTransferDto.setDestinationAccountId(destinationAccountId);

        List<Transaction> transactions = service.process(createTransferDto);

        List<TransactionResource> resources = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionResource resource = new TransactionResource(transaction);

            resource.add(linkTo(
                    methodOn(TransactionController.class).getTransaction(transaction.getId(),
                            transaction.getCustomer().getId(),
                            transaction.getId()))
                    .withSelfRel());

            if (TransactionTransferType.DESTINATION.equals(transaction.getTransferType())) {
                resource.add(linkTo(
                        methodOn(TransactionController.class).getTransaction(transaction.getId(),
                                transaction.getCustomer().getId(),
                                transaction.getId()))
                        .withRel(IanaLinkRelations.ORIGINAL));
            } else if (TransactionTransferType.SOURCE.equals(transaction.getTransferType())) {
                resource.add(linkTo(
                        methodOn(TransactionController.class).getTransaction(transaction.getId(),
                                transaction.getCustomer().getId(),
                                transaction.getId()))
                        .withRel(IanaLinkRelations.PAYMENT));
            }

            resource.add(getCommonLinks(transaction.getCustomer().getId(),
                    transaction.getId()));

            resources.add(resource);
        }

        return ResponseEntity.ok().body(resources);
    }

    @Operation(summary = "Process a withdrawl for an account")
    @PostMapping("customers/{customerId}/accounts/{accountId}/transactions/withdrawls")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResource> processWithdrawl(@PathVariable Long accountId,
            @PathVariable Long customerId,
            CreateWithdrawlDto createWithdrawlDto) {

        createWithdrawlDto.setAccountId(accountId);
        createWithdrawlDto.setCustomerId(customerId);

        Transaction transaction = service.process(createWithdrawlDto);

        TransactionResource resource = new TransactionResource(transaction);

        Link selfRelLink = linkTo(
                methodOn(TransactionController.class).getTransaction(transaction.getAccount().getId(),
                        transaction.getCustomer().getId(),
                        transaction.getId()))
                .withSelfRel();
        resource.add(selfRelLink);
        resource.add(getCommonLinks(transaction.getCustomer().getId(),
                transaction.getId()));

        return ResponseEntity.created(selfRelLink.toUri()).body(resource);
    }

    private List<Link> getCommonLinks(Long accountId, Long customerId) {
        List<Link> transactionLinks = new ArrayList<>();
        transactionLinks.add(
                linkTo(methodOn(AccountController.class).getAccount(customerId, accountId))
                        .withRel(IanaLinkRelations.ORIGINAL));
        transactionLinks.add(
                linkTo(methodOn(CustomerController.class).getCustomer(customerId))
                        .withRel(IanaLinkRelations.ORIGINAL));
        transactionLinks.add(
                linkTo(methodOn(TransactionController.class).getAllTransactions(accountId, customerId))
                        .withRel(IanaLinkRelations.COLLECTION));
        return transactionLinks;
    }

}
