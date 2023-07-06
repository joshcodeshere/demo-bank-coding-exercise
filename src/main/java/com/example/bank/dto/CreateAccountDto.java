package com.example.bank.dto;

import com.example.bank.domain.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * DTO object for creating accounts.
 */
@Builder
@Data
public class CreateAccountDto {

    @JsonIgnore
    @Min(value = 1L, message = "Invalid customer ID")
    @NotNull
    private Long customerId;

    @NotNull(message = "Account type required")
    private AccountType type;
}
