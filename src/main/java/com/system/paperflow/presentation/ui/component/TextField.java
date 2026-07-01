package com.system.paperflow.presentation.ui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.system.paperflow.presentation.ui.View;

public class TextField implements View {

    public enum TextFieldType {
        TEXT, PASSWORD;
    }

    private static final String DEFAULT_FONT = "Segoe UI";
    
    private final JTextField textField;

    private TextField(JTextField textField) {
        this.textField = textField;
        this.textField.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 14));
        this.textField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.textField.setBackground(Color.decode("#FFFFFF"));
        this.textField.setForeground(Color.decode("#1A202C"));
        this.textField.setCaretColor(Color.decode("#1A202C"));
        
        this.textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#E2E8F0"), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        this.textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    }

    public TextField withBackground(Color color) {
        this.textField.setBackground(color);
        return this;
    }

    public TextField withForeground(Color color) {
        this.textField.setForeground(color);
        return this;
    }

    public static TextField create(TextFieldType type) {
        return (type == TextFieldType.PASSWORD) ? new TextField(new JPasswordField()) : new TextField(new JTextField());
    }

    @Override
    public JComponent build() {
        return textField;
    }
}
