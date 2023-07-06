package com.example.bank.web.resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.example.bank.domain.Transaction;
import com.example.bank.domain.TransactionTransferType;
import com.example.bank.domain.TransactionType;

/**
 * {@link Transaction} HATEOAS resource.
 */
public class TransactionResource extends RepresentationModel<TransactionResource> {

    /**
     * Prevent domain model to be exposed fully on REST API.
     */
    private Transaction transaction;

    public TransactionResource(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("No transaction to build resource");
        }
        this.transaction = transaction;
    }

    public BigDecimal getAmount() {
        return this.transaction.getAmount();
    }

    public BigDecimal getCurrentBalance() {
        return this.transaction.getCurrentBalance();
    }

    public LocalDateTime getDateCreated() {
        return this.transaction.getDateCreated();
    }

    public String getDescription() {
        return this.transaction.getDescription();
    }

    public Long getId() {
        return this.transaction.getId();
    }

    public Boolean getIsCurrent() {
        return this.transaction.getIsCurrent();
    }

    public TransactionTransferType getTransferType() {
        return this.transaction.getTransferType();
    }

    public TransactionType getType() {
        return this.transaction.getType();
    }

}
