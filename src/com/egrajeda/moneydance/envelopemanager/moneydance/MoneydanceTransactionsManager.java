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
  private static final String ENVELOPE_TRANSACTION_CHECK_NUMBER = "Ignore";
  private static final String ENVELOPE_ADJUSTMENT_TRANSACTION_TAG = "overspend";

  private final AccountBook accountBook;
  private final MoneydanceMapper moneydanceMapper;

  public MoneydanceTransactionsManager(FeatureModuleContext featureModuleContext) {
    this.accountBook = featureModuleContext.getCurrentAccountBook();
    this.moneydanceMapper = new MoneydanceMapper(accountBook);
  }

  @Override
  public com.egrajeda.moneydance.envelopemanager.core.model.Account getAccount(String accountId) {
    return MoneydanceMapper.toAccount(accountBook.getAccountByUUID(accountId));
  }

  @Override
  public List<com.egrajeda.moneydance.envelopemanager.core.model.Account> getAccountList() {
    return accountBook.getRootAccount().getSubAccounts().stream()
        .filter(account -> account.getAccountType() == Account.AccountType.BANK)
        .map(MoneydanceMapper::toAccount)
        .collect(Collectors.toList());
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

    ParentTxn newParentTxn = new ParentTxn(accountBook);
    newParentTxn.setDateInt(parentTxn.getDateInt());
    newParentTxn.setTaxDateInt(parentTxn.getDateInt());
    newParentTxn.setCheckNumber(ENVELOPE_TRANSACTION_CHECK_NUMBER);
    newParentTxn.setAccount(parentTxn.getAccount());
    newParentTxn.setCheckNumber("Ignore");
    newParentTxn.setDescription(
        "Transfer to cover ["
            + parentTxn.getDescription()
            + "] under ["
            + TransactionUtils.getCategoryName(parentTxn)
            + "]");
    newParentTxn.setClearedStatus(AbstractTxn.ClearedStatus.RECONCILING);
    newParentTxn.setParameter(
        MoneydanceMapper.ORIGINAL_TRANSACTION_ID_PARAMETER_KEY, parentTxn.getUUID());

    SplitTxn newSplitTxn = new SplitTxn(newParentTxn);
    newSplitTxn.setAccount(envelopeAccount);
    newSplitTxn.setCheckNumber(ENVELOPE_TRANSACTION_CHECK_NUMBER);
    newSplitTxn.setAmount(parentTxn.getValue(), parentTxn.getValue());
    newSplitTxn.setDescription(newParentTxn.getDescription());
    newSplitTxn.setClearedStatus(AbstractTxn.ClearedStatus.RECONCILING);

    newParentTxn.addSplit(newSplitTxn);
    newParentTxn.syncItem();

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

    return getEnvelopeList(accountId).stream()
        .filter(envelope -> envelope.getBalance().getCurrencyUnit().equals(accountCurrency))
        .map(
            envelope ->
                new EnvelopeBudget(
                    envelope.getId(),
                    envelope.getName(),
                    BudgetType.PERCENTAGE,
                    0.05f,
                    null))
        .collect(Collectors.toList());
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
