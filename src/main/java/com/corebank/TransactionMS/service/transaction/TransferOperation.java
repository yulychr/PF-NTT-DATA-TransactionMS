package com.corebank.TransactionMS.service.transaction;

import com.corebank.TransactionMS.dto.DepositRequestDTO;
import com.corebank.TransactionMS.dto.WithdrawalRequestDTO;
import com.corebank.TransactionMS.exception.AccountNotFoundException;
import com.corebank.TransactionMS.exception.InsufficientFundsException;
import com.corebank.TransactionMS.exception.InvalidTransferAmountException;
import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.AccountRepository;
import com.corebank.TransactionMS.service.impl.TransactionCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TransferOperation implements TransactionOperation{

    private final AccountRepository accountRepository;
    private final TransactionCreator transactionCreator;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String ACCOUNT_SERVICE_URL = "http://localhost:8087";

    @Override
    public Mono<Transaction> execute(String sourceAccount, String destinationAccount, double amount) {
        if (amount <= 0) {
            return Mono.error(new InvalidTransferAmountException("Invalid transfer amount. Amount must be positive."));
        }
        return webClientBuilder.build()
                .get()
                .uri(ACCOUNT_SERVICE_URL + "/accounts/byAccountNumber/{accountNumber}", sourceAccount)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> Mono.error(new AccountNotFoundException("Source account with number " + sourceAccount + " not found.")))
                .bodyToMono(Account.class)
                .flatMap(accountSource -> {
                    return webClientBuilder.build()
                            .get()
                            .uri(ACCOUNT_SERVICE_URL + "/accounts/byAccountNumber/{accountNumber}", destinationAccount)
                            .retrieve()
                            .onStatus(status -> status.value() == 404,
                                    clientResponse -> Mono.error(new AccountNotFoundException("Destination account with number " + destinationAccount + " not found.")))
                            .bodyToMono(Account.class)
                            .flatMap(accountDestination -> {
                                // Verificar si la cuenta de origen tiene saldo suficiente
                                if (accountSource.getBalance() >= amount) {
                                    // Actualizar saldo de la cuenta de origen
                                    return webClientBuilder.build()
                                            .post()
                                            .uri(ACCOUNT_SERVICE_URL + "/accounts/tWithdrawal")
                                            .bodyValue(new WithdrawalRequestDTO(sourceAccount, amount)) // Restar el monto
                                            .retrieve()
                                            .bodyToMono(Account.class)
                                            .flatMap(updatedSourceAccount -> {
                                                // Actualizar saldo de la cuenta de destino
                                                return webClientBuilder.build()
                                                        .post()
                                                        .uri(ACCOUNT_SERVICE_URL + "/accounts/tDeposit")
                                                        .bodyValue(new DepositRequestDTO(destinationAccount, amount)) // Sumar el monto
                                                        .retrieve()
                                                        .bodyToMono(Account.class)
                                                        .flatMap(updatedAccount -> {
                                                            return transactionCreator.createTransaction("transfer", amount, sourceAccount, destinationAccount)
                                                                    .map(transaction -> {

                                                                        return transaction;
                                                                    });
                                                        });
                                            });
                                } else {
                                    return Mono.error(new InsufficientFundsException("The source account does not have enough balance to complete the transfer."));
                                }
                            });
                });
    }
    @Override
    public Mono<Transaction> execute(String sourceAccount, double amount) {
        throw new UnsupportedOperationException("Transfer operation does not support withdrawal and deposit.");
    }

}
