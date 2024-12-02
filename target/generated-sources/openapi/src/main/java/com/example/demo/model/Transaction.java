package com.example.demo.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Transaction
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-02T02:37:50.172028800-05:00[America/Lima]")
public class Transaction   {
  @JsonProperty("id")
  private String id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("amount")
  private Double amount;

  @JsonProperty("date")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  @JsonProperty("soureAccount")
  private String soureAccount;

  @JsonProperty("destinationAccount")
  private String destinationAccount;

  public Transaction id(String id) {
    this.id = id;
    return this;
  }

  /**
   * The unique ID of the transaction.
   * @return id
  */
  @ApiModelProperty(value = "The unique ID of the transaction.")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Transaction type(String type) {
    this.type = type;
    return this;
  }

  /**
   * The type of transaction (e.g., deposit, withdrawal, transfer)
   * @return type
  */
  @ApiModelProperty(value = "The type of transaction (e.g., deposit, withdrawal, transfer)")


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Transaction amount(Double amount) {
    this.amount = amount;
    return this;
  }

  /**
   * The amount deposited.
   * @return amount
  */
  @ApiModelProperty(value = "The amount deposited.")


  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Transaction date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * The date and time when the transaction was made.
   * @return date
  */
  @ApiModelProperty(value = "The date and time when the transaction was made.")

  @Valid

  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public Transaction soureAccount(String soureAccount) {
    this.soureAccount = soureAccount;
    return this;
  }

  /**
   * The source account number associated with the transaction.
   * @return soureAccount
  */
  @ApiModelProperty(value = "The source account number associated with the transaction.")


  public String getSoureAccount() {
    return soureAccount;
  }

  public void setSoureAccount(String soureAccount) {
    this.soureAccount = soureAccount;
  }

  public Transaction destinationAccount(String destinationAccount) {
    this.destinationAccount = destinationAccount;
    return this;
  }

  /**
   * The destination account number involved in the transaction (e.g., for transfers).
   * @return destinationAccount
  */
  @ApiModelProperty(value = "The destination account number involved in the transaction (e.g., for transfers).")


  public String getDestinationAccount() {
    return destinationAccount;
  }

  public void setDestinationAccount(String destinationAccount) {
    this.destinationAccount = destinationAccount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction transaction = (Transaction) o;
    return Objects.equals(this.id, transaction.id) &&
        Objects.equals(this.type, transaction.type) &&
        Objects.equals(this.amount, transaction.amount) &&
        Objects.equals(this.date, transaction.date) &&
        Objects.equals(this.soureAccount, transaction.soureAccount) &&
        Objects.equals(this.destinationAccount, transaction.destinationAccount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, amount, date, soureAccount, destinationAccount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    soureAccount: ").append(toIndentedString(soureAccount)).append("\n");
    sb.append("    destinationAccount: ").append(toIndentedString(destinationAccount)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

