package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.*;
import org.joda.money.Money;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class EnvelopesBudgetTab extends JPanel {
  private final TransactionsManager transactionsManager;
  private final EnvelopeBudgetTableModel envelopeBudgetTableModel;
  private String accountId;

  public EnvelopesBudgetTab(TransactionsManager transactionsManager) {
    super(new BorderLayout());
    this.transactionsManager = transactionsManager;

    envelopeBudgetTableModel = new EnvelopeBudgetTableModel();
    JTable table = new JTable(envelopeBudgetTableModel);
    table.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
    table.setDefaultRenderer(Money.class, new MoneyTableCellRenderer());
    table.setDefaultRenderer(BudgetType.class, new BudgetTypeTableCellRenderer());
    table.setDefaultRenderer(Float.class, new PercentageTableCellRenderer());

    table.setRowHeight(20);
    table.setShowHorizontalLines(false);

    TableColumnModel tableColumnModel = table.getColumnModel();
    for (int column = 0; column < tableColumnModel.getColumnCount(); column++) {
      tableColumnModel
          .getColumn(column)
          .setPreferredWidth(envelopeBudgetTableModel.getColumnWidth(column));
    }

    PercentageCellEditor percentageCellEditor = new PercentageCellEditor();
    percentageCellEditor.setClickCountToStart(2);

    table
        .getColumnModel()
        .getColumn(EnvelopeBudgetTableModel.COLUMN_BUDGET_PERCENTAGE_INDEX)
        .setCellEditor(percentageCellEditor);

    add(new JScrollPane(table), BorderLayout.CENTER);
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
    refreshEnvelopeBudgetList();
  }

  private void refreshEnvelopeBudgetList() {
    if (accountId == null) {
      return;
    }

    Account account = transactionsManager.getAccount(accountId);
    List<EnvelopeBudget> envelopeBudgetList = transactionsManager.getEnvelopeBudgetList(accountId);
    Money income =
        Money.of(
            account.getBalance().getCurrencyUnit(),
            BigDecimal.valueOf(3000)
                .movePointLeft(account.getBalance().getCurrencyUnit().getDecimalPlaces()),
            RoundingMode.HALF_EVEN);

    BudgetPlan budgetPlan = BudgetPlan.calculate(income, envelopeBudgetList);

    List<EnvelopeBudgetTableRow> envelopeBudgetTableRowList = new ArrayList<>();
    envelopeBudgetTableRowList.add(
        EnvelopeBudgetTableRow.fromIncome("Income", budgetPlan.getAmount()));
    envelopeBudgetTableRowList.add(EnvelopeBudgetTableRow.empty());

    envelopeBudgetTableRowList.addAll(
        budgetPlan.getItemList().stream()
            .map(
                item ->
                    EnvelopeBudgetTableRow.fromBudget(
                        item.getEnvelopeBudget().getName(),
                        item.getEnvelopeBudget().getType(),
                        item.getPercentage(),
                        item.getAmount()))
            .toList());

    envelopeBudgetTableRowList.add(EnvelopeBudgetTableRow.empty());
    envelopeBudgetTableRowList.add(
        EnvelopeBudgetTableRow.fromIncome("Balance", budgetPlan.getLeftover()));

    envelopeBudgetTableModel.setIncome(budgetPlan.getAmount());
    envelopeBudgetTableModel.setEnvelopeBudgetTableRowList(envelopeBudgetTableRowList);
  }

}
