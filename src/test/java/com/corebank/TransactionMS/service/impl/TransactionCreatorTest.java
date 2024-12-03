package com.corebank.TransactionMS.service.impl;

import com.corebank.TransactionMS.model.Account;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TransactionCreatorTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionCreator transactionCreator;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransactionTestSuccessful() {

        transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setType("transfer");
        transaction.setAmount(1000.0);
        transaction.setSourceAccount("12345");
        transaction.setDestinationAccount("67890");

        when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));

        Transaction result = transactionCreator.createTransaction("transfer", 1000.0, "12345", "67890").block();

        assertNotNull(result);
        assertEquals(1000.0, result.getAmount());
        assertEquals("12345", result.getSourceAccount());
        assertEquals("67890", result.getDestinationAccount());
    }

    @Test
    void createTransactionWithoutDestinationAccount() {

        transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setType("deposit");
        transaction.setAmount(1000.0);
        transaction.setSourceAccount("12345");
        transaction.setDestinationAccount(null);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));

        Transaction result = transactionCreator.createTransaction("transfer", 1000.0, "12345", null).block();

        assertNotNull(result);
        assertEquals(1000.0, result.getAmount());
        assertEquals("12345", result.getSourceAccount());
        assertNull(result.getDestinationAccount());
    }
}
