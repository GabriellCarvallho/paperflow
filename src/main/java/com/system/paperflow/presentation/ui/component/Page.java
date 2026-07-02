package com.system.paperflow.presentation.ui.component;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class Page implements View {

    private View child;
    private final JPanel panel;

    private Page(View child) {
        this.child = child;
        this.panel = new JPanel(new BorderLayout());
        this.panel.setBackground(Theme.Colors.BACKGROUND);
    }

    public static Page create(View child) {
        return new Page(child);
    }

    public static Page create() {
        return new Page(null);
    }

    public Page add(View child) {
        this.child = child;
        return this;
    }

    @Override
    public JComponent build() {
        this.panel.removeAll();

        if (child != null) {
            JPanel content = new JPanel(new BorderLayout());
            content.setOpaque(false);
            content.setBorder(BorderFactory.createEmptyBorder(
                Theme.Layout.SCREEN_PADDING,
                Theme.Layout.SCREEN_PADDING,
                Theme.Layout.SCREEN_PADDING,
                Theme.Layout.SCREEN_PADDING
            ));
            content.add(child.build(), BorderLayout.NORTH);

            JScrollPane scrollPane = new JScrollPane(content);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(24);

            this.panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }
}
