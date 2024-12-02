package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import reactor.core.publisher.Mono;

@Component
public class TransactionCreator {
    private final TransactionRepository transactionRepository;

    public TransactionCreator(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Mono<Transaction> createTransaction(String type, double amount, Account sourceAccount, Account destinationAccount) {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setSourceAccount(sourceAccount.getId());

        if (destinationAccount != null) {
            transaction.setDestinationAccount(destinationAccount.getId());
        }

        return transactionRepository.save(transaction);
    }
}
