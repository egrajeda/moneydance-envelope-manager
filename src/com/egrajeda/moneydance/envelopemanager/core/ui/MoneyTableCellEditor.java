package com.egrajeda.moneydance.envelopemanager.core.ui;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.joda.money.format.MoneyParseContext;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class MoneyTableCellEditor extends DefaultCellEditor implements TableCellEditor {
  private static final MoneyFormatter MONEY_FORMATTER =
      new MoneyFormatterBuilder().appendAmount().toFormatter();
  private final JTextField textField;
  private CurrencyUnit currency;

  public MoneyTableCellEditor() {
    super(new JTextField());
    textField = (JTextField) super.getComponent();
    textField.setHorizontalAlignment(SwingConstants.RIGHT);
  }

  public void setCurrency(CurrencyUnit currency) {
    this.currency = currency;
  }

  @Override
  public Component getTableCellEditorComponent(
      JTable jTable, Object value, boolean isSelected, int rowIndex, int columnIndex) {
    textField.setText(MONEY_FORMATTER.print((Money) value));
    return textField;
  }

  @Override
  public Object getCellEditorValue() {
    MoneyParseContext parseContext = MONEY_FORMATTER.parse(textField.getText(), 0);
    parseContext.setCurrency(currency);
    return parseContext.toBigMoney().toMoney();
  }
}
