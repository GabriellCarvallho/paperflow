package com.system.paperflow.presentation.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.system.paperflow.presentation.ui.Alignment;
import com.system.paperflow.presentation.ui.View;

public class Column implements View {

    private final JPanel panel;
    private Alignment alignment = Alignment.LEFT;

    private Column() {
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.panel.setBackground(Color.decode("#F8F9FA"));
    }

    public static Column create() {
        return new Column();
    }

    public Column modifier(Consumer<JPanel> modifier) {
        modifier.accept(this.panel);
        return this;
    }

    public Column withAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public Column children(View... components) {
        for (View component : components) {
            JComponent jComp = component.build();
            
            switch (alignment) {
                case LEFT -> jComp.setAlignmentX(Component.LEFT_ALIGNMENT);
                case CENTER -> jComp.setAlignmentX(Component.CENTER_ALIGNMENT);
                case RIGHT -> jComp.setAlignmentX(Component.RIGHT_ALIGNMENT);
            }

            this.panel.add(jComp);
        }
        return this;
    }

    @Override
    public JComponent build() {
        return panel;
    }
    
}
