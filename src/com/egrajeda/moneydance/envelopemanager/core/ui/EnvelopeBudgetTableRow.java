package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.BudgetType;
import com.egrajeda.moneydance.envelopemanager.core.model.EnvelopeBudget;
import org.joda.money.Money;

public class EnvelopeBudgetTableRow {
  private final EnvelopeBudget envelopeBudget;
  private final String name;
  private final Money income;
  private final BudgetType type;
  private final Float percentage;
  private final Money budget;

  private EnvelopeBudgetTableRow(EnvelopeBudget envelopeBudget, Float percentage, Money budget) {
    this.envelopeBudget = envelopeBudget;
    this.name = envelopeBudget.getName();
    this.type = envelopeBudget.getType();
    this.percentage = percentage;
    this.budget = budget;

    this.income = null;
  }

  private EnvelopeBudgetTableRow(String name, Money income) {
    this.name = name;
    this.income = income;

    this.envelopeBudget = null;
    this.type = null;
    this.percentage = null;
    this.budget = null;
  }

  public static EnvelopeBudgetTableRow empty() {
    return new EnvelopeBudgetTableRow(null, null);
  }

  public static EnvelopeBudgetTableRow fromIncome(String name, Money amount) {
    return new EnvelopeBudgetTableRow(name, amount);
  }

  public static EnvelopeBudgetTableRow fromEnvelopeBudget(
      EnvelopeBudget envelopeBudget, float percentage, Money amount) {
    return new EnvelopeBudgetTableRow(envelopeBudget, percentage, amount);
  }

  public EnvelopeBudget getEnvelopeBudget() {
    return envelopeBudget;
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

  public Money getBudget() {
    return budget;
  }
}
