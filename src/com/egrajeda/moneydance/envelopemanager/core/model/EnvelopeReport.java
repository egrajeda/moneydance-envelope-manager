package com.egrajeda.moneydance.envelopemanager.core.model;

import org.joda.money.Money;

import java.util.UUID;

public class EnvelopeReport {
  private final String id;
  private final String name;
  private final Money initialBalance;
  private final Money balance;
  private final Money budget;
  private final Money adjustments;
  private final Money spend;

  public EnvelopeReport(
      String id,
      String name,
      Money initialBalance,
      Money balance,
      Money budget,
      Money adjustments,
      Money spend) {
    this.id = id;
    this.name = name;
    this.initialBalance = initialBalance;
    this.balance = balance;
    this.budget = budget;
    this.adjustments = adjustments;
    this.spend = spend;
  }

  public EnvelopeReport(
      String name,
      Money initialBalance,
      Money balance,
      Money budget,
      Money adjustments,
      Money spend) {
    this(UUID.randomUUID().toString(), name, initialBalance, balance, budget, adjustments, spend);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Money getInitialBalance() {
    return initialBalance;
  }

  public Money getBalance() {
    return balance;
  }

  public Money getBudget() {
    return budget;
  }

  public Money getAdjustments() {
    return adjustments;
  }

  public Money getSpend() {
    return spend;
  }
}
