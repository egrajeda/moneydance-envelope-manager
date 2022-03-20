package com.egrajeda.moneydance.envelopemanager.moneydance;

import com.infinitekind.moneydance.model.*;

public class TransactionUtils {
  private static final String ENVELOPE_TRANSACTION_CHECK_NUMBER = "Ignore";

  public static ParentTxn createParentAndSplitTxn(
      AccountBook accountBook,
      int dateAsInt,
      Account parentAccount,
      Account splitAccount,
      String description,
      long amount) {
    ParentTxn newParentTxn = new ParentTxn(accountBook);
    newParentTxn.setDateInt(dateAsInt);
    newParentTxn.setTaxDateInt(dateAsInt);
    newParentTxn.setAccount(parentAccount);
    newParentTxn.setCheckNumber(ENVELOPE_TRANSACTION_CHECK_NUMBER);
    newParentTxn.setDescription(description);
    newParentTxn.setClearedStatus(AbstractTxn.ClearedStatus.RECONCILING);

    SplitTxn newSplitTxn = new SplitTxn(newParentTxn);
    newSplitTxn.setAccount(splitAccount);
    newSplitTxn.setCheckNumber(ENVELOPE_TRANSACTION_CHECK_NUMBER);
    newSplitTxn.setAmount(amount, amount);
    newSplitTxn.setDescription(newParentTxn.getDescription());
    newSplitTxn.setClearedStatus(AbstractTxn.ClearedStatus.RECONCILING);

    newParentTxn.addSplit(newSplitTxn);
    newParentTxn.syncItem();

    return newParentTxn;
  }

  public static Account getSplitAccount(ParentTxn parentTxn) {
    SplitTxn splitTxn = parentTxn.getSplit(0);
    return splitTxn == null ? null : parentTxn.getSplit(0).getAccount();
  }

  public static String getCategoryName(ParentTxn parentTxn) {
    Account splitAccount = getSplitAccount(parentTxn);
    return splitAccount == null ? "" : splitAccount.getFullAccountName();
  }
}
