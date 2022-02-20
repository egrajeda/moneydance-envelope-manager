package com.egrajeda.moneydance.envelopemanager.moneydance;

import com.infinitekind.moneydance.model.AbstractTxn;
import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.ParentTxn;
import com.infinitekind.moneydance.model.SplitTxn;

public class TransactionUtils {
  public static Account getSplitAccount(ParentTxn parentTxn) {
    SplitTxn splitTxn = parentTxn.getSplit(0);
    return splitTxn == null ? null : parentTxn.getSplit(0).getAccount();
  }

  public static String getCategoryName(ParentTxn parentTxn) {
    Account splitAccount = getSplitAccount(parentTxn);
    return splitAccount == null ? "" : splitAccount.getFullAccountName();
  }

  public static boolean isBetweenEnvelopes(AbstractTxn abstractTxn) {
    Account account = abstractTxn.getParentTxn().getAccount();
    Account splitAccount = getSplitAccount(abstractTxn.getParentTxn());
    return splitAccount != null
        && account.getParentAccount().getUUID().equals(splitAccount.getParentAccount().getUUID());
  }
}
