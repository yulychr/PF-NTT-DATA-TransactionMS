package com.corebank.TransactionMS.service;

import com.corebank.TransactionMS.dto.DepositRequestDTO;
import com.corebank.TransactionMS.dto.WithdrawalRequestDTO;
import com.corebank.TransactionMS.exception.AccountNotFoundException;
import com.corebank.TransactionMS.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTTest {
    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private AccountServiceT accountServiceT;

    private String accountNumber = "123456";
    private double amount = 100.0;
    private Account mockAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAccount = new Account();
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.setBalance(500.0);
    }

    @Test
    void testWithdrawal() {
        doReturn(requestBodyUriSpec).when(webClient).post();
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(any(String.class));
        doReturn(requestBodySpec).when(requestBodySpec).bodyValue(any(WithdrawalRequestDTO.class));
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(Mono.just(mockAccount)).when(responseSpec).bodyToMono(Account.class);
        Mono<Account> result = accountServiceT.withdrawal(accountNumber, amount);
        Account account = result.block();
        assertNotNull(account);
        assertEquals(500.0, account.getBalance());  // Balance after withdrawal should still be 500 (mock data).
    }

    @Test
    void testDeposit() {
        doReturn(requestBodyUriSpec).when(webClient).post();
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(any(String.class));
        doReturn(requestBodySpec).when(requestBodySpec).bodyValue(any(DepositRequestDTO.class));
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(Mono.just(mockAccount)).when(responseSpec).bodyToMono(Account.class);
        Mono<Account> result = accountServiceT.deposit(accountNumber, amount);
        Account account = result.block();
        assertNotNull(account);
        assertEquals(500.0, account.getBalance());  // Balance after deposit should still be 500 (mock data).
    }

}

