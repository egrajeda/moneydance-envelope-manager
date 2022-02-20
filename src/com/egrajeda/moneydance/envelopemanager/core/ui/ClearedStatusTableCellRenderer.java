package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.ClearedStatus;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ClearedStatusTableCellRenderer extends DefaultTableCellRenderer {
  private static final Font FONT = MONOSPACED_FONT.deriveFont(20f);
  private static final Color FOREGROUND_COLOR = new Color(244, 165, 54);
  private static final Map<ClearedStatus, String> CLEARED_STATUS_TO_ICON =
      new HashMap<>() {
        {
          put(ClearedStatus.CLEARED, "●");
          put(ClearedStatus.RECONCILING, "◆");
          put(ClearedStatus.UNRECONCILED, " ");
        }
      };

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    value = CLEARED_STATUS_TO_ICON.get((ClearedStatus) value);
    Component component =
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    setFont(FONT);
    setForeground(FOREGROUND_COLOR);
    setHorizontalAlignment(SwingConstants.CENTER);
    return component;
  }
}
