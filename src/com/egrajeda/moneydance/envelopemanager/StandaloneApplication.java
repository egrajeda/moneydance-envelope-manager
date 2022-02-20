package com.egrajeda.moneydance.envelopemanager;

import com.egrajeda.moneydance.envelopemanager.core.ui.EnvelopeManagerWindow;
import com.egrajeda.moneydance.envelopemanager.stub.TransactionsManagerStub;
import com.egrajeda.moneydance.envelopemanager.stub.UserPreferencesStub;

public class StandaloneApplication {
  public static void main(String[] args) {
    EnvelopeManagerWindow window =
        new EnvelopeManagerWindow(new TransactionsManagerStub(), new UserPreferencesStub());
    window.setVisible(true);
  }
}
