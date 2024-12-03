package com.corebank.TransactionMS.service;

import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.impl.TransactionCreator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceT {
    private final TransactionCreator transactionCreator;

    public TransactionServiceT(TransactionCreator transactionCreator) {
        this.transactionCreator = transactionCreator;
    }

    public Mono<Transaction> createTransaction(String type, double amount, String sourceAccount, String destinationAccount) {
        return transactionCreator.createTransaction(type, amount, sourceAccount, destinationAccount);
    }
}
