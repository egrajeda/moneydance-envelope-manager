package com.egrajeda.moneydance.envelopemanager.core.ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class DateTableCellRenderer extends DefaultTableCellRenderer {
  private final SimpleDateFormat dateFormat;

  public DateTableCellRenderer(String dateFormat) {
    this.dateFormat = new SimpleDateFormat(dateFormat);
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    value = dateFormat.format(value);
    Component component =
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    setFont(MONOSPACED_FONT);
    return component;
  }

  @Override
  public int getHorizontalAlignment() {
    return SwingConstants.CENTER;
  }
}
