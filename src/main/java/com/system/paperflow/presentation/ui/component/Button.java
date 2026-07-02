package com.system.paperflow.presentation.ui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class Button implements View {

    private final JButton button;

    private Button(String text) {
        this.button = new JButton(text);
        this.button.setFont(new Font(Theme.FONT_FAMILY, Font.BOLD, 14));
        
        this.button.setBackground(Theme.Colors.PRIMARY);
        this.button.setForeground(Color.WHITE);
        
        this.button.setFocusPainted(false);
        this.button.setBorderPainted(true);
        this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        this.button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));   
    }

    private Button asSecondary() {
        this.button.setBackground(Theme.Colors.SURFACE);
        this.button.setForeground(Theme.Colors.PRIMARY);
        this.button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.Colors.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        return this;
    }

    public Button onClick(Runnable action) {
        this.button.addActionListener(e -> action.run());
        return this;
    }

    public Button fullWidth() {
        return withWidthPercent(100);
    }

    public Button withWidthPercent(int percent) {
        Dimension currentSize = this.button.getPreferredSize();
        Dimension size = new Dimension(Theme.Layout.widthFromPercent(percent), currentSize.height);

        this.button.setPreferredSize(size);
        this.button.setMinimumSize(size);
        this.button.setMaximumSize(size);
        return this;
    }

    public static Button create(String text) {
        return new Button(text);
    }

    public static Button primary(String text) {
        return create(text);
    }

    public static Button secondary(String text) {
        return create(text).asSecondary();
    }

    @Override
    public JComponent build() {
        return button;
    }
    
}
