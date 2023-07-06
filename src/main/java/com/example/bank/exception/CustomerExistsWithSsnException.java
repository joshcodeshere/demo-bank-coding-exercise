package com.example.bank.exception;

public class CustomerExistsWithSsnException extends RuntimeException {

    public CustomerExistsWithSsnException() {
        super("Customer exists with SSN");
    }

}
