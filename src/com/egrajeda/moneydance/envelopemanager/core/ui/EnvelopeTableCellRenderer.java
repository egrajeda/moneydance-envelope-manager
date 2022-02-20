package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.Envelope;

import javax.swing.*;
import java.awt.*;

public class EnvelopeTableCellRenderer extends DefaultTableCellRenderer {
  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (value == null) {
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    Envelope envelope = (Envelope) value;
    value = envelope.getName();
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }
}
