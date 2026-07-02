package com.system.paperflow.presentation.ui.component;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class Card implements View {

    private final JPanel panel;
    private int gap = 0;

    private Card() {
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.panel.setBackground(Theme.Colors.SURFACE);
        this.panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.Colors.BORDER, 1),
            BorderFactory.createEmptyBorder(
                Theme.Layout.CARD_PADDING,
                Theme.Layout.CARD_PADDING,
                Theme.Layout.CARD_PADDING,
                Theme.Layout.CARD_PADDING
            )
        ));
    }

    public static Card create() {
        return new Card();
    }

    public Card withMinHeight(int height) {
        this.panel.setMinimumSize(new Dimension(0, height));
        this.panel.setPreferredSize(new Dimension(0, height));
        return this;
    }

    public Card gap(int gap) {
        this.gap = gap;
        return this;
    }

    public Card children(View... components) {
        this.panel.removeAll();

        for (View component : components) {
            add(component);
        }

        return this;
    }

    public Card add(View component) {
        if (gap > 0 && this.panel.getComponentCount() > 0) {
            this.panel.add(Spacer.vertical(gap).build());
        }

        JComponent child = component.build();
        child.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.panel.add(child);
        return this;
    }

    @Override
    public JComponent build() {
        return panel;
    }
}
