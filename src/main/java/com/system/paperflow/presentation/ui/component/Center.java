package com.system.paperflow.presentation.ui.component;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class Center implements View {

    private final View child;
    private final JPanel panel;

    private Center(View child) {
        this.child = child;
        this.panel = new JPanel(new GridBagLayout());
        this.panel.setBackground(Theme.Colors.BACKGROUND);
        this.panel.setBorder(BorderFactory.createEmptyBorder(
            Theme.Layout.SCREEN_PADDING,
            Theme.Layout.SCREEN_PADDING,
            Theme.Layout.SCREEN_PADDING,
            Theme.Layout.SCREEN_PADDING
        ));
    }

    public static Center create(View child) {
        return new Center(child);
    }

    public Center withBackground(Color color) {
        this.panel.setBackground(color);
        return this;
    }

    public Center withPadding(int padding) {
        this.panel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        return this;
    }

    @Override
    public JComponent build() {
        this.panel.removeAll();
        this.panel.add(child.build());
        return panel;
    }
}
