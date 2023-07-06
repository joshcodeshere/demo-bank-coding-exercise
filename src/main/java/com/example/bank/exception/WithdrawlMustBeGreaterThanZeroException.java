package com.example.bank.exception;

public class WithdrawlMustBeGreaterThanZeroException extends RuntimeException {

    public WithdrawlMustBeGreaterThanZeroException() {
        super("Withdrawl must be greater than zero");
    }

}
