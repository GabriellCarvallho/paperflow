package com.system.paperflow.presentation.ui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;

import com.system.paperflow.presentation.ui.View;

public class Button implements View {

    private static final String DEFAULT_FONT = "Segoe UI";

    private final JButton button;

    private Button(String text) {
        this.button = new JButton(text);
        this.button.setFont(new Font(DEFAULT_FONT, Font.BOLD, 14));
        
        this.button.setBackground(Color.decode("#3182CE"));
        this.button.setForeground(Color.WHITE);
        
        this.button.setFocusPainted(false);
        this.button.setBorderPainted(true);
        this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        this.button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));   
    }

    public Button onClick(Runnable action) {
        this.button.addActionListener(e -> action.run());
        return this;
    }

    public static Button create(String text) {
        return new Button(text);
    }

    @Override
    public JComponent build() {
        return button;
    }
    
}
