package com.egrajeda.moneydance.envelopemanager.core.ui;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class PercentageTableCellRenderer extends DefaultTableCellRenderer {
  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (value == null) {
      return super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
    }

    NumberFormat numberFormat = NumberFormat.getPercentInstance();
    numberFormat.setMinimumFractionDigits(2);
    value = numberFormat.format(value);
    Component component =
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    setFont(MONOSPACED_FONT);
    setHorizontalAlignment(SwingConstants.RIGHT);
    return component;
  }
}
