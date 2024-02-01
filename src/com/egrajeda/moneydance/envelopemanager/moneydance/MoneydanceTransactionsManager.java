package com.egrajeda.moneydance.envelopemanager.moneydance;

import com.egrajeda.moneydance.envelopemanager.core.model.*;
import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.*;
import com.infinitekind.util.StringUtils;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MoneydanceTransactionsManager implements TransactionsManager {
  private static final String ENVELOPE_ADJUSTMENT_TRANSACTION_TAG = "overspend";

  private final AccountBook accountBook;
  private final MoneydanceMapper moneydanceMapper;

  public MoneydanceTransactionsManager(FeatureModuleContext featureModuleContext) {
    this.accountBook = featureModuleContext.getCurrentAccountBook();
    this.moneydanceMapper = new MoneydanceMapper(accountBook);
  }

  @Override
  public com.egrajeda.moneydance.envelopemanager.core.model.Account getAccount(String accountId) {
    if (accountId == null) {
      return null;
    }

    Account account = accountBook.getAccountByUUID(accountId);
    if (account == null) {
      return null;
    }

    return MoneydanceMapper.toAccount(account);
  }

  @Override
  public List<com.egrajeda.moneydance.envelopemanager.core.model.Account> getAccountList() {
    return accountBook.getRootAccount().getSubAccounts().stream()
        .filter(account -> account.getAccountType() == Account.AccountType.BANK)
        .map(MoneydanceMapper::toAccount)
        .collect(Collectors.toList());
  }

  @Override
  public Envelope getEnvelope(String envelopeId) {
    if (envelopeId == null) {
      return null;
    }

    Account account = accountBook.getAccountByUUID(envelopeId);
    if (account == null) {
      return null;
    }

    return MoneydanceMapper.toEnvelope(account);
  }

  @Override
  public List<Envelope> getEnvelopeList(String accountId) {
    return accountBook.getAccountByUUID(accountId).getSubAccounts().stream()
        .map(MoneydanceMapper::toEnvelope)
        .collect(Collectors.toList());
  }

  @Override
  public List<Transaction> getTransactionList(String accountId) {
    Account account = accountBook.getAccountByUUID(accountId);
    TxnSet transactionSet = accountBook.getTransactionSet().getTransactionsForAccount(account);

    return StreamSupport.stream(transactionSet.spliterator(), false)
        .filter(transaction -> transaction instanceof ParentTxn)
        .filter(transaction -> transaction.getClearedStatus() != AbstractTxn.ClearedStatus.CLEARED)
        .filter(transaction -> transaction.getValue() < 0)
        .filter(
            transaction ->
                !transaction.doesParameterExist(
                    MoneydanceMapper.ORIGINAL_TRANSACTION_ID_PARAMETER_KEY))
        .map(transaction -> (ParentTxn) transaction)
        .map(moneydanceMapper::toTransaction)
        .collect(Collectors.toList());
  }

  @Override
  public void setEnvelopeOfTransaction(Envelope envelope, Transaction transaction) {
    ParentTxn parentTxn = getParentTxn(transaction.getId());
    Account envelopeAccount = accountBook.getAccountByUUID(envelope.getId());

    ParentTxn newParentTxn =
        TransactionUtils.createParentAndSplitTxn(
            accountBook,
            parentTxn.getDateInt(),
            parentTxn.getAccount(),
            envelopeAccount,
            "Transfer to cover ["
                + parentTxn.getDescription()
                + "] under ["
                + TransactionUtils.getCategoryName(parentTxn)
                + "]",
            parentTxn.getValue());

    parentTxn.setClearedStatus(AbstractTxn.ClearedStatus.RECONCILING);
    parentTxn.setParameter(
        MoneydanceMapper.ENVELOPE_TRANSACTION_ID_PARAMETER_KEY, newParentTxn.getUUID());
    parentTxn.syncItem();

    accountBook.refreshAccountBalances();
  }

  @Override
  public void clearEnvelopeOfTransaction(Transaction transaction) {
    ParentTxn parentTxn = getParentTxn(transaction.getId());

    String envelopeTransactionId =
        parentTxn.getParameter(MoneydanceMapper.ENVELOPE_TRANSACTION_ID_PARAMETER_KEY);
    if (StringUtils.isEmpty(envelopeTransactionId)) {
      return;
    }

    ParentTxn envelopeParentTxn = getParentTxn(envelopeTransactionId);
    envelopeParentTxn.deleteItem();

    if (parentTxn.getClearedStatus() == AbstractTxn.ClearedStatus.RECONCILING) {
      parentTxn.setClearedStatus(AbstractTxn.ClearedStatus.UNRECONCILED);
    }
    parentTxn.removeParameter(MoneydanceMapper.ENVELOPE_TRANSACTION_ID_PARAMETER_KEY);
    parentTxn.syncItem();

    accountBook.refreshAccountBalances();
  }

  @Override
  public List<EnvelopeReport> getEnvelopeReportList(
      String accountId, LocalDate start, LocalDate end) {
    return getEnvelopeList(accountId).stream()
        .map(envelope -> getEnvelopeReport(envelope, start, end))
        .collect(Collectors.toList());
  }

  @Override
  public List<EnvelopeBudget> getEnvelopeBudgetList(String accountId) {
    Account account = accountBook.getAccountByUUID(accountId);
    CurrencyUnit accountCurrency = CurrencyUnit.of(account.getCurrencyType().getIDString());

    return accountBook.getAccountByUUID(accountId).getSubAccounts().stream()
        .filter(
            envelope -> {
              CurrencyUnit envelopeCurrency =
                  CurrencyUnit.of(envelope.getCurrencyType().getIDString());
              return envelopeCurrency.equals(accountCurrency);
            })
        .map(MoneydanceMapper::toEnvelopeBudget)
        .collect(Collectors.toList());
  }

  @Override
  public void saveEnvelopeBudget(EnvelopeBudget envelopeBudget) {
    Account envelope = accountBook.getAccountByUUID(envelopeBudget.getId());

    envelope.setParameter(
        MoneydanceMapper.ENVELOPE_BUDGET_TYPE_PARAMETER_KEY, envelopeBudget.getType().name());
    if (envelopeBudget.getType() == BudgetType.PERCENTAGE) {
      envelope.setParameter(
          MoneydanceMapper.ENVELOPE_BUDGET_PERCENTAGE_PARAMETER_KEY,
          envelopeBudget.getPercentage());
      envelope.removeParameter(MoneydanceMapper.ENVELOPE_BUDGET_AMOUNT_PARAMETER_KEY);
    } else if (envelopeBudget.getType() == BudgetType.AMOUNT) {
      envelope.setParameter(
          MoneydanceMapper.ENVELOPE_BUDGET_AMOUNT_PARAMETER_KEY,
          envelopeBudget.getBudget().toString());
      envelope.removeParameter(MoneydanceMapper.ENVELOPE_BUDGET_PERCENTAGE_PARAMETER_KEY);
    } else {
      envelope.removeParameter(MoneydanceMapper.ENVELOPE_BUDGET_PERCENTAGE_PARAMETER_KEY);
      envelope.removeParameter(MoneydanceMapper.ENVELOPE_BUDGET_AMOUNT_PARAMETER_KEY);
    }
    envelope.syncItem();
  }

  @Override
  public void assignBudgetPlan(BudgetPlan budgetPlan) {
    for (BudgetPlanItem item : budgetPlan.getItemList()) {
      if (item.getAmount().isNegativeOrZero()) {
        continue;
      }

      Account envelope = accountBook.getAccountByUUID(item.getEnvelopeBudget().getId());
      TransactionUtils.createParentAndSplitTxn(
          accountBook,
          LocalDateUtils.nowAsInt(),
          envelope.getParentAccount(),
          envelope,
          "Budget assignment for [" + envelope.getAccountName() + "]",
          item.getAmount().getAmountMinorLong());
    }

    accountBook.refreshAccountBalances();
  }

  private EnvelopeReport getEnvelopeReport(Envelope envelope, LocalDate start, LocalDate end) {
    Account envelopeAccount = accountBook.getAccountByUUID(envelope.getId());

    TxnSet transactionSet =
        accountBook
            .getTransactionSet()
            .getTransactions(
                TxnUtil.getSearch(envelopeAccount, LocalDateUtils.createDateRange(start, end)));

    CurrencyUnit currencyUnit = CurrencyUnit.of(envelopeAccount.getCurrencyType().getIDString());
    Money budget = Money.zero(currencyUnit);
    Money adjustments = Money.zero(currencyUnit);
    Money spend = Money.zero(currencyUnit);
    for (AbstractTxn transaction : transactionSet) {
      Money value =
          MoneydanceMapper.toMoney(
              transaction.getValue(), transaction.getAccount().getCurrencyType());
      if (transaction.getTags().containsValue(ENVELOPE_ADJUSTMENT_TRANSACTION_TAG)) {
        adjustments = adjustments.plus(value);
      } else if (transaction.getValue() >= 0) {
        budget = budget.plus(value);
      } else {
        spend = spend.plus(value);
      }
    }

    return new EnvelopeReport(
        envelope.getId(),
        envelope.getName(),
        getBalanceAtStartOf(envelopeAccount, start),
        envelope.getBalance(),
        budget,
        adjustments,
        spend);
  }

  private ParentTxn getParentTxn(String transactionId) {
    AbstractTxn abstractTxn = accountBook.getTransactionSet().getTxnByID(transactionId);
    if (!(abstractTxn instanceof ParentTxn)) {
      throw new IllegalArgumentException(
          "The transaction [" + abstractTxn + "] must be of type ParentTxn");
    }

    return (ParentTxn) abstractTxn;
  }

  private Money getBalanceAtStartOf(Account account, LocalDate date) {
    long balance =
        AccountUtil.getBalanceAsOfDate(
            accountBook, account, LocalDateUtils.convertLocalDateToInt(date.minusDays(1)));
    return MoneydanceMapper.toMoney(balance, account.getCurrencyType());
  }
}
