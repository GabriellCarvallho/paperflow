package com.system.paperflow.presentation.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.system.paperflow.presentation.ui.Alignment;
import com.system.paperflow.presentation.ui.View;

public class Column implements View {

    private final JPanel panel;
    private Alignment alignment = Alignment.LEFT;
    private int gap = 0;

    private Column() {
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.panel.setOpaque(false);
    }

    public static Column create() {
        return new Column();
    }

    public Column unsafeModifier(Consumer<JPanel> modifier) {
        modifier.accept(this.panel);
        return this;
    }

    public Column withBackground(Color color) {
        this.panel.setOpaque(true);
        this.panel.setBackground(color);
        return this;
    }

    public Column transparent() {
        this.panel.setOpaque(false);
        return this;
    }

    public Column withPadding(int padding) {
        return withPadding(padding, padding, padding, padding);
    }

    public Column withPadding(int top, int left, int bottom, int right) {
        this.panel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        return this;
    }

    public Column withAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public Column left() {
        return withAlignment(Alignment.LEFT);
    }

    public Column center() {
        return withAlignment(Alignment.CENTER);
    }

    public Column right() {
        return withAlignment(Alignment.RIGHT);
    }

    public Column gap(int gap) {
        this.gap = gap;
        return this;
    }

    public Column children(View... components) {
        this.panel.removeAll();

        for (View component : components) {
            add(component);
        }
        return this;
    }

    public Column add(View component) {
        if (gap > 0 && this.panel.getComponentCount() > 0) {
            this.panel.add(Spacer.vertical(gap).build());
        }

        JComponent jComp = component.build();

        switch (alignment) {
            case LEFT -> jComp.setAlignmentX(Component.LEFT_ALIGNMENT);
            case CENTER -> jComp.setAlignmentX(Component.CENTER_ALIGNMENT);
            case RIGHT -> jComp.setAlignmentX(Component.RIGHT_ALIGNMENT);
        }

        this.panel.add(jComp);
        return this;
    }

    @Override
    public JComponent build() {
        return panel;
    }
    
}
