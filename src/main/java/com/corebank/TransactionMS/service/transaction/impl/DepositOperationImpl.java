package com.corebank.TransactionMS.service.transaction.impl;

import com.corebank.TransactionMS.exception.InvalidTransferAmountException;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.AccountServiceT;
import com.corebank.TransactionMS.service.TransactionServiceT;
import com.corebank.TransactionMS.service.transaction.operation.DepositOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class DepositOperationImpl implements DepositOperation {

    private AccountServiceT accountServiceT;
    private TransactionServiceT transactionServiceT;

    @Autowired
    public DepositOperationImpl(AccountServiceT accountServiceT, TransactionServiceT transactionServiceT){
        this.accountServiceT = accountServiceT;
        this.transactionServiceT = transactionServiceT;
    }


    @Override
    public Mono<Transaction> execute(String accountNumber, double amount) {
        if (amount < 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid deposit amount. Amount must be greater than zero."));
        }
        return accountServiceT.getAccountByNumber(accountNumber)
                .flatMap(account -> {
                    return accountServiceT.deposit(accountNumber, amount)
                            .flatMap(updatedAccount -> {
                                return transactionServiceT.createTransaction("deposit", amount, accountNumber, null)
                                        .map(transaction -> transaction);
                            });
                });
    }
}
