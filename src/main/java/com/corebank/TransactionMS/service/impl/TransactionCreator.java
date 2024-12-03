package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import reactor.core.publisher.Mono;


@Component
public class TransactionCreator {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionCreator(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired


    public Mono<Transaction> createTransaction(String type, double amount, String sourceAccount, String destinationAccount) {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setSourceAccount(sourceAccount);

        if (destinationAccount != null) {
            transaction.setDestinationAccount(destinationAccount);
        }

        return transactionRepository.save(transaction);
    }
}
