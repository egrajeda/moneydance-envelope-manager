package com.egrajeda.moneydance.envelopemanager.moneydance;

import com.egrajeda.moneydance.envelopemanager.core.ui.UserPreferences;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;

public class MoneydanceUserPreferences implements UserPreferences {
  private static final String SELECTED_ACCOUNT_ID_SETTING_KEY = "envelopeManager:selectedAccountId";
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
  public String getSelectedAccountId() {
    return userPreferences.getSetting(SELECTED_ACCOUNT_ID_SETTING_KEY);
  }

  @Override
  public void setSelectedAccountId(String accountId) {
    userPreferences.setSetting(SELECTED_ACCOUNT_ID_SETTING_KEY, accountId);
  }
}
