package com.moneydance.modules.features.envelopemanager;

import com.egrajeda.moneydance.envelopemanager.core.ui.EnvelopeManagerWindow;
import com.egrajeda.moneydance.envelopemanager.core.model.TransactionsManager;
import com.egrajeda.moneydance.envelopemanager.core.ui.UserPreferences;
import com.egrajeda.moneydance.envelopemanager.moneydance.MoneydanceTransactionsManager;
import com.egrajeda.moneydance.envelopemanager.moneydance.MoneydanceUserPreferences;
import com.moneydance.apps.md.controller.FeatureModule;
import com.moneydance.apps.md.controller.FeatureModuleContext;

import java.awt.*;
import java.io.ByteArrayOutputStream;

public class Main extends FeatureModule {
  private static final String ENVELOPE_MANAGER_FEATURE_URI = "envelopeManager";

  private EnvelopeManagerWindow envelopeManagerWindow = null;

  public void init() {
    FeatureModuleContext context = getContext();
    try {
      context.registerFeature(this, ENVELOPE_MANAGER_FEATURE_URI, getIcon(), getName());
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void cleanup() {
    onCloseExtension();
  }

  public String getName() {
    return "Envelope Manager";
  }

  private Image getIcon() {
    try {
      ClassLoader cl = getClass().getClassLoader();
      java.io.InputStream in =
          cl.getResourceAsStream("/com/moneydance/modules/features/envelopemanager/icon.gif");
      if (in != null) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1000);
        byte[] buf = new byte[256];
        int n;
        while ((n = in.read(buf, 0, buf.length)) >= 0) bout.write(buf, 0, n);
        return Toolkit.getDefaultToolkit().createImage(bout.toByteArray());
      }
    } catch (Throwable e) {
    }
    return null;
  }

  public void invoke(String uri) {
    if (uri.equals(ENVELOPE_MANAGER_FEATURE_URI)) {
      onOpenExtension();
    }
  }

  private synchronized void onOpenExtension() {
    if (envelopeManagerWindow == null) {
      TransactionsManager transactionsManager =
          new MoneydanceTransactionsManager(this.getContext());
      UserPreferences userPreferences = new MoneydanceUserPreferences(this.getContext());
      envelopeManagerWindow = new EnvelopeManagerWindow(transactionsManager, userPreferences);
      envelopeManagerWindow.setWindowClosingListener(this::onCloseExtension);
    }
    envelopeManagerWindow.setVisible(true);
    envelopeManagerWindow.toFront();
    envelopeManagerWindow.requestFocus();
  }

  synchronized void onCloseExtension() {
    if (envelopeManagerWindow != null) {
      envelopeManagerWindow.setVisible(false);
      envelopeManagerWindow.dispose();
      envelopeManagerWindow = null;
      System.gc();
    }
  }
}
