package com.corebank.TransactionMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawalRequestDTO {
    private String accountNumber;
    private double amount;

    public WithdrawalRequestDTO(String accountNumber, double amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }
}
