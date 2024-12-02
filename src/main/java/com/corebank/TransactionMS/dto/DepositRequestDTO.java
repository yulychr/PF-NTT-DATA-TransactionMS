package com.corebank.TransactionMS.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DepositRequestDTO {
    private String accountNumber;
    private double amount;

    public DepositRequestDTO(String accountNumber, double amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }
}
