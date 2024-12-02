package com.corebank.TransactionMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDTO {
    String sourceAccount;
    String destinationAccount;
    double amount;

    public TransferRequestDTO(String sourceAccount, String destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }
}
