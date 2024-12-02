package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TransactionCreator {
    private final TransactionRepository transactionRepository;

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
