package com.example.bank.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException() {
        super("Account(s) not found");
    }

}
