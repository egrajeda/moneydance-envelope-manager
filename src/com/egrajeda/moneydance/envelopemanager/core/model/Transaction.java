package com.egrajeda.moneydance.envelopemanager.core.model;

import org.joda.money.Money;

import java.util.Date;

public class Transaction {
  private String id;
  private Date date;
  private String checkNumber;
  private String description;
  private String memo;
  private String category;
  private ClearedStatus clearedStatus;
  private Money payment;
  private Envelope envelope;

  public Transaction(
      String id,
      Date date,
      String checkNumber,
      String description,
      String memo,
      String category,
      ClearedStatus clearedStatus,
      Money payment) {
    this.id = id;
    this.date = date;
    this.checkNumber = checkNumber;
    this.description = description;
    this.memo = memo;
    this.category = category;
    this.clearedStatus = clearedStatus;
    this.payment = payment;
  }

  // TODO: Clean this up
  public Transaction(
      String id,
      Date date,
      String checkNumber,
      String description,
      String memo,
      String category,
      ClearedStatus clearedStatus,
      Money payment,
      Envelope envelope) {
    this(id, date, checkNumber, description, memo, category, clearedStatus, payment);
    this.envelope = envelope;
  }

  public String getId() {
    return id;
  }

  public Date getDate() {
    return date;
  }

  public String getCheckNumber() {
    return checkNumber;
  }

  public String getDescription() {
    return description;
  }

  public String getMemo() {
    return memo;
  }

  public String getCategory() {
    return category;
  }

  public ClearedStatus getClearedStatus() {
    return clearedStatus;
  }

  public void setClearedStatus(ClearedStatus clearedStatus) {
    this.clearedStatus = clearedStatus;
  }

  public Money getPayment() {
    return payment;
  }

  public Envelope getEnvelope() {
    return envelope;
  }

  public void setEnvelope(Envelope envelope) {
    this.envelope = envelope;
  }
}
