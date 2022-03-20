package com.egrajeda.moneydance.envelopemanager.core.ui;

public interface UserPreferences {
  String getDateFormat();

  void setSelectedAccountId(String accountId);

  String getSelectedAccountId();
}
