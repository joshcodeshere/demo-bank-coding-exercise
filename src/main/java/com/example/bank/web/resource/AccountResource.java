package com.example.bank.web.resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.example.bank.domain.Account;
import com.example.bank.domain.AccountType;

/**
 * {@link Account} HATEOAS resource.
 */
public class AccountResource extends RepresentationModel<AccountResource> {

    /**
     * Prevent domain model to be exposed fully on REST API.
     */
    private Account account;

    public AccountResource(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("No account to build resource");
        }
        this.account = account;
    }

    public BigDecimal getBalance() {
        return this.account.getBalance();
    }

    public LocalDateTime getDateCreated() {
        return this.account.getDateCreated();
    }

    public LocalDateTime getDateUpdated() {
        return this.account.getDateUpdated();
    }

    public Long getId() {
        return this.account.getId();
    }

    public AccountType getType() {
        return this.account.getType();
    }

}
