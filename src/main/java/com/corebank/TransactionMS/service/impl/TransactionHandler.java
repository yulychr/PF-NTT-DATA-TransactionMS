package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.transaction.DepositOperation;
import com.corebank.TransactionMS.service.transaction.TransferOperation;
import com.corebank.TransactionMS.service.transaction.WithdrawalOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TransactionHandler {

    private final DepositOperation depositOperation;
    private final WithdrawalOperation withdrawalOperation;
    private final TransferOperation transferOperation;

    public Mono<Transaction> handleDeposit(String accountNumber, double amount) {
        return depositOperation.execute(accountNumber, amount);
    }

    public Mono<Transaction> handleWithdrawal(String accountNumber, double amount) {
        return withdrawalOperation.execute(accountNumber, amount);
    }

    public Mono<Transaction> handleTransfer(String sourceAccount, String destinationAccount, double amount) {
        return transferOperation.execute(sourceAccount, destinationAccount, amount);
    }
}
