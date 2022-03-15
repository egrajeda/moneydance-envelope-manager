package com.egrajeda.moneydance.envelopemanager.core.model;

import org.joda.money.Money;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BudgetPlan {
  private final Money amount;
  private final Money leftover;
  private final List<BudgetPlanItem> itemList;

  private BudgetPlan(Money amount, Money leftover, List<BudgetPlanItem> itemList) {
    this.amount = amount;
    this.leftover = leftover;
    this.itemList = itemList;
  }

  public static BudgetPlan calculate(Money amount, List<EnvelopeBudget> envelopeBudgetList) {
    List<BudgetPlanItem> itemList = new ArrayList<>();

    Money leftover = Money.of(amount);
    List<EnvelopeBudget> leftoverEnvelopeBudgetList = new ArrayList<>();
    for (EnvelopeBudget envelopeBudget : envelopeBudgetList) {
      BudgetPlanItem item = null;
      if (envelopeBudget.getType() == BudgetType.AMOUNT) {
        item = BudgetPlanItem.fromAmount(envelopeBudget.getBudget(), amount, envelopeBudget);
      } else if (envelopeBudget.getType() == BudgetType.PERCENTAGE) {
        item =
            BudgetPlanItem.fromPercentage(envelopeBudget.getPercentage(), amount, envelopeBudget);
      } else if (envelopeBudget.getType() == BudgetType.LEFTOVER) {
        leftoverEnvelopeBudgetList.add(envelopeBudget);
      }

      if (item != null) {
        leftover = leftover.minus(item.getAmount());
        itemList.add(item);
      }
    }

    if (leftover.isPositive() && !leftoverEnvelopeBudgetList.isEmpty()) {
      Money evenlySplitLeftover =
          leftover.dividedBy(leftoverEnvelopeBudgetList.size(), RoundingMode.HALF_EVEN);
      for (EnvelopeBudget envelopeBudget : leftoverEnvelopeBudgetList) {
        itemList.add(BudgetPlanItem.fromAmount(evenlySplitLeftover, amount, envelopeBudget));
        leftover = leftover.minus(evenlySplitLeftover);
      }
    } else if (!leftoverEnvelopeBudgetList.isEmpty()) {
      for (EnvelopeBudget envelopeBudget : leftoverEnvelopeBudgetList) {
        itemList.add(
            BudgetPlanItem.fromAmount(
                Money.zero(amount.getCurrencyUnit()), amount, envelopeBudget));
      }
    }

    return new BudgetPlan(amount, leftover, itemList);
  }

  public Money getAmount() {
    return amount;
  }

  public Money getLeftover() {
    return leftover;
  }

  public List<BudgetPlanItem> getItemList() {
    return itemList;
  }
}
