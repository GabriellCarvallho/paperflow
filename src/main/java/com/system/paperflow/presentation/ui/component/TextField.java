package com.system.paperflow.presentation.ui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class TextField implements View {

    public enum TextFieldType {
        TEXT, PASSWORD, DATE;
    }

    private final JTextField textField;

    private TextField(JTextField textField) {
        this.textField = textField;
        this.textField.setFont(new Font(Theme.FONT_FAMILY, Font.PLAIN, 14));

        this.textField.setBackground(Theme.Colors.SURFACE);
        this.textField.setForeground(Theme.Colors.TEXT);
        this.textField.setCaretColor(Theme.Colors.TEXT);
        
        this.textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.Colors.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        applyWidthPercent(100);
    }

    public TextField withBackground(Color color) {
        this.textField.setBackground(color);
        return this;
    }

    public TextField withForeground(Color color) {
        this.textField.setForeground(color);
        return this;
    }

    public TextField withColumns(int columns) {
        this.textField.setColumns(columns);
        return this;
    }

    public TextField withWidthPercent(int percent) {
        applyWidthPercent(percent);
        return this;
    }

    public TextField fullWidth() {
        return withWidthPercent(100);
    }

    public String text() {
        return this.textField.getText();
    }

    public String password() {
        if (this.textField instanceof JPasswordField passwordField) {
            return new String(passwordField.getPassword());
        }

        return this.textField.getText();
    }

    public void clear() {
        this.textField.setText("");
    }

    public static TextField create(TextFieldType type) {
        return switch (type) {
            case PASSWORD -> new TextField(new JPasswordField());
            case DATE -> new TextField(dateField());
            default -> new TextField(new JTextField());
        };
    }

    @Override
    public JComponent build() {
        return textField;
    }

    private void applyWidthPercent(int percent) {
        Dimension size = new Dimension(
            Theme.Layout.widthFromPercent(percent),
            Theme.Layout.CONTROL_HEIGHT
        );

        this.textField.setPreferredSize(size);
        this.textField.setMinimumSize(size);
        this.textField.setMaximumSize(size);
    }

    private static JFormattedTextField dateField() {
        try {
            MaskFormatter formatter = new MaskFormatter("##/##/####");
            formatter.setPlaceholderCharacter('_');
            return new JFormattedTextField(formatter);
        } catch (ParseException exception) {
            throw new IllegalStateException("Nao foi possivel configurar o campo de data.", exception);
        }
    }
}
