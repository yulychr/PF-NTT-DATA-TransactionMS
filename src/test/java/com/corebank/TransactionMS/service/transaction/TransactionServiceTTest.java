package com.corebank.TransactionMS.service.transaction;

import ch.qos.logback.core.joran.util.beans.BeanDescriptionFactory;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.TransactionServiceT;
import com.corebank.TransactionMS.service.impl.TransactionCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTTest {

    @Mock
    private TransactionCreator transactionCreator;

    @InjectMocks
    private TransactionServiceT transactionServiceT;


    @Test
    void testCreateTransaction() {
        Transaction mockTransaction = new Transaction("1T","transfer", 1000.0, LocalDateTime.now(), "123456", "654321");
        when(transactionCreator.createTransaction("transfer", 1000.0, "123456", "654321"))
                .thenReturn(Mono.just(mockTransaction));
        CompletableFuture<Transaction> future = transactionServiceT.createTransaction("transfer", 1000.0, "123456", "654321")
                .toFuture();
        Transaction transaction = future.join();
        assertEquals(mockTransaction, transaction);
    }

}
