package com.corebank.TransactionMS.service.transaction.impl;

import com.corebank.TransactionMS.exception.InsufficientFundsException;
import com.corebank.TransactionMS.exception.InvalidTransferAmountException;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.AccountServiceT;
import com.corebank.TransactionMS.service.TransactionServiceT;
import com.corebank.TransactionMS.service.transaction.operation.TransferOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TransferOperationImpl implements TransferOperation {

    private final AccountServiceT accountServiceT;
    private final TransactionServiceT transactionServiceT;

    @Override
    public Mono<Transaction> execute(String sourceAccount, String destinationAccount, double amount) {
        if (amount < 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid transfer amount. Amount must be greater than zero"));
        }

        return accountServiceT.getAccountByNumber(sourceAccount)
                .flatMap(accountSource -> {
                    return accountServiceT.getAccountByNumber(destinationAccount)
                            .flatMap(accountDestination -> {
                                if (accountSource.getBalance() >= amount) {
                                    return accountServiceT.withdrawal(sourceAccount, amount)
                                            .flatMap(updatedSourceAccount -> {
                                                return accountServiceT.deposit(destinationAccount, amount)
                                                        .flatMap(updatedDestinationAccount -> {
                                                            return transactionServiceT.createTransaction("transfer", amount, sourceAccount, destinationAccount)
                                                                    .map(transaction -> transaction);
                                                        });
                                            });
                                } else {
                                    return Mono.error(new InsufficientFundsException("The source account does not have enough balance to complete the transfer."));
                                }
                            });
                });
    }
}
