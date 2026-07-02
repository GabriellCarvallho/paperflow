package com.system.paperflow.presentation.ui.component;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class Grid implements View {

    private final JPanel panel;
    private final int columns;

    private Grid(int columns) {
        this.columns = columns;
        this.panel = new JPanel();
        this.panel.setBackground(Theme.Colors.BACKGROUND);
        this.panel.setLayout(new GridLayout(0, columns, Theme.Layout.GRID_GAP, Theme.Layout.GRID_GAP));
    }

    public static Grid columns(int columns) {
        return new Grid(columns);
    }

    public Grid children(View... components) {
        this.panel.removeAll();

        for (View component : components) {
            add(component);
        }

        return this;
    }

    public Grid add(View component) {
        this.panel.add(component.build());
        return this;
    }

    @Override
    public JComponent build() {
        return panel;
    }
}
