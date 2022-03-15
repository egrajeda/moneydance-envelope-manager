package com.egrajeda.moneydance.envelopemanager.core.model;

import org.joda.money.Money;

import java.util.UUID;

public class EnvelopeBudget {
  private final String id;
  private final String name;
  private BudgetType type;
  private Float percentage;
  private Money budget;

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

  public void setType(BudgetType type) {
    this.type = type;
    if (type == BudgetType.LEFTOVER) {
      this.percentage = null;
      this.budget = null;
    }
  }

  public Float getPercentage() {
    return percentage;
  }

  public void setPercentage(Float percentage) {
    this.type = BudgetType.PERCENTAGE;
    this.percentage = percentage;
  }

  public Money getBudget() {
    return budget;
  }

  public void setBudget(Money budget) {
    this.type = BudgetType.AMOUNT;
    this.budget = budget;
  }
}
