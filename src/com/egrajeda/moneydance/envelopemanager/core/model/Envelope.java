package com.egrajeda.moneydance.envelopemanager.core.model;

import org.joda.money.Money;

import java.util.Objects;
import java.util.UUID;

public class Envelope {
  private final String id;
  private final String name;
  private final Money balance;

  public Envelope(String id, String name, Money balance) {
    this.id = id;
    this.name = name;
    this.balance = balance;
  }

  public Envelope(String name, Money balance) {
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
    return name + " (" + balance + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Envelope envelope = (Envelope) o;
    return id.equals(envelope.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
