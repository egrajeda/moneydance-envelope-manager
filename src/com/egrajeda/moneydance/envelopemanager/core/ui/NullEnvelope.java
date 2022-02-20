package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.Envelope;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class NullEnvelope extends Envelope {
  public NullEnvelope() {
    super("None", Money.zero(CurrencyUnit.EUR));
  }
}
