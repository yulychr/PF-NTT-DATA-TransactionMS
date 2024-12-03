package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.transaction.impl.DepositOperationImpl;
import com.corebank.TransactionMS.service.transaction.impl.TransferOperationImpl;
import com.corebank.TransactionMS.service.transaction.impl.WithdrawalOperationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionHandlerTest {
    @Mock
    private DepositOperationImpl depositOperation;

    @Mock
    private WithdrawalOperationImpl withdrawalOperation;

    @Mock
    private TransferOperationImpl transferOperation;

    @InjectMocks
    private TransactionHandler transactionHandler;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void handleDeposit_shouldReturnTransaction_whenValidDeposit() {
        transaction = new Transaction();
        transaction.setAmount(1000.0);
        transaction.setSourceAccount("12345");
        transaction.setType("deposit");

        when(depositOperation.execute("12345", 1000.0)).thenReturn(Mono.just(transaction));
        Mono<Transaction> result = transactionHandler.handleDeposit("12345", 1000.0);
        assertNotNull(result);
        assertEquals(1000.0, result.block().getAmount());
        assertEquals("12345", result.block().getSourceAccount());
    }

    @Test
    void handleWithdrawal_shouldReturnTransaction_whenValidWithdrawal() {

        transaction = new Transaction();
        transaction.setAmount(1000.0);
        transaction.setSourceAccount("12345");
        transaction.setType("withdrawal");

        when(withdrawalOperation.execute("12345", 500.0)).thenReturn(Mono.just(transaction));
        Mono<Transaction> result = transactionHandler.handleWithdrawal("12345", 500.0);
        assertNotNull(result);
        assertEquals(1000.0, result.block().getAmount());
        assertEquals("12345", result.block().getSourceAccount());
    }

    @Test
    void handleTransfer_shouldReturnTransaction_whenValidTransfer() {

        transaction = new Transaction();
        transaction.setAmount(1000.0);
        transaction.setSourceAccount("12345");
        transaction.setDestinationAccount("67890");
        transaction.setType("transfer");

        when(transferOperation.execute("12345", "67890", 1000.0)).thenReturn(Mono.just(transaction));
        Mono<Transaction> result = transactionHandler.handleTransfer("12345", "67890", 1000.0);
        assertNotNull(result);
        assertEquals(1000.0, result.block().getAmount());
        assertEquals("12345", result.block().getSourceAccount());
        assertEquals("67890", result.block().getDestinationAccount());
    }

}
