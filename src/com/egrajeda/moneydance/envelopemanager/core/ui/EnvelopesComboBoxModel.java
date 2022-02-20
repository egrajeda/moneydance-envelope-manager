package com.egrajeda.moneydance.envelopemanager.core.ui;

import com.egrajeda.moneydance.envelopemanager.core.model.Envelope;

import javax.swing.*;
import java.util.List;

public class EnvelopesComboBoxModel extends DefaultComboBoxModel<Envelope> {

  public void setEnvelopeList(List<Envelope> envelopeList) {
    removeAllElements();
    addAll(envelopeList);
  }
}
