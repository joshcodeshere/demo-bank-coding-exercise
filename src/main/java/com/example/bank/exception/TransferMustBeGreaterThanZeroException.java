package com.example.bank.exception;

public class TransferMustBeGreaterThanZeroException extends RuntimeException {

    public TransferMustBeGreaterThanZeroException() {
        super("Transfer must be greater than zero");
    }

}
