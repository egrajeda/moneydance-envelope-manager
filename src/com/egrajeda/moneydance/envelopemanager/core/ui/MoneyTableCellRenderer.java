package com.egrajeda.moneydance.envelopemanager.core.ui;

import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import javax.swing.*;
import java.awt.*;

public class MoneyTableCellRenderer extends DefaultTableCellRenderer {
  private static final Color POSITIVE_FOREGROUND_COLOR = new Color(51, 51, 51);
  private static final Color NEGATIVE_FOREGROUND_COLOR = new Color(220, 50, 47);
  private static final Color ZERO_FOREGROUND_COLOR = new Color(153, 153, 153);
  private static final MoneyFormatter MONEY_FORMATTER =
      new MoneyFormatterBuilder().appendAmount().toFormatter();
  private final boolean isUnsigned;

  public MoneyTableCellRenderer() {
    this(false);
  }

  public MoneyTableCellRenderer(boolean isUnsigned) {
    this.isUnsigned = isUnsigned;
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    Money money = isUnsigned ? ((Money) value).abs() : (Money) value;
    value = MONEY_FORMATTER.print(money);
    Component component =
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    setFont(MONOSPACED_FONT);
    if (money.isPositive()) {
      setForeground(POSITIVE_FOREGROUND_COLOR);
    } else if (money.isNegative()) {
      setForeground(NEGATIVE_FOREGROUND_COLOR);
    } else {;
      setForeground(ZERO_FOREGROUND_COLOR);
    }
    setHorizontalAlignment(SwingConstants.RIGHT);
    return component;
  }
}
