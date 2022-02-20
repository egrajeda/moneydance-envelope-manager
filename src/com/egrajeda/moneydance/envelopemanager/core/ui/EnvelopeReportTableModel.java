package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.EnvelopeReport;
import org.joda.money.Money;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.List;

public class EnvelopeReportTableModel extends AbstractTableModel {
  public static final int COLUMN_NAME_INDEX = 0;
  public static final int COLUMN_INITIAL_BALANCE_INDEX = 1;
  public static final int COLUMN_BUDGETED_INDEX = 2;
  public static final int COLUMN_ADJUSTED_INDEX = 3;
  public static final int COLUMN_SPENT_INDEX = 4;
  public static final int COLUMN_BALANCE_INDEX = 5;
  private static final String[] COLUMNS = {
    "Envelope", "Initial Balance", "Budget", "Adjustments", "Spend", "Balance"
  };
  private static final int[] COLUMN_WIDTHS = new int[] {500, 100, 100, 100, 100, 100};
  private List<EnvelopeReport> envelopeReportList = Collections.emptyList();

  public void setEnvelopeReportList(List<EnvelopeReport> envelopeReportList) {
    this.envelopeReportList = envelopeReportList;
    fireTableDataChanged();
  }

  @Override
  public int getRowCount() {
    return envelopeReportList.size();
  }

  @Override
  public int getColumnCount() {
    return COLUMNS.length;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return COLUMNS[columnIndex];
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
      case COLUMN_NAME_INDEX:
        return String.class;
      case COLUMN_INITIAL_BALANCE_INDEX:
      case COLUMN_BUDGETED_INDEX:
      case COLUMN_ADJUSTED_INDEX:
      case COLUMN_SPENT_INDEX:
      case COLUMN_BALANCE_INDEX:
        return Money.class;
      default:
        throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }

  public int getColumnWidth(int columnIndex) {
    return COLUMN_WIDTHS[columnIndex];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    EnvelopeReport report = envelopeReportList.get(rowIndex);
    switch (columnIndex) {
      case COLUMN_NAME_INDEX:
        return report.getName();
      case COLUMN_INITIAL_BALANCE_INDEX:
        return report.getInitialBalance();
      case COLUMN_BUDGETED_INDEX:
        return report.getBudget();
      case COLUMN_ADJUSTED_INDEX:
        return report.getAdjustments();
      case COLUMN_SPENT_INDEX:
        return report.getSpend();
      case COLUMN_BALANCE_INDEX:
        return report.getBalance();
      default:
        throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }
}
