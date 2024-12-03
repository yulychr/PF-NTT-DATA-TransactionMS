package com.corebank.TransactionMS.controller;

import com.corebank.TransactionMS.dto.DepositRequestDTO;
import com.corebank.TransactionMS.dto.TransferRequestDTO;
import com.corebank.TransactionMS.dto.WithdrawalRequestDTO;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.repository.AccountRepository;
import com.corebank.TransactionMS.repository.TransactionRepository;
import com.corebank.TransactionMS.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringJUnitConfig(Transaction.class)
public class TransactionControllerTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    TransactionService transactionService;

    WebTestClient client;

    Transaction[] transactionsArray;
    Transaction transaction;

    @BeforeEach
    void setUp(ApplicationContext context) {
        MockitoAnnotations.openMocks(this);
        client = WebTestClient.bindToController(new TransactionController(transactionService))
                .build();
        transactionsArray = new Transaction[] {
                new Transaction("1T", "deposit",100.00,  LocalDateTime.now(), "1A", "null"),
                new Transaction("2T", "withdrawal",100.00,  LocalDateTime.now(), "2A", "null"),
                new Transaction("3T", "transfer",100.00,  LocalDateTime.now(), "3A", "4A"),
        };

    }
    @Test
    public void testGetAllTransactions() {
        given(transactionService.listTransactions()).willReturn(Flux.just(transactionsArray));
        client.get().uri("/transactions/history")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Transaction.class)
                .hasSize(3);
    }

    @Test
    public void testPostDeposit() {
        double amount = 100.00;
        String accountNumber = "1C";
        DepositRequestDTO depositRequestDTO = new DepositRequestDTO(accountNumber, amount);
        Transaction transaction = new Transaction("1T", "deposit", amount, LocalDateTime.now(), accountNumber, null);
        given(transactionService.registerDeposit(accountNumber, amount)).willReturn(Mono.just(transaction));

        client.post().uri("/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new DepositRequestDTO(accountNumber, amount))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class)
                .value(transactionResponse -> {
                    assertNotNull(transactionResponse);
                    assertEquals(accountNumber, transactionResponse.getSourceAccount());
                    assertEquals(amount, transactionResponse.getAmount(), 0.01);
                    assertNull(transactionResponse.getDestinationAccount());
                });
    }

    @Test
    public void testPostWithdrawal() {
        double amount = 100.00;
        String accountNumber = "1C";
        DepositRequestDTO depositRequestDTO = new DepositRequestDTO(accountNumber, amount);
        Transaction transaction = new Transaction("1T", "withdrawal", amount, LocalDateTime.now(), accountNumber, null);
        given(transactionService.registerWithdrawal(accountNumber, amount)).willReturn(Mono.just(transaction));

        client.post().uri("/transactions/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new WithdrawalRequestDTO(accountNumber, amount)) // Asegúrate de enviar un objeto DepositRequestDTO
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class)
                .value(transactionResponse -> {
                    assertNotNull(transactionResponse);
                    assertEquals(accountNumber, transactionResponse.getSourceAccount());
                    assertEquals(amount, transactionResponse.getAmount(), 0.01);
                    assertNull(transactionResponse.getDestinationAccount());
                });
    }
    @Test
    public void testPostTransfer() {
        double amount = 100.00;
        String accountSource = "1C";
        String destinationAccount = "2C";
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO(accountSource,destinationAccount, amount);
        Transaction transaction = new Transaction("1T", "transfer", amount, LocalDateTime.now(), accountSource, destinationAccount);
        given(transactionService.registerTransfer(accountSource,destinationAccount, amount)).willReturn(Mono.just(transaction));

        client.post().uri("/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new TransferRequestDTO(accountSource,destinationAccount, amount)) // Asegúrate de enviar un objeto DepositRequestDTO
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class)
                .value(transactionResponse -> {
                    assertNotNull(transactionResponse);
                    assertEquals(accountSource, transactionResponse.getSourceAccount());
                    assertEquals(destinationAccount, transactionResponse.getDestinationAccount());
                    assertEquals(amount, transactionResponse.getAmount(), 0.01);
                });
    }

}
