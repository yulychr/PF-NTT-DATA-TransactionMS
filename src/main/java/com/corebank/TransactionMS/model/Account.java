package com.corebank.TransactionMS.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document (collection = "account")
public class Account {
    @BsonId
    private String id;
    private String accountNumber;
    private double balance;
    private String typeAccount;
    private String customerId;


    public Account(String id, String accountNumber, double balance,String typeAccount, String customerId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.typeAccount= typeAccount;
        this.customerId = customerId;
    }
}
