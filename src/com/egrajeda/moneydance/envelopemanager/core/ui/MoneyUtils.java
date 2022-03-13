package com.egrajeda.moneydance.envelopemanager.core.ui;

import org.joda.money.Money;

import java.math.RoundingMode;

public class MoneyUtils {
  public static float divide(Money dividend, Money divisor) {
    return dividend.getAmount().divide(divisor.getAmount(), 4, RoundingMode.HALF_EVEN).floatValue();
  }
}
