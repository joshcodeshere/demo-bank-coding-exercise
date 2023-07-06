package com.example.bank.exception;

public class DepositMustBeGreaterThanZeroException extends RuntimeException {

    public DepositMustBeGreaterThanZeroException() {
        super("Deposit must be greater than zero");
    }

}
