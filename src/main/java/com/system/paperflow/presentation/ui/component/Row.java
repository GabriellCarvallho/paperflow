package com.system.paperflow.presentation.ui.component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.system.paperflow.presentation.ui.Alignment;
import com.system.paperflow.presentation.ui.View;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Row implements View {

    private final JPanel panel;
    private final List<View> childComponents = new ArrayList<>();
    private Alignment alignment = Alignment.LEFT;
    private int gap = 0;
    private boolean spaceBetween = false;

    private Row() {
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        this.panel.setOpaque(false);
    }

    public static Row create() {
        return new Row();
    }

    public Row withAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public Row left() {
        return withAlignment(Alignment.LEFT);
    }

    public Row center() {
        return withAlignment(Alignment.CENTER);
    }

    public Row right() {
        return withAlignment(Alignment.RIGHT);
    }

    public Row gap(int gap) {
        this.gap = gap;
        return this;
    }

    public Row spaceBetween() {
        this.spaceBetween = true;
        return this;
    }

    public Row unsafeModifier(Consumer<JPanel> modifier) {
        modifier.accept(this.panel);
        return this;
    }

    public Row withBackground(Color color) {
        this.panel.setOpaque(true);
        this.panel.setBackground(color);
        return this;
    }

    public Row transparent() {
        this.panel.setOpaque(false);
        return this;
    }

    public Row children(View... components) {
        this.childComponents.clear();

        for (View component : components) {
            add(component);
        }

        return this;
    }

    public Row add(View component) {
        this.childComponents.add(component);
        return this;
    }

    @Override
    public JComponent build() {
        this.panel.removeAll();

        if (alignment == Alignment.CENTER || alignment == Alignment.RIGHT) {
            this.panel.add(Box.createHorizontalGlue());
        }

        for (int index = 0; index < childComponents.size(); index++) {
            View component = childComponents.get(index);
            JComponent jComp = component.build();
            jComp.setAlignmentY(Component.CENTER_ALIGNMENT); 

            if (spaceBetween) {
                jComp.setMaximumSize(jComp.getPreferredSize());
            }

            this.panel.add(jComp);

            if (gap > 0 && index < childComponents.size() - 1) {
                this.panel.add(Spacer.horizontal(gap).build());
            }

            if (spaceBetween && gap == 0 && index < childComponents.size() - 1) {
                this.panel.add(Box.createHorizontalGlue());
            }
        }

        if (!spaceBetween && (alignment == Alignment.CENTER || alignment == Alignment.LEFT)) {
            this.panel.add(Box.createHorizontalGlue());
        }

        return panel;
    }
}
