package com.example.bank.exception;

public class AccountDoesNotBelongToCustomerException extends RuntimeException {

    public AccountDoesNotBelongToCustomerException() {
        super("Account does not belong to customer");
    }

}
