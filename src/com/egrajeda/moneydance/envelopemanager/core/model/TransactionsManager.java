package com.egrajeda.moneydance.envelopemanager.core.model;

import java.time.LocalDate;
import java.util.List;

public interface TransactionsManager {
  Account getAccount(String accountId);

  List<Account> getAccountList();

  Envelope getEnvelope(String envelopeId);

  List<Envelope> getEnvelopeList(String accountId);

  List<Transaction> getTransactionList(String accountId);

  void setEnvelopeOfTransaction(Envelope envelope, Transaction transaction);

  void clearEnvelopeOfTransaction(Transaction transaction);

  List<EnvelopeReport> getEnvelopeReportList(String accountId, LocalDate start, LocalDate end);

  List<EnvelopeBudget> getEnvelopeBudgetList(String accountId);

  void saveEnvelopeBudget(EnvelopeBudget envelopeBudget);

  void assignBudgetPlan(BudgetPlan budgetPlan);
}
