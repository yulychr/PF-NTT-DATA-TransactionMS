package com.corebank.TransactionMS.service.transaction;

import com.corebank.TransactionMS.exception.AccountNotFoundException;
import com.corebank.TransactionMS.exception.InvalidTransferAmountException;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.AccountRepository;
import com.corebank.TransactionMS.service.impl.AccountUpdater;
import com.corebank.TransactionMS.service.impl.TransactionCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class DepositOperation implements TransactionOperation{
    private final AccountRepository accountRepository;
    private final AccountUpdater accountUpdater;
    private final TransactionCreator transactionCreator;

    @Override
    public Mono<Transaction> execute(String accountNumber, double amount) {
        if (amount <= 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid deposit amount. Amount must be positive."));
        }

        return accountRepository.findByAccountNumber(accountNumber)
                .flatMap(account -> accountUpdater.updateBalance(account, amount, true))
                .flatMap(updatedAccount -> transactionCreator.createTransaction("deposit", amount, updatedAccount, null))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account with number " + accountNumber + " not found.")));
    }
    @Override
    public Mono<Transaction> execute(String sourceAccount, String destinationAccount, double amount) {
        throw new UnsupportedOperationException("Deposit operation does not support transfers.");
    }
}
