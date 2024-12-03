package com.corebank.TransactionMS.service.transaction.operation;

import com.corebank.TransactionMS.model.Transaction;
import reactor.core.publisher.Mono;

public interface DepositOperation {
    Mono<Transaction> execute(String accountNumber, double amount);
}
