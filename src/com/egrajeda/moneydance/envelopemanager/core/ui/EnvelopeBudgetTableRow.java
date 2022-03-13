package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.BudgetType;
import org.joda.money.Money;

public class EnvelopeBudgetTableRow {
  private final String name;
  private final Money income;
  private BudgetType type;
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

  public static EnvelopeBudgetTableRow empty() {
    return new EnvelopeBudgetTableRow("", null, null, null, null);
  }

  public static EnvelopeBudgetTableRow fromIncome(String name, Money amount) {
    return new EnvelopeBudgetTableRow(name, amount, null, null, null);
  }

  public static EnvelopeBudgetTableRow fromBudget(
      String name, BudgetType budgetType, float percentage, Money amount) {
    return new EnvelopeBudgetTableRow(name, null, budgetType, percentage, amount);
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

  public void setType(BudgetType type) {
    this.type = type;
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
