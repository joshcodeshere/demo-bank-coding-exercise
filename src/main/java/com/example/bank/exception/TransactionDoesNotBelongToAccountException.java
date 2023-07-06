package com.example.bank.exception;

public class TransactionDoesNotBelongToAccountException extends RuntimeException {

    public TransactionDoesNotBelongToAccountException() {
        super("Transaction does not belong to account");
    }

}
