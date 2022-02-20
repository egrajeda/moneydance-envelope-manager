package com.egrajeda.moneydance.envelopemanager.core.model;

import java.util.Date;
import java.util.List;

public interface TransactionsManager {
  List<Account> getAccountList();

  List<Envelope> getEnvelopeList(String accountId);

  List<Transaction> getTransactionList(String accountId);

  void setEnvelopeOfTransaction(Envelope envelope, Transaction transaction);

  void clearEnvelopeOfTransaction(Transaction transaction);

  List<EnvelopeReport> getEnvelopeReportList(String accountId, Date start, Date end);
}