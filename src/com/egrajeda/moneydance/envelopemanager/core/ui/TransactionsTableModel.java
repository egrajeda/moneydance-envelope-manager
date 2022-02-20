package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.ClearedStatus;
import com.egrajeda.moneydance.envelopemanager.core.model.Envelope;
import com.egrajeda.moneydance.envelopemanager.core.model.Transaction;
import org.joda.money.Money;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TransactionsTableModel extends AbstractTableModel {
  public static final int COLUMN_DATE_INDEX = 0;
  public static final int COLUMN_CHECK_NUMBER_INDEX = 1;
  public static final int COLUMN_DESCRIPTION_INDEX = 2;
  public static final int COLUMN_CATEGORY_INDEX = 3;
  public static final int COLUMN_CLEARED_STATUS_INDEX = 4;
  public static final int COLUMN_PAYMENT_INDEX = 5;
  public static final int COLUMN_ENVELOPE_INDEX = 6;
  private static final String[] COLUMNS = {
    "Date", "Check#", "Description", "Category", "C", "Payment", "Envelope"
  };
  private static final int[] COLUMN_WIDTHS = new int[] {100, 100, 250, 200, 25, 100, 225};
  private List<Transaction> transactionList = Collections.emptyList();

  public void setTransactionList(List<Transaction> transactionList) {
    this.transactionList = transactionList;
    fireTableDataChanged();
  }

  public Transaction getTransactionAt(int rowIndex) {
    return transactionList.get(rowIndex);
  }

  @Override
  public int getRowCount() {
    return transactionList.size();
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
      case COLUMN_DATE_INDEX:
        return Date.class;
      case COLUMN_CHECK_NUMBER_INDEX:
      case COLUMN_DESCRIPTION_INDEX:
      case COLUMN_CATEGORY_INDEX:
        return String.class;
      case COLUMN_CLEARED_STATUS_INDEX:
        return ClearedStatus.class;
      case COLUMN_PAYMENT_INDEX:
        return Money.class;
      case COLUMN_ENVELOPE_INDEX:
        return Envelope.class;
      default:
        throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }

  public int getColumnWidth(int columnIndex) {
    return COLUMN_WIDTHS[columnIndex];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Transaction transaction = getTransactionAt(rowIndex);
    switch (columnIndex) {
      case COLUMN_DATE_INDEX:
        return transaction.getDate();
      case COLUMN_CHECK_NUMBER_INDEX:
        return transaction.getCheckNumber();
      case COLUMN_DESCRIPTION_INDEX:
        return transaction.getDescription();
      case COLUMN_CATEGORY_INDEX:
        return transaction.getCategory();
      case COLUMN_CLEARED_STATUS_INDEX:
        return transaction.getClearedStatus();
      case COLUMN_PAYMENT_INDEX:
        return transaction.getPayment();
      case COLUMN_ENVELOPE_INDEX:
        return transaction.getEnvelope();
      default:
        throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    Transaction transaction = getTransactionAt(rowIndex);
    if (columnIndex == COLUMN_ENVELOPE_INDEX) {
      if (value instanceof NullEnvelope) {
        value = null;
      }

      if (!Objects.equals(value, transaction.getEnvelope())) {
        transaction.setEnvelope((Envelope) value);
        fireTableCellUpdated(rowIndex, columnIndex);

        transaction.setClearedStatus(
            value == null ? ClearedStatus.UNRECONCILED : ClearedStatus.RECONCILING);
        fireTableCellUpdated(rowIndex, COLUMN_CLEARED_STATUS_INDEX);
      }
    } else {
      throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columnIndex == COLUMN_ENVELOPE_INDEX;
  }
}
