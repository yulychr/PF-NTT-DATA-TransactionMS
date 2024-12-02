package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.exception.AccountNotFoundException;
import com.corebank.TransactionMS.exception.InsufficientFundsException;
import com.corebank.TransactionMS.exception.InvalidTransferAmountException;
import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.AccountRepository;
import com.corebank.TransactionMS.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {

    private final AccountRepository accountRepository;
    private final AccountUpdater accountUpdater;
    private final TransactionCreator transactionCreator;

    public TransactionHandler(AccountRepository accountRepository, AccountUpdater accountUpdater, TransactionCreator transactionCreator) {
        this.accountRepository = accountRepository;
        this.accountUpdater = accountUpdater;
        this.transactionCreator = transactionCreator;
    }

    public Mono<Transaction> handleDeposit(String accountNumber, double amount) {
        if (amount <= 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid deposit amount. Amount must be positive."));
        }

        return accountRepository.findByAccountNumber(accountNumber)
                .flatMap(account -> accountUpdater.updateBalance(account, amount, true))
                .flatMap(updatedAccount -> transactionCreator.createTransaction("deposit", amount, updatedAccount, null))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account with number " + accountNumber + " not found.")));
    }

    public Mono<Transaction> handleWithdrawal(String accountNumber, double amount) {
        if (amount <= 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid withdrawal amount. Amount must be positive"));
        }

        return accountRepository.findByAccountNumber(accountNumber)
                .flatMap(account -> {
                    if (account.getBalance() >= amount) {
                        return accountUpdater.updateBalance(account, amount, false);
                    } else {
                        return Mono.error(new InsufficientFundsException("Withdrawal amount exceeds available balance."));
                    }
                })
                .flatMap(updatedAccount -> transactionCreator.createTransaction("withdrawal", amount, updatedAccount, null))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account with number " + accountNumber + " not found.")));
    }

    public Mono<Transaction> handleTransfer(String sourceAccount, String destinationAccount, double amount) {
        if (amount <= 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid transfer amount. Amount must be positive."));
        }

        return accountRepository.findByAccountNumber(sourceAccount)
                .flatMap(accountSource -> accountRepository.findByAccountNumber(destinationAccount)
                        .flatMap(accountDestination -> {
                            if (accountSource.getBalance() >= amount) {
                                // Actualizar los saldos de ambas cuentas
                                return accountUpdater.updateBalance(accountSource, amount, false)
                                        .flatMap(updatedSourceAccount -> accountUpdater.updateBalance(accountDestination, amount, true)
                                                .flatMap(updatedDestinationAccount ->
                                                        // Crear la transacción de transferencia
                                                        transactionCreator.createTransaction("transfer", amount, updatedSourceAccount, updatedDestinationAccount)));
                            } else {
                                return Mono.error(new InsufficientFundsException("The source account does not have enough balance to complete the transfer."));
                            }
                        }))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Source or Destination account not found.")));
    }
}
