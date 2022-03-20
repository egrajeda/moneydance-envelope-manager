package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.BudgetPlan;
import com.egrajeda.moneydance.envelopemanager.core.model.BudgetType;
import com.egrajeda.moneydance.envelopemanager.core.model.EnvelopeBudget;
import org.joda.money.Money;

import javax.swing.table.AbstractTableModel;
import java.util.*;

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
  private List<EnvelopeBudgetTableRow> envelopeBudgetTableRowList = Collections.emptyList();

  public void setBudgetPlan(BudgetPlan budgetPlan) {
    envelopeBudgetTableRowList = new ArrayList<>();
    envelopeBudgetTableRowList.add(
            EnvelopeBudgetTableRow.fromIncome("Income", budgetPlan.getAmount()));
    envelopeBudgetTableRowList.add(EnvelopeBudgetTableRow.empty());

    envelopeBudgetTableRowList.addAll(
            budgetPlan.getItemList().stream()
                    .map(
                            item ->
                                    EnvelopeBudgetTableRow.fromEnvelopeBudget(
                                            item.getEnvelopeBudget(), item.getPercentage(), item.getAmount()))
                    .sorted(Comparator.comparing(EnvelopeBudgetTableRow::getName))
                    .toList());

    envelopeBudgetTableRowList.add(EnvelopeBudgetTableRow.empty());
    envelopeBudgetTableRowList.add(
            EnvelopeBudgetTableRow.fromIncome("Balance", budgetPlan.getLeftover()));

    fireTableDataChanged();
  }

  public EnvelopeBudget getEnvelopeBudgetAt(int rowIndex) {
    return envelopeBudgetTableRowList.get(rowIndex).getEnvelopeBudget();
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
    EnvelopeBudget envelopeBudget = row.getEnvelopeBudget();
    if (columnIndex == COLUMN_BUDGET_TYPE_INDEX) {
      if (!Objects.equals(value, row.getType())) {
        BudgetType type = (BudgetType) value;
        if (type == BudgetType.PERCENTAGE) {
          envelopeBudget.setPercentage(row.getPercentage());
        } else if (type == BudgetType.AMOUNT) {
          envelopeBudget.setBudget(row.getBudget());
        } else {
          envelopeBudget.setType(type);
        }
        fireTableRowsUpdated(rowIndex, rowIndex);
      }
    } else if (columnIndex == COLUMN_BUDGET_PERCENTAGE_INDEX) {
      if (!Objects.equals(value, row.getPercentage())) {
        envelopeBudget.setPercentage((float) value);
        fireTableRowsUpdated(rowIndex, rowIndex);
      }
    } else if (columnIndex == COLUMN_BUDGET_INDEX) {
      if (!Objects.equals(value, row.getBudget())) {
        envelopeBudget.setBudget((Money) value);
        fireTableRowsUpdated(rowIndex, rowIndex);
      }
    } else {
      throw new IllegalStateException("Unexpected value: " + columnIndex);
    }
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    EnvelopeBudgetTableRow row = envelopeBudgetTableRowList.get(rowIndex);
    if (row.getPercentage() == null || row.getBudget() == null) {
      return false;
    }

    return columnIndex == COLUMN_BUDGET_TYPE_INDEX
        || columnIndex == COLUMN_BUDGET_PERCENTAGE_INDEX
        || columnIndex == COLUMN_BUDGET_INDEX;
  }
}
