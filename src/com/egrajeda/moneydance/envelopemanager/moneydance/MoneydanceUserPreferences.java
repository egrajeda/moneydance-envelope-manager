package com.egrajeda.moneydance.envelopemanager.moneydance;

import com.egrajeda.moneydance.envelopemanager.core.ui.UserPreferences;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;

public class MoneydanceUserPreferences implements UserPreferences {
  private final String dateFormat;

  public MoneydanceUserPreferences(FeatureModuleContext featureModuleContext) {
    if (!(featureModuleContext instanceof Main)) {
      throw new IllegalArgumentException();
    }

    Main main = (Main) featureModuleContext;
    this.dateFormat = main.getPreferences().getShortDateFormat();
  }

  @Override
  public String getDateFormat() {
    return dateFormat;
  }
}
