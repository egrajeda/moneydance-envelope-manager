package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.BudgetType;
import org.joda.money.Money;

public class EnvelopeBudgetTableRow {
  private final String name;
  private final Money income;
  private final BudgetType type;
  private Float percentage;
  private Money budget;

  public EnvelopeBudgetTableRow(
      String name, Money income, BudgetType type, Float percentage, Money budget) {
    this.name = name;
    this.income = income;
    this.type = type;
    this.percentage = percentage;
    this.budget = budget;
  }

  public String getName() {
    return name;
  }

  public Money getIncome() {
    return income;
  }

  public BudgetType getType() {
    return type;
  }

  public Float getPercentage() {
    return percentage;
  }

  public void setPercentage(Float percentage) {
    this.percentage = percentage;
  }

  public Money getBudget() {
    return budget;
  }

  public void setBudget(Money budget) {
    this.budget = budget;
  }
}
