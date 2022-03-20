package com.egrajeda.moneydance.envelopemanager.core.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TableTopPanel extends JPanel {
  private static final Color PANEL_BACKGROUND_COLOR = new Color(211, 236, 248);

  TableTopPanel() {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setBackground(PANEL_BACKGROUND_COLOR);
  }
}
