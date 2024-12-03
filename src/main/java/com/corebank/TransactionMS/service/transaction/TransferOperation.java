package com.corebank.TransactionMS.service.transaction;

import com.corebank.TransactionMS.model.Transaction;
import reactor.core.publisher.Mono;

public interface TransferOperation {
    Mono<Transaction> execute(String sourceAccount, String destinationAccount, double amount);
}
