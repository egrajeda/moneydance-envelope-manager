package com.egrajeda.moneydance.envelopemanager.core.model;

import org.joda.money.Money;

import java.util.UUID;

public class EnvelopeBudget {
  private final String id;
  private final String name;
  private final BudgetType type;
  private final Float percentage;
  private final Money budget;

  public EnvelopeBudget(String id, String name, BudgetType type, Float percentage, Money budget) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.percentage = percentage;
    this.budget = budget;
  }

  public EnvelopeBudget(String name, BudgetType type, Float percentage, Money budget) {
    this(UUID.randomUUID().toString(), name, type, percentage, budget);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BudgetType getType() {
    return type;
  }

  public Float getPercentage() {
    return percentage;
  }

  public Money getBudget() {
    return budget;
  }
}
