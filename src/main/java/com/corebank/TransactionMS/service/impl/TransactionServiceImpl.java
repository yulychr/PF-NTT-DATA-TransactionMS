package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.TransactionRepository;
import com.corebank.TransactionMS.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionHandler transactionHandler;

    @Override
    public Mono<Transaction> registerDeposit(String accountNumber, double amount) {
        return transactionHandler.handleDeposit(accountNumber, amount);
    }

    @Override
    public Mono<Transaction> registerWithdrawal(String accountNumber, double amount) {
        return transactionHandler.handleWithdrawal(accountNumber, amount);
    }

        @Override
        public Mono<Transaction> registerTransfer(String sourceAccount, String destinationAccount, double amount) {
            return transactionHandler.handleTransfer(sourceAccount, destinationAccount, amount);
        }

    @Override
    public Flux<Transaction> listTransactions() {
        return transactionRepository.findAll();
    }

}
