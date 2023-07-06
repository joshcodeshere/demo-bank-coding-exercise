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

import com.example.bank.domain.Account;
import com.example.bank.dto.CreateAccountDto;
import com.example.bank.exception.AccountDoesNotBelongToCustomerException;
import com.example.bank.service.AccountService;
import com.example.bank.web.exception.GlobalExceptionHandling;
import com.example.bank.web.resource.AccountResource;

import io.swagger.v3.oas.annotations.Operation;

/**
 * Account REST API operations.
 * 
 * @see AccountService for service layer business logic
 * @see GlobalExceptionHandling for exception handling
 */
@RestController
@RequestMapping("api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Create a new account for an existing customer")
    @PostMapping(path = "customers/{customerId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountResource> createAccount(CreateAccountDto createAccountDto,
            @PathVariable Long customerId) {
        Account account = accountService.create(createAccountDto);

        AccountResource resource = new AccountResource(account);

        Link selfRelLink = linkTo(methodOn(AccountController.class).getAccount(account.getId(), customerId))
                .withSelfRel();
        resource.add(selfRelLink);
        resource.add(getCommonLinks(account.getId(), account.getCustomer().getId()));

        return ResponseEntity.created(selfRelLink.toUri()).body(resource);
    }

    @Operation(summary = "Get an account by ID")
    @GetMapping("customers/{customerId}/accounts/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountResource> getAccount(@PathVariable Long accountId, @PathVariable Long customerId) {
        Account account = accountService.findById(accountId);

        if (!customerId.equals(account.getCustomer().getId())) {
            throw new AccountDoesNotBelongToCustomerException();
        }

        AccountResource resource = new AccountResource(account);

        resource.add(linkTo(methodOn(AccountController.class).getAccount(account.getId(), customerId))
                .withSelfRel());
        resource.add(getCommonLinks(account.getId(), account.getCustomer().getId()));

        return ResponseEntity.ok().body(resource);
    }

    @Operation(summary = "Get all accounts for customer ID")
    @GetMapping("customers/{customerId}/accounts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AccountResource>> getAccounts(@PathVariable Long customerId) {
        List<Account> accounts = accountService.findAccountsByCustomerId(customerId);

        List<AccountResource> resources = new ArrayList<>();

        for (Account account : accounts) {
            AccountResource resource = new AccountResource(account);

            resource.add(
                    linkTo(methodOn(AccountController.class).getAccount(account.getId(), account.getCustomer().getId()))
                            .withSelfRel());
            resource.add(getCommonLinks(account.getId(), account.getCustomer().getId()));

            resources.add(resource);
        }

        return ResponseEntity.ok().body(resources);
    }

    private List<Link> getCommonLinks(Long accountId, Long customerId) {
        List<Link> transactionLinks = new ArrayList<>();
        transactionLinks.add(
                linkTo(methodOn(AccountController.class).getAccounts(customerId))
                        .withRel(IanaLinkRelations.COLLECTION));
        transactionLinks.add(
                linkTo(methodOn(CustomerController.class).getCustomer(customerId))
                        .withRel(IanaLinkRelations.ORIGINAL));
        transactionLinks.add(
                linkTo(methodOn(TransactionController.class).getAllTransactions(accountId, customerId))
                        .withRel(IanaLinkRelations.RELATED));
        return transactionLinks;
    }

}
