package com.corebank.TransactionMS.service.transaction.impl;

import com.corebank.TransactionMS.exception.InsufficientFundsException;
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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawalOperationImplTest {

    @Mock
    private AccountServiceT accountServiceT;

    @Mock
    private TransactionServiceT transactionServiceT;

    @InjectMocks
    private WithdrawalOperationImpl withdrawalOperation;

    private String accountNumber = "123456";
    private double validAmount = 500.0;
    private double invalidAmount = -100.0;
    private double insufficientAmount = 1000.0;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(500.0);
    }

    @Test
    void testExecuteValidWithdrawal() {
        // Arrange
        when(accountServiceT.getAccountByNumber(accountNumber)).thenReturn(Mono.just(account));
        when(accountServiceT.withdrawal(accountNumber, validAmount)).thenReturn(Mono.just(account));
        when(transactionServiceT.createTransaction("withdrawal", validAmount, accountNumber, null)).thenReturn(Mono.just(new Transaction()));
        Mono<Transaction> result = withdrawalOperation.execute(accountNumber, validAmount);
        Transaction transaction = result.block();
        assertNotNull(transaction);
        verify(accountServiceT).getAccountByNumber(accountNumber);
        verify(accountServiceT).withdrawal(accountNumber, validAmount);
        verify(transactionServiceT).createTransaction("withdrawal", validAmount, accountNumber, null);
    }

    @Test
    void testExecuteInvalidWithdrawalAmount() {
        Mono<Transaction> result = withdrawalOperation.execute(accountNumber, invalidAmount);
        InvalidTransferAmountException exception = assertThrows(InvalidTransferAmountException.class, result::block);
        assertEquals("Invalid withdrawal amount. Amount must be greater than zero.", exception.getMessage());
        verify(accountServiceT, never()).getAccountByNumber(accountNumber);
        verify(accountServiceT, never()).withdrawal(accountNumber, invalidAmount);
        verify(transactionServiceT, never()).createTransaction(any(), anyDouble(), anyString(), any());
    }

    @Test
    void testExecuteInsufficientFunds() {
        when(accountServiceT.getAccountByNumber(accountNumber)).thenReturn(Mono.just(account));
        Mono<Transaction> result = withdrawalOperation.execute(accountNumber, insufficientAmount);
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, result::block);
        assertEquals("The source account does not have enough balance to complete the transfer.", exception.getMessage());
        verify(accountServiceT).getAccountByNumber(accountNumber);
        verify(accountServiceT, never()).withdrawal(anyString(), anyDouble());
        verify(transactionServiceT, never()).createTransaction(anyString(), anyDouble(), anyString(), any());
    }
}
