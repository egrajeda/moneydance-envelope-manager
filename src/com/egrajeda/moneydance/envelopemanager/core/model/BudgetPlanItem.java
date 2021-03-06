package com.egrajeda.moneydance.envelopemanager.core.model;

import com.egrajeda.moneydance.envelopemanager.core.ui.MoneyUtils;
import org.joda.money.Money;

import java.math.RoundingMode;

public class BudgetPlanItem {
  private final Money amount;
  private final float percentage;
  private final EnvelopeBudget envelopeBudget;

  private BudgetPlanItem(Money amount, float percentage, EnvelopeBudget envelopeBudget) {
    this.amount = amount;
    this.percentage = percentage;
    this.envelopeBudget = envelopeBudget;
  }

  public static BudgetPlanItem fromAmount(
      Money amount, Money total, EnvelopeBudget envelopeBudget) {
    return new BudgetPlanItem(
        amount, total.isZero() ? 0 : MoneyUtils.divide(amount, total), envelopeBudget);
  }

  public static BudgetPlanItem fromPercentage(
      float percentage, Money total, EnvelopeBudget envelopeBudget) {
    return new BudgetPlanItem(
        total.multipliedBy(percentage, RoundingMode.HALF_EVEN), percentage, envelopeBudget);
  }

  public Money getAmount() {
    return amount;
  }

  public float getPercentage() {
    return percentage;
  }

  public EnvelopeBudget getEnvelopeBudget() {
    return envelopeBudget;
  }
}
