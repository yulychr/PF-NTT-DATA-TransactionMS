package com.corebank.TransactionMS.service.transaction;

import com.corebank.TransactionMS.exception.AccountNotFoundException;
import com.corebank.TransactionMS.exception.InsufficientFundsException;
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
public class WithdrawalOperation implements TransactionOperation{

    private final AccountRepository accountRepository;
    private final AccountUpdater accountUpdater;
    private final TransactionCreator transactionCreator;


    @Override
    public Mono<Transaction> execute(String accountNumber, double amount) {
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
    @Override
    public Mono<Transaction> execute(String sourceAccount, String destinationAccount, double amount) {
        throw new UnsupportedOperationException("Withdrawal operation does not support transfers.");
    }
}
