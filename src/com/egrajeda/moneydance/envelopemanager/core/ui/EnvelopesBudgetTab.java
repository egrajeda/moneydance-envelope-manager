package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class EnvelopesBudgetTab extends JPanel {
  private static final int ENTIRE_TABLE_WAS_CHANGED = 0;
  private final TransactionsManager transactionsManager;
  private final EnvelopeBudgetTableModel envelopeBudgetTableModel = new EnvelopeBudgetTableModel();
  private final MoneyTableCellEditor moneyTableCellEditor = new MoneyTableCellEditor();
  private String accountId;
  private BudgetPlan budgetPlan;

  public EnvelopesBudgetTab(TransactionsManager transactionsManager) {
    super(new BorderLayout());
    this.transactionsManager = transactionsManager;

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

    JComboBox<BudgetType> budgetTypesComboBox = new JComboBox<>(BudgetType.values());
    DefaultCellEditor budgetTypesTableCellEditor = new DefaultCellEditor(budgetTypesComboBox);
    budgetTypesTableCellEditor.setClickCountToStart(2);

    table
        .getColumnModel()
        .getColumn(EnvelopeBudgetTableModel.COLUMN_BUDGET_TYPE_INDEX)
        .setCellEditor(budgetTypesTableCellEditor);

    PercentageTableCellEditor percentageTableCellEditor = new PercentageTableCellEditor();
    percentageTableCellEditor.setClickCountToStart(2);

    table
        .getColumnModel()
        .getColumn(EnvelopeBudgetTableModel.COLUMN_BUDGET_PERCENTAGE_INDEX)
        .setCellEditor(percentageTableCellEditor);

    moneyTableCellEditor.setClickCountToStart(2);

    table
        .getColumnModel()
        .getColumn(EnvelopeBudgetTableModel.COLUMN_BUDGET_INDEX)
        .setCellEditor(moneyTableCellEditor);

    envelopeBudgetTableModel.addTableModelListener(
        tableModelEvent -> {
          if (tableModelEvent.getType() == TableModelEvent.UPDATE) {
            EnvelopeBudgetTableModel model = (EnvelopeBudgetTableModel) tableModelEvent.getSource();
            int row = tableModelEvent.getFirstRow();
            if (row == ENTIRE_TABLE_WAS_CHANGED) {
              return;
            }

            EnvelopeBudget envelopeBudget = model.getEnvelopeBudgetAt(row);
            transactionsManager.saveEnvelopeBudget(envelopeBudget);
            SwingUtilities.invokeLater(this::refreshEnvelopeBudgetList);
          }
        });

    JButton assignButton = new JButton("Assign");
    assignButton.addActionListener(
        actionEvent -> {
          int result =
              JOptionPane.showConfirmDialog(
                  this,
                  "A new transaction will be created per envelope to transfer the amount specified from the main account.\nAre you sure you want to continue?",
                  "Assign Budget?",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (result == JOptionPane.YES_OPTION) {
            transactionsManager.assignBudgetPlan(budgetPlan);
          }
        });

    TableTopPanel panel = new TableTopPanel();

    panel.add(Box.createHorizontalGlue());
    panel.add(assignButton);

    add(panel, BorderLayout.PAGE_START);
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
    CurrencyUnit currency = account.getBalance().getCurrencyUnit();
    Money income = account.getBalance();
    List<EnvelopeBudget> envelopeBudgetList = transactionsManager.getEnvelopeBudgetList(accountId);

    budgetPlan = BudgetPlan.calculate(income, envelopeBudgetList);
    envelopeBudgetTableModel.setBudgetPlan(budgetPlan);
    moneyTableCellEditor.setCurrency(currency);
  }
}
