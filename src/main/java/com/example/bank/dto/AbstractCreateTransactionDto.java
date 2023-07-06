package com.example.bank.dto;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.example.bank.domain.Transaction;
import com.example.bank.domain.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Abstract create DTO object for transactions.
 */
@Data
public abstract class AbstractCreateTransactionDto {

    @JsonIgnore
    @Min(value = 1L, message = "Invalid account ID")
    @NotNull(message = "Account ID required")
    private Long accountId;

    @Min(value = 1L, message = "Transaction amount must be greater that zero")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Invalid change fraction")
    @NotNull(message = "Amount required")
    private BigDecimal amount;

    @JsonIgnore
    @Min(value = 1L, message = "Invalid customer ID")
    @NotNull(message = "Customer ID required")
    private Long customerId;

    @Length(max = Transaction.DESCRIPTION_LENGTH)
    private String description;

    public abstract TransactionType getType();

}
