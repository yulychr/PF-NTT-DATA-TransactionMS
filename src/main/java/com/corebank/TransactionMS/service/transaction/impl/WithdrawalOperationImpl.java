package com.corebank.TransactionMS.service.transaction.impl;

import com.corebank.TransactionMS.dto.WithdrawalRequestDTO;
import com.corebank.TransactionMS.exception.AccountNotFoundException;
import com.corebank.TransactionMS.exception.InvalidTransferAmountException;
import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.impl.TransactionCreator;
import com.corebank.TransactionMS.service.transaction.WithdrawalOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class WithdrawalOperationImpl implements WithdrawalOperation {

    private final TransactionCreator transactionCreator;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String ACCOUNT_SERVICE_URL = "http://localhost:8087";

    @Override
    public Mono<Transaction> execute(String accountNumber, double amount) {
        if (amount <= 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid withdrawal amount. Amount must be positive"));
        }
        return webClientBuilder.build()
                .get()
                .uri(ACCOUNT_SERVICE_URL + "/accounts/byAccountNumber/{accountNumber}", accountNumber)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> Mono.error(new AccountNotFoundException("Account with number " + accountNumber + " not found.")))
                .bodyToMono(Account.class)
                .flatMap(account -> {
                    return webClientBuilder.build()
                            .post()
                            .uri(ACCOUNT_SERVICE_URL + "/accounts/tWithdrawal")
                            .bodyValue(new WithdrawalRequestDTO(accountNumber, amount))
                            .retrieve()
                            .bodyToMono(Account.class)
                            .flatMap(updatedAccount -> {
                                return transactionCreator.createTransaction("withdrawal", amount, accountNumber, null)
                                        .map(transaction -> {
                                            return transaction;
                                        });
                            });
                });
    }
}
