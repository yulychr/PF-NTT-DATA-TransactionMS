package com.corebank.TransactionMS.service.transaction;

import com.corebank.TransactionMS.model.Transaction;
import reactor.core.publisher.Mono;

public interface TransactionOperation {
    Mono<Transaction> execute(String accountNumber, double amount);
    Mono<Transaction> execute(String sourceAccount, String destinationAccount, double amount);  // Para Transferencias
}
