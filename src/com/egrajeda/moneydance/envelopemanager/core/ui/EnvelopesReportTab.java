package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.TransactionsManager;
import com.infinitekind.util.DateUtil;
import org.joda.money.Money;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Date;

public class EnvelopesReportTab extends JPanel {
  private final TransactionsManager transactionsManager;
  private final EnvelopeReportTableModel envelopeReportTableModel;

  public EnvelopesReportTab(TransactionsManager transactionsManager) {
    super(new BorderLayout());
    this.transactionsManager = transactionsManager;

    envelopeReportTableModel = new EnvelopeReportTableModel();
    JTable table = new JTable(envelopeReportTableModel);
    table.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
    table.setDefaultRenderer(Money.class, new MoneyTableCellRenderer());

    table.setRowHeight(20);
    table.setShowHorizontalLines(false);

    TableColumnModel tableColumnModel = table.getColumnModel();
    for (int column = 0; column < tableColumnModel.getColumnCount(); column++) {
      tableColumnModel
          .getColumn(column)
          .setPreferredWidth(envelopeReportTableModel.getColumnWidth(column));
    }

    add(new JScrollPane(table), BorderLayout.CENTER);
  }

  public void refreshEnvelopeReportList(String accountId) {
    envelopeReportTableModel.setEnvelopeReportList(
        transactionsManager.getEnvelopeReportList(
            accountId, DateUtil.firstDayInMonth(new Date()), DateUtil.lastDayInMonth(new Date())));
  }
}
