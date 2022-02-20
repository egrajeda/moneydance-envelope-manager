package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.Envelope;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

import javax.swing.*;
import java.awt.*;

public class EnvelopeListCellRenderer extends DefaultListCellRenderer {
  private static final Color FOREGROUND_COLOR = new Color(220, 50, 47);
  private static final MoneyFormatter MONEY_FORMATTER =
      new MoneyFormatterBuilder()
          .appendCurrencySymbolLocalized()
          .appendLiteral(" ")
          .appendAmount()
          .toFormatter();

  @Override
  public Component getListCellRendererComponent(
      JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    if (value == null) {
      return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

    Envelope envelope = (Envelope) value;
    if (envelope instanceof NullEnvelope) {
      value = envelope.getName();
    } else {
      value = envelope.getName() + " (" + MONEY_FORMATTER.print(envelope.getBalance()) + ")";
    }
    Component component =
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    if (envelope.getBalance().isNegative()) {
      setForeground(FOREGROUND_COLOR);
    }
    return component;
  }
}
