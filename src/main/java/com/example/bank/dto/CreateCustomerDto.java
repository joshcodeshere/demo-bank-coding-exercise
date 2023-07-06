package com.example.bank.dto;

import org.hibernate.validator.constraints.Length;

import com.example.bank.domain.Customer;
import com.example.bank.validation.ValidSsn;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO object for creating customers.
 * 
 * @see {@link ValidSsn} for example of custom DTO validation annotation
 */
@Data
public class CreateCustomerDto {

    @Length(max = Customer.NAME_LENGTH)
    @NotBlank(message = "Name required")
    private String name;

    @Digits(integer = 9, fraction = 0)
    @NotNull(message = "SSN required")
    @ValidSsn
    private Long ssn;

}
