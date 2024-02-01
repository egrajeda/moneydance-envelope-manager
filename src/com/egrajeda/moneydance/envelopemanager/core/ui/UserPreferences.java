package com.egrajeda.moneydance.envelopemanager.core.ui;

public interface UserPreferences {
  String getDateFormat();

  void setSelectedAccountId(String accountId);

  String getSelectedAccountId();

  void setMainAccountId(String accountId);

  String getMainAccountId();

  void setSavingsEnvelopeId(String envelopeId);

  String getSavingsEnvelopeId();
}
