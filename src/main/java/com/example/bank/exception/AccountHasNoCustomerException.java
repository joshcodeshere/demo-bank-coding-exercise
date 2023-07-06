package com.example.bank.exception;

public class AccountHasNoCustomerException extends RuntimeException {

    public AccountHasNoCustomerException() {
        super("Account has no customer");
    }

}
