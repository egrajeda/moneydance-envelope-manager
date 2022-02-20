package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.*;
import org.joda.money.Money;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnvelopeManagerWindow extends JFrame {
  private final TransactionsManager transactionsManager;
  private final TransactionsTableModel transactionsTableModel;
  private final EnvelopesComboBoxModel envelopesComboBoxModel;
  private final EnvelopesReportTab envelopesReportTab;
  private Account selectedAccount;
  private WindowClosingListener windowClosingListener;

  public EnvelopeManagerWindow(
      TransactionsManager transactionsManager, UserPreferences userPreferences) {
    super();
    this.transactionsManager = transactionsManager;

    setTitle("Envelope Manager");
    setMinimumSize(new Dimension(1024, 768));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    transactionsTableModel = new TransactionsTableModel();
    JTable table = new JTable(transactionsTableModel);
    table.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
    table.setDefaultRenderer(
        Date.class, new DateTableCellRenderer(userPreferences.getDateFormat()));
    table.setDefaultRenderer(ClearedStatus.class, new ClearedStatusTableCellRenderer());
    table.setDefaultRenderer(Money.class, new MoneyTableCellRenderer(true));
    table.setDefaultRenderer(Envelope.class, new EnvelopeTableCellRenderer());

    table.setRowHeight(20);
    table.setShowHorizontalLines(false);

    TableRowSorter<TransactionsTableModel> sorter = new TableRowSorter<>(transactionsTableModel);
    sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
    table.setRowSorter(sorter);

    TableColumnModel tableColumnModel = table.getColumnModel();
    for (int column = 0; column < tableColumnModel.getColumnCount(); column++) {
      tableColumnModel
          .getColumn(column)
          .setPreferredWidth(transactionsTableModel.getColumnWidth(column));
    }

    envelopesComboBoxModel = new EnvelopesComboBoxModel();
    JComboBox<Envelope> envelopeComboBox = new JComboBox<>(envelopesComboBoxModel);
    envelopeComboBox.setRenderer(new EnvelopeListCellRenderer());
    DefaultCellEditor envelopesComboBoxCellEditor = new DefaultCellEditor(envelopeComboBox);
    envelopesComboBoxCellEditor.setClickCountToStart(2);

    table
        .getColumnModel()
        .getColumn(TransactionsTableModel.COLUMN_ENVELOPE_INDEX)
        .setCellEditor(envelopesComboBoxCellEditor);

    transactionsTableModel.addTableModelListener(
        tableModelEvent -> {
          if (tableModelEvent.getType() == TableModelEvent.UPDATE
              && tableModelEvent.getColumn() == TransactionsTableModel.COLUMN_ENVELOPE_INDEX) {
            TransactionsTableModel model = (TransactionsTableModel) tableModelEvent.getSource();
            int row = tableModelEvent.getFirstRow();
            Transaction transaction = model.getTransactionAt(row);
            transactionsManager.clearEnvelopeOfTransaction(transaction);
            if (transaction.getEnvelope() != null) {
              transactionsManager.setEnvelopeOfTransaction(transaction.getEnvelope(), transaction);
            }
            // Refresh the envelope amounts
            SwingUtilities.invokeLater(this::refreshEnvelopeList);
          }
        });

    JPanel panel = new JPanel(new BorderLayout());

    JPanel subPanel0 = new JPanel();
    subPanel0.setLayout(new BoxLayout(subPanel0, BoxLayout.LINE_AXIS));
    subPanel0.setBorder(new EmptyBorder(5, 5, 5, 5));

    JComboBox<Account> accountComboBox =
        new JComboBox<>(transactionsManager.getAccountList().toArray(new Account[0]));
    accountComboBox.addItemListener(
        itemEvent -> {
          onAccountSelected((Account) itemEvent.getItem());
        });
    accountComboBox.setMaximumSize(accountComboBox.getPreferredSize());

    subPanel0.add(new JLabel("Account:"));
    subPanel0.add(Box.createHorizontalStrut(5));
    subPanel0.add(accountComboBox);
    subPanel0.add(Box.createHorizontalGlue());

    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(
        actionEvent -> {
          refreshTransactionList();
          refreshEnvelopeList();
        });
    subPanel0.add(refreshButton);

    panel.add(subPanel0, BorderLayout.PAGE_START);

    envelopesReportTab = new EnvelopesReportTab(transactionsManager);

    JTabbedPane tabbedPane = new JTabbedPane();

    tabbedPane.add("Uncleared Transactions", new JScrollPane(table));
    tabbedPane.add("Envelopes Report", envelopesReportTab);

    panel.add(tabbedPane, BorderLayout.CENTER);

    Account firstAccount = transactionsManager.getAccountList().get(0);
    onAccountSelected(firstAccount);

    add(panel);

    pack();
  }

  public void setWindowClosingListener(WindowClosingListener windowClosingListener) {
    this.windowClosingListener = windowClosingListener;
  }

  private void onAccountSelected(Account account) {
    selectedAccount = account;
    refreshTransactionList();
    refreshEnvelopeList();
    envelopesReportTab.refreshEnvelopeReportList(selectedAccount.getId());
  }

  private void refreshTransactionList() {
    transactionsTableModel.setTransactionList(
        transactionsManager.getTransactionList(selectedAccount.getId()));
  }

  private void refreshEnvelopeList() {
    List<Envelope> envelopeList = new ArrayList<>();
    envelopeList.add(new NullEnvelope());
    envelopeList.addAll(transactionsManager.getEnvelopeList(selectedAccount.getId()));
    envelopesComboBoxModel.setEnvelopeList(envelopeList);
  }

  @Override
  protected void processEvent(AWTEvent event) {
    if (event.getID() == WindowEvent.WINDOW_CLOSING) {
      if (windowClosingListener != null) {
        windowClosingListener.onWindowClosing();
        return;
      }
    }
    super.processEvent(event);
  }
}