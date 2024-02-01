package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.Account;
import com.egrajeda.moneydance.envelopemanager.core.model.Envelope;
import com.egrajeda.moneydance.envelopemanager.core.model.TransactionsManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsWindow extends JFrame {
  private final TransactionsManager transactionsManager;
  private final EnvelopesComboBoxModel envelopesComboBoxModel;

  public SettingsWindow(TransactionsManager transactionsManager, UserPreferences userPreferences) {
    this.transactionsManager = transactionsManager;

    setTitle("Settings");
    setMinimumSize(new Dimension(500, 150));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.setBorder(new EmptyBorder(5, 5, 5, 5));

    LabelWithComboBox<Account> mainAccountComponent =
        new LabelWithComboBox<>(
            "Main Account:", transactionsManager.getAccountList().toArray(new Account[0]));
    mainAccountComponent.addItemListener(
        itemEvent -> onAccountSelected(((Account) itemEvent.getItem()).getId()));
    panel.add(mainAccountComponent);

    envelopesComboBoxModel = new EnvelopesComboBoxModel();
    LabelWithComboBox<Envelope> savingsEnvelopeComponent =
        new LabelWithComboBox<>("Savings Envelope:", envelopesComboBoxModel);
    panel.add(savingsEnvelopeComponent);

    panel.add(Box.createVerticalGlue());

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
    bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JButton okButton = new JButton("OK");
    okButton.addActionListener(
        actionEvent -> {
          userPreferences.setMainAccountId(mainAccountComponent.getSelectedItem().getId());
          userPreferences.setSavingsEnvelopeId(savingsEnvelopeComponent.getSelectedItem().getId());
          dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(
        actionEvent -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

    bottomPanel.add(Box.createHorizontalGlue());
    bottomPanel.add(okButton);
    bottomPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    bottomPanel.add(cancelButton);
    panel.add(bottomPanel);

    String mainAccountId =
        userPreferences.getMainAccountId() == null
            ? transactionsManager.getAccountList().getFirst().getId()
            : userPreferences.getMainAccountId();
    Account mainAccount =
        transactionsManager.getAccount(mainAccountId) == null
            ? transactionsManager.getAccountList().getFirst()
            : transactionsManager.getAccount(mainAccountId);
    if (Objects.equals(mainAccountComponent.getSelectedItem(), mainAccount)) {
      onAccountSelected(mainAccount.getId());
    } else {
      mainAccountComponent.setSelectedItem(mainAccount);
    }

    String savingsEnvelopeId = userPreferences.getSavingsEnvelopeId();
    if (savingsEnvelopeId != null) {
      Envelope savingsEnvelope = transactionsManager.getEnvelope(savingsEnvelopeId);
      savingsEnvelopeComponent.setSelectedItem(savingsEnvelope);
    }

    add(panel);

    pack();
  }

  private void onAccountSelected(String accountId) {
    List<Envelope> envelopeList = new ArrayList<>(transactionsManager.getEnvelopeList(accountId));
    envelopesComboBoxModel.setEnvelopeList(envelopeList);
    if (!envelopeList.isEmpty()) {
      envelopesComboBoxModel.setSelectedItem(envelopeList.getFirst());
    }
  }
}
