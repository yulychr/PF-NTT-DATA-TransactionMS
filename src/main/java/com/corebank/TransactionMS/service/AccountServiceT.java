package com.corebank.TransactionMS.service;

import com.corebank.TransactionMS.dto.DepositRequestDTO;
import com.corebank.TransactionMS.dto.WithdrawalRequestDTO;
import com.corebank.TransactionMS.exception.AccountNotFoundException;
import com.corebank.TransactionMS.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceT {

    private final WebClient.Builder webClientBuilder;
    private final String ACCOUNT_SERVICE_URL = "http://localhost:8087";

    public AccountServiceT(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Account> getAccountByNumber(String accountNumber) {
        return webClientBuilder.build()
                .get()
                .uri(ACCOUNT_SERVICE_URL + "/accounts/byAccountNumber/{accountNumber}", accountNumber)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> Mono.error(new AccountNotFoundException("Account with number " + accountNumber + " not found.")))
                .bodyToMono(Account.class);
    }


    public Mono<Account> withdrawal(String accountNumber, double amount) {
        return webClientBuilder.build()
                .post()
                .uri(ACCOUNT_SERVICE_URL + "/accounts/tWithdrawal")
                .bodyValue(new WithdrawalRequestDTO(accountNumber, amount))
                .retrieve()
                .bodyToMono(Account.class);
    }

    public Mono<Account> deposit(String accountNumber, double amount) {
        return webClientBuilder.build()
                .post()
                .uri(ACCOUNT_SERVICE_URL + "/accounts/tDeposit")
                .bodyValue(new DepositRequestDTO(accountNumber, amount))
                .retrieve()
                .bodyToMono(Account.class);
    }
}
