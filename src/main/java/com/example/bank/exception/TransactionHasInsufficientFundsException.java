package com.example.bank.exception;

public class TransactionHasInsufficientFundsException extends RuntimeException {

    public TransactionHasInsufficientFundsException() {
        super("Transaction has insufficient funds");
    }

}
