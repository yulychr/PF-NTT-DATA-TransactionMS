package com.corebank.TransactionMS.controller;

import com.corebank.TransactionMS.dto.DepositRequestDTO;
import com.corebank.TransactionMS.dto.TransferRequestDTO;
import com.corebank.TransactionMS.dto.WithdrawalRequestDTO;
import com.corebank.TransactionMS.model.Transaction;
import com.corebank.TransactionMS.service.TransactionService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions/")
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    //Endpoint para realizar un deposito
    @PostMapping("/deposit")
    public Mono<ResponseEntity<Transaction>> registerDeposit(@RequestBody DepositRequestDTO depositRequestDTO) {
        String accountNumber = depositRequestDTO.getAccountNumber();
        double amount = depositRequestDTO.getAmount();
        return transactionService.registerDeposit(accountNumber, amount)
                .map(transaction -> ResponseEntity.ok(transaction));
    }

    // Endpoint para realizar un retiro
    @PostMapping("/withdrawal")
    public Mono<ResponseEntity<Transaction>> registerWithdrawal(@RequestBody WithdrawalRequestDTO withdrawalRequestDTO) {
        String accountNumber = withdrawalRequestDTO.getAccountNumber();
        double amount = withdrawalRequestDTO.getAmount();
        return transactionService.registerWithdrawal(accountNumber, amount)
                .map(transaction -> ResponseEntity.ok(transaction));
    }

    // Endpoint para realizar una transferencia entre dos cuentas
    @PostMapping("/transfer")
    public Mono<ResponseEntity<Transaction>> registerTransfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        String sourceAccount = transferRequestDTO.getSourceAccount();
        String destinationAccount = transferRequestDTO.getDestinationAccount();
        double amount = transferRequestDTO.getAmount();
        return transactionService.registerTransfer(sourceAccount, destinationAccount, amount)
                .map(transaction -> ResponseEntity.ok(transaction));
    }

    //Endpoint para ver el historial de las transferencias
    @GetMapping("/history")
    public Flux<Transaction> viewTransactions(@RequestHeader Map<String, String> headers) {
        return transactionService.listTransactions();
    }

}
