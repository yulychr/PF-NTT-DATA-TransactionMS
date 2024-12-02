package com.corebank.TransactionMS.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document (collection = "transaction")
public class Transaction {
    @BsonId
    private String id;
    private String type;
    private double amount;
    private LocalDateTime date;
    private String sourceAccount;
    private String destinationAccount;

    public Transaction(String id, String type, double amount, LocalDateTime date, String sourceAccount, String destinationAccount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }
}
