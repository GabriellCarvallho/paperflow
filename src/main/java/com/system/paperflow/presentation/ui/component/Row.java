package com.system.paperflow.presentation.ui.component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.system.paperflow.presentation.ui.Alignment;
import com.system.paperflow.presentation.ui.View;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Consumer;

public class Row implements View {

    private final JPanel panel;
    private View[] childComponents = new View[0];
    private Alignment alignment = Alignment.LEFT;

    private Row() {
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        this.panel.setBackground(Color.decode("#F8F9FA"));
    }

    public static Row create() {
        return new Row();
    }

    public Row withAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public Row modifier(Consumer<JPanel> modifier) {
        modifier.accept(this.panel);
        return this;
    }

    public Row children(View... components) {
        this.childComponents = components;
        return this;
    }

    @Override
    public JComponent build() {
        this.panel.removeAll();

        if (alignment == Alignment.CENTER || alignment == Alignment.RIGHT) {
            this.panel.add(Box.createHorizontalGlue());
        }

        for (View component : childComponents) {
            JComponent jComp = component.build();
            jComp.setAlignmentY(Component.CENTER_ALIGNMENT); 
            this.panel.add(jComp);
        }

        if (alignment == Alignment.CENTER || alignment == Alignment.LEFT) {
            this.panel.add(Box.createHorizontalGlue());
        }

        return panel;
    }
}
