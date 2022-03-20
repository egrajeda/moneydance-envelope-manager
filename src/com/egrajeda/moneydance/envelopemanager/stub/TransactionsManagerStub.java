package com.egrajeda.moneydance.envelopemanager.stub;

import com.egrajeda.moneydance.envelopemanager.core.model.*;
import com.egrajeda.moneydance.envelopemanager.core.ui.DateUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.LocalDate;
import java.util.*;

public class TransactionsManagerStub implements TransactionsManager {
  private static final List<Account> ACCOUNT_LIST =
      List.of(
          new Account("Checking", Money.zero(CurrencyUnit.EUR)),
          new Account("Savings", Money.zero(CurrencyUnit.EUR)));

  private static final List<Envelope> ENVELOPE_LIST =
      List.of(
          new Envelope("Savings", Money.of(CurrencyUnit.EUR, 1000.00)),
          new Envelope("Mortgage", Money.of(CurrencyUnit.EUR, 512.24)),
          new Envelope("Groceries", Money.of(CurrencyUnit.EUR, 182.84)),
          new Envelope("Medical", Money.zero(CurrencyUnit.EUR)),
          new Envelope("Gifts", Money.of(CurrencyUnit.EUR, -20.34)));

  private static final List<EnvelopeReport> ENVELOPE_REPORT_LIST =
      List.of(
          new EnvelopeReport(
              "Savings",
              Money.of(CurrencyUnit.EUR, 0.00),
              Money.of(CurrencyUnit.EUR, 1000.00),
              Money.of(CurrencyUnit.EUR, 1000.00),
              Money.of(CurrencyUnit.EUR, 0.00),
              Money.of(CurrencyUnit.EUR, 0.00)));

  private static final List<EnvelopeBudget> ENVELOPE_BUDGET_LIST =
      List.of(
          new EnvelopeBudget("Savings", BudgetType.PERCENTAGE, 0.15f, null),
          new EnvelopeBudget(
              "Supermarket", BudgetType.AMOUNT, null, Money.of(CurrencyUnit.EUR, 3000)),
          new EnvelopeBudget("Other", BudgetType.LEFTOVER, null, null));

  private static final Map<String, List<Transaction>> TRANSACTION_LIST_BY_ACCOUNT_ID =
      new HashMap<>() {
        {
          put(
              ACCOUNT_LIST.get(0).getId(),
              List.of(
                  new Transaction(
                      UUID.randomUUID().toString(),
                      DateUtils.create(2021, 12, 10),
                      "",
                      "CB AMAZON PAYMENTS 10/12",
                      "Ski and the boots",
                      "Personal:Sporting Goods",
                      ClearedStatus.RECONCILING,
                      Money.parse("EUR 499.99"),
                      ENVELOPE_LIST.get(0)),
                  new Transaction(
                      UUID.randomUUID().toString(),
                      DateUtils.create(2021, 12, 11),
                      "",
                      "CB CARREFOUR 11/12",
                      "Weekly groceries",
                      "Personal:Groceries",
                      ClearedStatus.UNRECONCILED,
                      Money.parse("EUR 14.98"))));
          put(
              ACCOUNT_LIST.get(1).getId(),
              List.of(
                  new Transaction(
                      UUID.randomUUID().toString(),
                      new Date(),
                      "",
                      "CB DOCTOR 17/11",
                      "Yearly check-up",
                      "Healthcare:Physician",
                      ClearedStatus.UNRECONCILED,
                      Money.parse("EUR 25.00"))));
        }
      };

  @Override
  public Account getAccount(String accountId) {
    return ACCOUNT_LIST.get(0);
  }

  @Override
  public List<Account> getAccountList() {
    return ACCOUNT_LIST;
  }

  @Override
  public List<Envelope> getEnvelopeList(String accountId) {
    return ENVELOPE_LIST;
  }

  @Override
  public List<Transaction> getTransactionList(String accountId) {
    return TRANSACTION_LIST_BY_ACCOUNT_ID.get(accountId);
  }

  @Override
  public void setEnvelopeOfTransaction(Envelope envelope, Transaction transaction) {}

  @Override
  public void clearEnvelopeOfTransaction(Transaction transaction) {}

  @Override
  public List<EnvelopeReport> getEnvelopeReportList(
      String accountId, LocalDate start, LocalDate end) {
    return ENVELOPE_REPORT_LIST;
  }

  @Override
  public List<EnvelopeBudget> getEnvelopeBudgetList(String accountId) {
    return ENVELOPE_BUDGET_LIST;
  }

  @Override
  public void saveEnvelopeBudget(EnvelopeBudget envelopeBudget) {

  }
}
