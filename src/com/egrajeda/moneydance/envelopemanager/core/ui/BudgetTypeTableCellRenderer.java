package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.BudgetType;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BudgetTypeTableCellRenderer extends DefaultTableCellRenderer {
  private static final Map<BudgetType, String> BUDGET_TYPE_TO_STRING =
      new HashMap<>() {
        {
          put(BudgetType.AMOUNT, "Amount");
          put(BudgetType.PERCENTAGE, "Percentage");
          put(BudgetType.LEFTOVER, "Leftover");
        }
      };

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    value = BUDGET_TYPE_TO_STRING.get((BudgetType) value);
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }
}
