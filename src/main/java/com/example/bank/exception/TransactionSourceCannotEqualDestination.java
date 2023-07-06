package com.example.bank.exception;

public class TransactionSourceCannotEqualDestination extends RuntimeException {

    public TransactionSourceCannotEqualDestination() {
        super("Transaction source cannot equal destination");
    }

}
