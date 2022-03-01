package com.egrajeda.moneydance.envelopemanager.core.ui;

import javax.swing.*;
import java.awt.*;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class MonthListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            return super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
        }

        Month month = (Month) value;
        value = month.getDisplayName(TextStyle.FULL, Locale.getDefault());
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
