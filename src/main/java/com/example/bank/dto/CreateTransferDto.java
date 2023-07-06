package com.example.bank.dto;

import com.example.bank.domain.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Concrete DTO object for creating transfers from a source account to a
 * destination account.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateTransferDto extends AbstractCreateTransactionDto {

    @JsonIgnore
    @Min(value = 1L, message = "Invalid transfer destination account ID")
    @NotNull(message = "Transfer destination account ID required")
    private Long destinationAccountId;

    @JsonIgnore
    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }

}
