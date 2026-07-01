package com.system.paperflow.presentation.ui.component;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class StatusMessage implements View {

    private final JLabel label;

    private StatusMessage() {
        this.label = new JLabel(" ");
        this.label.setFont(new Font(Theme.FONT_FAMILY, Font.PLAIN, 13));
        this.label.setForeground(Theme.Colors.ERROR);
    }

    public static StatusMessage create() {
        return new StatusMessage();
    }

    public void error(String message) {
        this.label.setForeground(Theme.Colors.ERROR);
        this.label.setText(message);
    }

    public void success(String message) {
        this.label.setForeground(Theme.Colors.SUCCESS);
        this.label.setText(message);
    }

    public void clear() {
        this.label.setText(" ");
    }

    @Override
    public JComponent build() {
        return label;
    }
}
