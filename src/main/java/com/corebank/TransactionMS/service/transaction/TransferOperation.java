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
public class TransferOperation implements TransactionOperation{

    private final AccountRepository accountRepository;
    private final AccountUpdater accountUpdater;
    private final TransactionCreator transactionCreator;


    @Override
    public Mono<Transaction> execute(String sourceAccount, String destinationAccount, double amount) {
        if (amount <= 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid transfer amount. Amount must be positive."));
        }
        return accountRepository.findByAccountNumber(sourceAccount)
                .flatMap(accountSource -> accountRepository.findByAccountNumber(destinationAccount)
                        .flatMap(accountDestination -> {
                            if (accountSource.getBalance() >= amount) {
                                // Actualizar saldos de ambas cuentas
                                return accountUpdater.updateBalance(accountSource, amount, false)
                                        .flatMap(updatedSourceAccount -> accountUpdater.updateBalance(accountDestination, amount, true)
                                                .flatMap(updatedDestinationAccount -> transactionCreator.createTransaction("transfer", amount, updatedSourceAccount, updatedDestinationAccount)));
                            } else {
                                return Mono.error(new InsufficientFundsException("The source account does not have enough balance to complete the transfer."));
                            }
                        }))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Source or Destination account not found.")));
    }
    @Override
    public Mono<Transaction> execute(String sourceAccount, double amount) {
        throw new UnsupportedOperationException("Transfer operation does not support withdrawal and deposit.");
    }
}
