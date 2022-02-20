package com.egrajeda.moneydance.envelopemanager.core.model;

import org.joda.money.Money;

import java.util.UUID;

public class Account {
  private final String id;
  private final String name;
  private final Money balance;

  public Account(String id, String name, Money balance) {
    this.id = id;
    this.name = name;
    this.balance = balance;
  }

  public Account(String name, Money balance) {
    this(UUID.randomUUID().toString(), name, balance);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Money getBalance() {
    return balance;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
