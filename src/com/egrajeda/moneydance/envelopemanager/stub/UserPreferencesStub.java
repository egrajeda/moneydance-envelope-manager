package com.egrajeda.moneydance.envelopemanager.stub;

import com.egrajeda.moneydance.envelopemanager.core.ui.UserPreferences;

public class UserPreferencesStub implements UserPreferences {
  @Override
  public String getDateFormat() {
    return "dd/MM/yyyy";
  }
}
