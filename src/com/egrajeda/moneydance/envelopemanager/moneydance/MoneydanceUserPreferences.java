package com.egrajeda.moneydance.envelopemanager.moneydance;

import com.egrajeda.moneydance.envelopemanager.core.ui.UserPreferences;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;

public class MoneydanceUserPreferences implements UserPreferences {
  private static final String SELECTED_ACCOUNT_ID_SETTING_KEY = "envelopeManager:selectedAccountId";
  private static final String MAIN_ACCOUNT_ID_SETTINGS_KEY = "envelopeManager:mainAccountId";
  private static final String SAVINGS_ENVELOPE_ID_SETTINGS_KEY =
      "envelopeManager:savingsEnvelopeId";
  private final com.moneydance.apps.md.controller.UserPreferences userPreferences;

  public MoneydanceUserPreferences(FeatureModuleContext featureModuleContext) {
    if (!(featureModuleContext instanceof Main)) {
      throw new IllegalArgumentException();
    }

    userPreferences = ((Main) featureModuleContext).getPreferences();
  }

  @Override
  public String getDateFormat() {
    return userPreferences.getShortDateFormat();
  }

  @Override
  public void setSelectedAccountId(String accountId) {
    userPreferences.setSetting(SELECTED_ACCOUNT_ID_SETTING_KEY, accountId);
  }

  @Override
  public String getSelectedAccountId() {
    return userPreferences.getSetting(SELECTED_ACCOUNT_ID_SETTING_KEY);
  }

  @Override
  public void setMainAccountId(String accountId) {
    userPreferences.setSetting(MAIN_ACCOUNT_ID_SETTINGS_KEY, accountId);
  }

  @Override
  public String getMainAccountId() {
    return userPreferences.getSetting(MAIN_ACCOUNT_ID_SETTINGS_KEY);
  }

  @Override
  public void setSavingsEnvelopeId(String envelopeId) {
    userPreferences.setSetting(SAVINGS_ENVELOPE_ID_SETTINGS_KEY, envelopeId);
  }

  @Override
  public String getSavingsEnvelopeId() {
    return userPreferences.getSetting(SAVINGS_ENVELOPE_ID_SETTINGS_KEY);
  }
}
