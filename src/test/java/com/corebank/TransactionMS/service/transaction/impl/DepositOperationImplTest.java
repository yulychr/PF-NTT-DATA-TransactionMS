package com.corebank.TransactionMS.service.transaction.impl;

import com.corebank.TransactionMS.exception.InvalidTransferAmountException;
import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.AccountServiceT;
import com.corebank.TransactionMS.service.TransactionServiceT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepositOperationImplTest {

    @Mock
    private AccountServiceT accountServiceT;

    @Mock
    private TransactionServiceT transactionServiceT;

    @InjectMocks
    private DepositOperationImpl depositOperation;

    private String accountNumber = "123456";
    private double validAmount = 1000.0;
    private double invalidAmount = -100.0;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(500.0);
    }

    @Test
    void testExecuteValidDeposit() {
        when(accountServiceT.getAccountByNumber(accountNumber)).thenReturn(Mono.just(account));
        when(accountServiceT.deposit(accountNumber, validAmount)).thenReturn(Mono.just(account));
        when(transactionServiceT.createTransaction("deposit", validAmount, accountNumber, null)).thenReturn(Mono.just(new Transaction()));
        Mono<Transaction> result = depositOperation.execute(accountNumber, validAmount);
        Transaction transaction = result.block();
        assertNotNull(transaction);
        verify(accountServiceT).getAccountByNumber(accountNumber);
        verify(accountServiceT).deposit(accountNumber, validAmount);
        verify(transactionServiceT).createTransaction("deposit", validAmount, accountNumber, null);
    }

    @Test
    void testExecuteInvalidDepositAmount() {
        Mono<Transaction> result = depositOperation.execute(accountNumber, invalidAmount);
        InvalidTransferAmountException exception = assertThrows(InvalidTransferAmountException.class, result::block);
        assertEquals("Invalid deposit amount. Amount must be greater than zero.", exception.getMessage());
        verify(accountServiceT, never()).getAccountByNumber(accountNumber);
        verify(accountServiceT, never()).deposit(accountNumber, invalidAmount);
        verify(transactionServiceT, never()).createTransaction(any(), anyDouble(), anyString(), any());
    }
}
