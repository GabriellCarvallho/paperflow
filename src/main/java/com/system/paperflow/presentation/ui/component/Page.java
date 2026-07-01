package com.system.paperflow.presentation.ui.component;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class Page implements View {

    private final View child;
    private final JPanel panel;

    private Page(View child) {
        this.child = child;
        this.panel = new JPanel(new BorderLayout());
        this.panel.setBackground(Theme.Colors.BACKGROUND);
        this.panel.setBorder(BorderFactory.createEmptyBorder(
            Theme.Layout.SCREEN_PADDING,
            Theme.Layout.SCREEN_PADDING,
            Theme.Layout.SCREEN_PADDING,
            Theme.Layout.SCREEN_PADDING
        ));
    }

    public static Page create(View child) {
        return new Page(child);
    }

    @Override
    public JComponent build() {
        this.panel.removeAll();
        this.panel.add(child.build(), BorderLayout.NORTH);
        return panel;
    }
}
