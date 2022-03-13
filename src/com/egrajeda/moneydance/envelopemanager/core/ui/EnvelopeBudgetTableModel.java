package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.BudgetType;
import org.joda.money.Money;

import javax.swing.table.AbstractTableModel;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EnvelopeBudgetTableModel extends AbstractTableModel {
  public static final int COLUMN_NAME_INDEX = 0;
  public static final int COLUMN_INCOME_INDEX = 1;
  public static final int COLUMN_BUDGET_TYPE_INDEX = 2;
  public static final int COLUMN_BUDGET_PERCENTAGE_INDEX = 3;
  public static final int COLUMN_BUDGET_INDEX = 4;
  private static final String[] COLUMNS = {
    "Envelope", "Income", "Budget Type", "Budget (%)", "Budget"
  };
  private static final int[] COLUMN_WIDTHS = new int[] {450, 100, 100, 100, 100};
  private Money income;
  private List<EnvelopeBudgetTableRow> envelopeBudgetTableRowList = Collections.emptyList();

  public void setIncome(Money income) {
    this.income = income;
  }

  public void setEnvelopeBudgetTableRowList(
      List<EnvelopeBudgetTableRow> envelopeBudgetTableRowList) {
    this.envelopeBudgetTableRowList = envelopeBudgetTableRowList;
    fireTableDataChanged();
  }

  @Override
  public int getRowCount() {
    return this.envelopeBudgetTableRowList.size();
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
      case COLUMN_INCOME_INDEX:
      case COLUMN_BUDGET_INDEX:
        return Money.class;
      case COLUMN_BUDGET_TYPE_INDEX:
        return BudgetType.class;
      case COLUMN_BUDGET_PERCENTAGE_INDEX:
        return Float.class;
      default:
        throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }

  public int getColumnWidth(int columnIndex) {
    return COLUMN_WIDTHS[columnIndex];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    EnvelopeBudgetTableRow budget = envelopeBudgetTableRowList.get(rowIndex);
    switch (columnIndex) {
      case COLUMN_NAME_INDEX:
        return budget.getName();
      case COLUMN_INCOME_INDEX:
        return budget.getIncome();
      case COLUMN_BUDGET_TYPE_INDEX:
        return budget.getType();
      case COLUMN_BUDGET_PERCENTAGE_INDEX:
        return budget.getPercentage();
      case COLUMN_BUDGET_INDEX:
        return budget.getBudget();
      default:
        throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    EnvelopeBudgetTableRow row = envelopeBudgetTableRowList.get(rowIndex);
    if (columnIndex == COLUMN_BUDGET_PERCENTAGE_INDEX) {
      if (!Objects.equals(value, row.getPercentage())) {
        float percentage = (float) value;

        setValuesAndFireTableCellsUpdated(
            rowIndex,
            BudgetType.PERCENTAGE,
            percentage,
            income.multipliedBy(percentage, RoundingMode.HALF_EVEN));
      }
    } else if (columnIndex == COLUMN_BUDGET_INDEX) {
      if (!Objects.equals(value, row.getBudget())) {
        Money budget = (Money) value;

        setValuesAndFireTableCellsUpdated(
            rowIndex, BudgetType.AMOUNT, MoneyUtils.divide(budget, income), budget);
      }
    } else {
      throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }

  private void setValuesAndFireTableCellsUpdated(
      int rowIndex, BudgetType type, float percentage, Money budget) {
    EnvelopeBudgetTableRow row = envelopeBudgetTableRowList.get(rowIndex);

    row.setType(type);
    fireTableCellUpdated(rowIndex, COLUMN_BUDGET_TYPE_INDEX);

    row.setPercentage(percentage);
    fireTableCellUpdated(rowIndex, COLUMN_BUDGET_PERCENTAGE_INDEX);

    row.setBudget(budget);
    fireTableCellUpdated(rowIndex, COLUMN_BUDGET_INDEX);
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    EnvelopeBudgetTableRow row = envelopeBudgetTableRowList.get(rowIndex);
    if (row.getPercentage() == null || row.getBudget() == null) {
      return false;
    }

    return columnIndex == COLUMN_BUDGET_PERCENTAGE_INDEX || columnIndex == COLUMN_BUDGET_INDEX;
  }
}
