package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.TransactionsManager;
import org.joda.money.Money;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.stream.IntStream;

public class EnvelopesReportTab extends JPanel {
  private final TransactionsManager transactionsManager;
  private final EnvelopeReportTableModel envelopeReportTableModel;
  private final JComboBox<Month> monthComboBox;
  private final JComboBox<Integer> yearComboBox;
  private String accountId;
  private LocalDate selectedDate;

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

    monthComboBox = new JComboBox<>(getMonthList());
    yearComboBox = new JComboBox<>(getYearList());

    monthComboBox.setRenderer(new MonthListCellRenderer());
    monthComboBox.setMaximumSize(monthComboBox.getPreferredSize());
    monthComboBox.addItemListener(
        itemEvent -> {
          if (yearComboBox.getSelectedItem() == null) {
            return;
          }

          onYearMonthSelected((int) yearComboBox.getSelectedItem(), (Month) itemEvent.getItem());
        });

    yearComboBox.setMaximumSize(yearComboBox.getPreferredSize());
    yearComboBox.addItemListener(
        itemEvent ->
            onYearMonthSelected(
                (int) itemEvent.getItem(), (Month) monthComboBox.getSelectedItem()));

    JButton previousMonthButton = new JButton("⯇");
    previousMonthButton.addActionListener(
        actionEvent -> {
          LocalDate date = selectedDate.minusMonths(1);
          setSelectedDate(date);
        });

    JButton nextMonthButton = new JButton("⯈");
    nextMonthButton.addActionListener(
        actionEvent -> {
          LocalDate date = selectedDate.plusMonths(1);
          setSelectedDate(date);
        });

    TableTopPanel panel = new TableTopPanel();

    panel.add(Box.createHorizontalGlue());
    panel.add(previousMonthButton);
    panel.add(Box.createRigidArea(new Dimension(10, 0)));
    panel.add(monthComboBox);
    panel.add(Box.createRigidArea(new Dimension(10, 0)));
    panel.add(yearComboBox);
    panel.add(Box.createRigidArea(new Dimension(10, 0)));
    panel.add(nextMonthButton);
    panel.add(Box.createHorizontalGlue());

    add(panel, BorderLayout.PAGE_START);
    add(new JScrollPane(table), BorderLayout.CENTER);

    setSelectedDate(LocalDate.now());
  }

  private void setSelectedDate(LocalDate selectedDate) {
    monthComboBox.setSelectedItem(selectedDate.getMonth());
    yearComboBox.setSelectedItem(selectedDate.getYear());
    // This must be last, as the previous combo box event listeners will also partially set selectedDate
    this.selectedDate = selectedDate;
  }

  private void onYearMonthSelected(int year, Month month) {
    selectedDate = LocalDate.of(year, month, 1);
    refreshEnvelopeReportList();
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
    refreshEnvelopeReportList();
  }

  private void refreshEnvelopeReportList() {
    if (accountId == null) {
      return;
    }

    LocalDate start = selectedDate.withDayOfMonth(1);
    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
    envelopeReportTableModel.setEnvelopeReportList(
        transactionsManager.getEnvelopeReportList(accountId, start, end));
  }

  private Integer[] getYearList() {
    return IntStream.rangeClosed(Year.now().getValue() - 100, Year.now().getValue())
        .boxed()
        .sorted(Collections.reverseOrder())
        .toArray(Integer[]::new);
  }

  private Month[] getMonthList() {
    return Month.values();
  }
}
