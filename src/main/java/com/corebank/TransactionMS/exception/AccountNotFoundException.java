package com.corebank.TransactionMS.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String accountNumber) {
        super(accountNumber);
    }
}
