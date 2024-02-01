package com.egrajeda.moneydance.envelopemanager.core.ui;

import java.awt.*;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LabelWithComboBox<E> extends JPanel {
  private final JComboBox<E> comboBox;

  public LabelWithComboBox(String text, E[] items) {
    this(text, new DefaultComboBoxModel<>(items));
  }

  public LabelWithComboBox(String text, ComboBoxModel<E> model) {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel label = new JLabel(text);
    label.setMaximumSize(new Dimension(125, label.getMaximumSize().height));
    label.setHorizontalAlignment(SwingConstants.RIGHT);
    add(label);

    add(Box.createRigidArea(new Dimension(5, 0)));

    comboBox = new JComboBox<>(model);
    comboBox.setMaximumSize(new Dimension(300, comboBox.getPreferredSize().height));
    add(comboBox);
  }

  public void setSelectedItem(E item) {
    comboBox.setSelectedItem(item);
  }

  public E getSelectedItem() {
    return (E) comboBox.getSelectedItem();
  }

  public void addItemListener(ItemListener listener) {
    comboBox.addItemListener(listener);
  }
}
