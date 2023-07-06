package com.example.bank.exception;

public class NoAccountsForCustomer extends RuntimeException {

    public NoAccountsForCustomer() {
        super("No accounts for customer");
    }

}
