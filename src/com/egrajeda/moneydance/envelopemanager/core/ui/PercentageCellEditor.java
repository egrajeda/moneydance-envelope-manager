package com.egrajeda.moneydance.envelopemanager.core.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;

class PercentageCellEditor extends DefaultCellEditor implements TableCellEditor {
  private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
  private final JTextField textField;

  public PercentageCellEditor() {
    super(new JTextField());
    numberFormat.setMinimumFractionDigits(2);
    textField = (JTextField) super.getComponent();
    textField.setHorizontalAlignment(SwingConstants.RIGHT);
  }

  @Override
  public Component getTableCellEditorComponent(
      JTable jTable, Object value, boolean isSelected, int rowIndex, int columnIndex) {
    textField.setText(numberFormat.format(((float) value) * 100));
    return textField;
  }

  @Override
  public Object getCellEditorValue() {
    try {
      Number number = numberFormat.parse(textField.getText());
      return Math.max(Math.min(number.floatValue() / 100, 1), 0);
    } catch (ParseException e) {
      throw new IllegalStateException(e);
    }
  }
}
