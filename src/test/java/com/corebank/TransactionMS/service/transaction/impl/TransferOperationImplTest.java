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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferOperationImplTest {
    @Mock
    private AccountServiceT accountServiceT;

    @Mock
    private TransactionServiceT transactionServiceT;

    @InjectMocks
    private TransferOperationImpl transferOperation;

    private String sourceAccount = "123456";
    private String destinationAccount = "654321";
    private double validAmount = 500.0;
    private double invalidAmount = -100.0;
    private double insufficientAmount = 1000.0;

    private Account source;
    private Account destination;

    @BeforeEach
    void setUp() {
        source = new Account();
        source.setAccountNumber(sourceAccount);
        source.setBalance(500.0);

        destination = new Account();
        destination.setAccountNumber(destinationAccount);
        destination.setBalance(300.0);
    }

    @Test
    void testExecuteValidTransfer() {
        when(accountServiceT.getAccountByNumber(sourceAccount)).thenReturn(Mono.just(source));
        when(accountServiceT.getAccountByNumber(destinationAccount)).thenReturn(Mono.just(destination));
        when(accountServiceT.withdrawal(sourceAccount, validAmount)).thenReturn(Mono.just(source));
        when(accountServiceT.deposit(destinationAccount, validAmount)).thenReturn(Mono.just(destination));
        when(transactionServiceT.createTransaction("transfer", validAmount, sourceAccount, destinationAccount)).thenReturn(Mono.just(new Transaction()));
        Mono<Transaction> result = transferOperation.execute(sourceAccount, destinationAccount, validAmount);
        Transaction transaction = result.block();
        assertNotNull(transaction);
        verify(accountServiceT).getAccountByNumber(sourceAccount);
        verify(accountServiceT).getAccountByNumber(destinationAccount);
        verify(accountServiceT).withdrawal(sourceAccount, validAmount);
        verify(accountServiceT).deposit(destinationAccount, validAmount);
        verify(transactionServiceT).createTransaction("transfer", validAmount, sourceAccount, destinationAccount);
    }

    @Test
    void testExecuteInvalidTransferAmount() {
        Mono<Transaction> result = transferOperation.execute(sourceAccount, destinationAccount, invalidAmount);
        InvalidTransferAmountException exception = assertThrows(InvalidTransferAmountException.class, result::block);
        assertEquals("Invalid transfer amount. Amount must be greater than zero.", exception.getMessage());
        verify(accountServiceT, never()).getAccountByNumber(sourceAccount);
        verify(accountServiceT, never()).getAccountByNumber(destinationAccount);
        verify(accountServiceT, never()).withdrawal(sourceAccount, invalidAmount);
        verify(accountServiceT, never()).deposit(destinationAccount, invalidAmount);
        verify(transactionServiceT, never()).createTransaction(any(), anyDouble(), anyString(), anyString());
    }

    @Test
    void testExecuteInsufficientFunds() {

        when(accountServiceT.getAccountByNumber(sourceAccount)).thenReturn(Mono.just(source));
        when(accountServiceT.getAccountByNumber(destinationAccount)).thenReturn(Mono.just(destination));
        Mono<Transaction> result = transferOperation.execute(sourceAccount, destinationAccount, insufficientAmount);
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, result::block);
        assertEquals("The source account does not have enough balance to complete the transfer.", exception.getMessage());
        verify(accountServiceT).getAccountByNumber(sourceAccount);
        verify(accountServiceT).getAccountByNumber(destinationAccount);
        verify(accountServiceT, never()).withdrawal(sourceAccount, insufficientAmount);
        verify(accountServiceT, never()).deposit(destinationAccount, insufficientAmount);
        verify(transactionServiceT, never()).createTransaction(any(), anyDouble(), anyString(), anyString());
    }
}
