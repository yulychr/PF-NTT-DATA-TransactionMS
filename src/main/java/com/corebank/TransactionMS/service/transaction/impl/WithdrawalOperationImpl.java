package com.corebank.TransactionMS.service.transaction.impl;

import com.corebank.TransactionMS.exception.InsufficientFundsException;
import com.corebank.TransactionMS.exception.InvalidTransferAmountException;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.AccountServiceT;
import com.corebank.TransactionMS.service.TransactionServiceT;
import com.corebank.TransactionMS.service.transaction.operation.WithdrawalOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class WithdrawalOperationImpl implements WithdrawalOperation {

    private final AccountServiceT accountServiceT;
    private final TransactionServiceT transactionServiceT;

    @Override
    public Mono<Transaction> execute(String accountNumber, double amount) {
        if (amount <= 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid withdrawal amount. Amount must be positive"));
        }
        return accountServiceT.getAccountByNumber(accountNumber)
                .flatMap(account -> {
                    if (account.getBalance() >= amount) {
                        return accountServiceT.withdrawal(accountNumber, amount)
                                .flatMap(updatedAccount -> {
                                    return transactionServiceT.createTransaction("withdrawal", amount, accountNumber, null)
                                            .map(transaction -> transaction);
                                });
                    } else {
                        return Mono.error(new InsufficientFundsException("The source account does not have enough balance to complete the transfer."));
                    }
                });
    }
}
