package com.example.bank.exception;

public class TransactionDoesNotBelongToCustomerException extends RuntimeException {

    public TransactionDoesNotBelongToCustomerException() {
        super("Transaction does not belong to customer");
    }

}
