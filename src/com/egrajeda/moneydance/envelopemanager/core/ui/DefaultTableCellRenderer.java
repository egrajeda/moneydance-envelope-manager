package com.egrajeda.moneydance.envelopemanager.core.ui;

import javax.swing.*;
import java.awt.*;

public class DefaultTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
  protected static final Font MONOSPACED_FONT = new Font("Monospaced", Font.PLAIN, 12);
  private static final Color BACKGROUND_COLOR = Color.WHITE;
  private static final Color STRIPPED_BACKGROUND_COLOR = new Color(241, 250, 254);

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    Component component =
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    setBorder(
        BorderFactory.createCompoundBorder(
            getBorder(), BorderFactory.createEmptyBorder(15, 10, 10, 10)));
    if (isSelected) {
      setBackground(new Color(84, 162, 249));
    } else {
      setBackground(row % 2 == 0 ? BACKGROUND_COLOR : STRIPPED_BACKGROUND_COLOR);
    }
    return component;
  }
}
