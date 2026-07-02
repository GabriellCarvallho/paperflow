package com.system.paperflow.presentation.ui.component;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import com.system.paperflow.presentation.ui.View;

public class ClickableCard implements View {

    private final Card card;
    private Runnable action = () -> { };

    private ClickableCard() {
        this.card = Card.create();
    }

    public static ClickableCard create() {
        return new ClickableCard();
    }

    public ClickableCard onClick(Runnable action) {
        this.action = action == null ? () -> { } : action;
        return this;
    }

    public ClickableCard withMinHeight(int height) {
        card.withMinHeight(height);
        return this;
    }

    public ClickableCard gap(int gap) {
        card.gap(gap);
        return this;
    }

    public ClickableCard children(View... components) {
        card.children(components);
        return this;
    }

    public ClickableCard add(View component) {
        card.add(component);
        return this;
    }

    @Override
    public JComponent build() {
        JComponent component = card.build();
        component.setCursor(new Cursor(Cursor.HAND_CURSOR));
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                action.run();
            }
        });

        return component;
    }
}
