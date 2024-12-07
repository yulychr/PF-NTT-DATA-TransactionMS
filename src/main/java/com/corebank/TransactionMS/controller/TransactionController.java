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
        return transactionService.registerDeposit(depositRequestDTO.getAccountNumber(), depositRequestDTO.getAmount())
                .map(transaction -> ResponseEntity.ok(transaction));
    }

    // Endpoint para realizar un retiro
    @PostMapping("/withdrawal")
    public Mono<ResponseEntity<Transaction>> registerWithdrawal(@RequestBody WithdrawalRequestDTO withdrawalRequestDTO) {
        return transactionService.registerWithdrawal(withdrawalRequestDTO.getAccountNumber(), withdrawalRequestDTO.getAmount())
                .map(transaction -> ResponseEntity.ok(transaction));
    }

    // Endpoint para realizar una transferencia entre dos cuentas
    @PostMapping("/transfer")
    public Mono<ResponseEntity<Transaction>> registerTransfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        return transactionService.registerTransfer(
                transferRequestDTO.getSourceAccount(),
                transferRequestDTO.getDestinationAccount(),
                transferRequestDTO.getAmount())
                .map(transaction -> ResponseEntity.ok(transaction));
    }

    //Endpoint para ver el historial de las transferencias
    @GetMapping("/history")
    public Flux<Transaction> viewTransactions(@RequestHeader Map<String, String> headers) {
        return transactionService.listTransactions();
    }

}
