package com.egrajeda.moneydance.envelopemanager.moneydance;

import com.egrajeda.moneydance.envelopemanager.core.model.*;
import com.infinitekind.moneydance.model.AbstractTxn;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.ParentTxn;
import com.infinitekind.util.DateUtil;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneydanceMapper {
  public static final String ENVELOPE_TRANSACTION_ID_PARAMETER_KEY = "envelopeTransactionId";
  public static final String ORIGINAL_TRANSACTION_ID_PARAMETER_KEY = "originalTransactionId";

  public static final String ENVELOPE_BUDGET_TYPE_PARAMETER_KEY = "envelopeBudgetType";
  public static final String ENVELOPE_BUDGET_PERCENTAGE_PARAMETER_KEY = "envelopeBudgetPercentage";
  public static final String ENVELOPE_BUDGET_AMOUNT_PARAMETER_KEY = "envelopeBudgetAmount";

  private final AccountBook accountBook;

  public MoneydanceMapper(AccountBook accountBook) {
    this.accountBook = accountBook;
  }

  public static Account toAccount(com.infinitekind.moneydance.model.Account account) {
    return new Account(
        account.getUUID(),
        account.getAccountName(),
        toMoney(account.getBalance(), account.getCurrencyType()));
  }

  public static Envelope toEnvelope(com.infinitekind.moneydance.model.Account account) {
    return new Envelope(
        account.getUUID(),
        account.getAccountName(),
        toMoney(account.getBalance(), account.getCurrencyType()));
  }

  public static EnvelopeBudget toEnvelopeBudget(com.infinitekind.moneydance.model.Account account) {
    String moneyAsString = account.getParameter(ENVELOPE_BUDGET_AMOUNT_PARAMETER_KEY);
    return new EnvelopeBudget(
        account.getUUID(),
        account.getAccountName(),
        BudgetType.valueOf(
            account.getParameter(ENVELOPE_BUDGET_TYPE_PARAMETER_KEY, BudgetType.PERCENTAGE.name())),
        Float.parseFloat(account.getParameter(ENVELOPE_BUDGET_PERCENTAGE_PARAMETER_KEY, "0")),
        moneyAsString == null ? null : Money.parse(moneyAsString));
  }

  public static Money toMoney(long amount, CurrencyType currencyType) {
    CurrencyUnit currencyUnit = CurrencyUnit.of(currencyType.getIDString());
    return Money.of(
        currencyUnit,
        BigDecimal.valueOf(amount).movePointLeft(currencyType.getDecimalPlaces()),
        RoundingMode.HALF_EVEN);
  }

  public Transaction toTransaction(ParentTxn parentTxn) {
    String categoryName = TransactionUtils.getCategoryName(parentTxn);
    Envelope envelope = getEnvelopeIfPresent(parentTxn);
    return new Transaction(
        parentTxn.getUUID(),
        DateUtil.convertIntDateToLong(parentTxn.getDateInt()),
        parentTxn.getCheckNumber(),
        parentTxn.getDescription(),
        parentTxn.getMemo(),
        categoryName,
        toClearedStatus(parentTxn.getClearedStatus()),
        toMoney(parentTxn.getValue(), parentTxn.getAccount().getCurrencyType()),
        envelope);
  }

  public ClearedStatus toClearedStatus(AbstractTxn.ClearedStatus clearedStatus) {
    switch (clearedStatus) {
      case CLEARED:
        return ClearedStatus.CLEARED;
      case RECONCILING:
        return ClearedStatus.RECONCILING;
      case UNRECONCILED:
        return ClearedStatus.UNRECONCILED;
      default:
        throw new IllegalStateException("Unexpected value: " + clearedStatus);
    }
  }

  private Envelope getEnvelopeIfPresent(ParentTxn parentTxn) {
    String envelopeTransactionId = parentTxn.getParameter(ENVELOPE_TRANSACTION_ID_PARAMETER_KEY);
    AbstractTxn envelopeTransaction =
        accountBook.getTransactionSet().getTxnByID(envelopeTransactionId);
    if (!(envelopeTransaction instanceof ParentTxn)) {
      return null;
    }
    com.infinitekind.moneydance.model.Account splitAccount =
        TransactionUtils.getSplitAccount((ParentTxn) envelopeTransaction);
    if (splitAccount == null) {
      return null;
    }
    return toEnvelope(splitAccount);
  }
}
