package com.example.bank.dto;

import com.example.bank.domain.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Concrete DTO object for creating deposits.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateDepositDto extends AbstractCreateTransactionDto {

    @JsonIgnore
    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }

}
