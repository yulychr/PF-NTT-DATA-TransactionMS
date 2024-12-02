package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AccountUpdater {
    private final AccountRepository accountRepository;

    public Mono<Account> updateBalance(Account account, double amount, boolean isDeposit) {
        if (isDeposit) {
            account.setBalance(account.getBalance() + amount);
        } else {
            account.setBalance(account.getBalance() - amount);
        }
        return accountRepository.save(account);
    }
}
