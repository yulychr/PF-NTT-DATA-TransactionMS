package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.transaction.impl.DepositOperationImpl;
import com.corebank.TransactionMS.service.transaction.impl.TransferOperationImpl;
import com.corebank.TransactionMS.service.transaction.impl.WithdrawalOperationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {

    private DepositOperationImpl depositOperation;
    private WithdrawalOperationImpl withdrawalOperation;
    private  TransferOperationImpl transferOperation;

    @Autowired
    public TransactionHandler(DepositOperationImpl depositOperation, WithdrawalOperationImpl withdrawalOperation, TransferOperationImpl transferOperation) {
        this.depositOperation = depositOperation;
        this.withdrawalOperation = withdrawalOperation;
        this.transferOperation = transferOperation;
    }

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
