package com.system.paperflow.presentation.ui.component;

import javax.swing.*;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

import java.awt.Color;
import java.awt.Font;

public class Text implements View {

    private final JLabel label;

    private Text(String content) {
        this.label = new JLabel(content);
        this.label.setFont(new Font(Theme.FONT_FAMILY, Font.PLAIN, 14));
        this.label.setForeground(Theme.Colors.TEXT);
    }

    public static Text create(String text) { 
        return new Text(text); 
    }

    public static Text title(String text) {
        return create(text).fontSize(28).bold();
    }

    public static Text sectionTitle(String text) {
        return create(text).fontSize(18).bold();
    }

    public static Text metric(String text) {
        return create(text).fontSize(30).bold();
    }

    public static Text subtitle(String text) {
        return create(text).muted();
    }

    public static Text body(String text) {
        return create(text);
    }

    public static Text caption(String text) {
        return create(text).fontSize(13).muted();
    }

    public Text fontSize(int size) {
        this.label.setFont(new Font(Theme.FONT_FAMILY, this.label.getFont().getStyle(), size));
        return this;
    }

    public Text bold() {
        this.label.setFont(this.label.getFont().deriveFont(Font.BOLD));
        return this;
    }

    public Text muted() {
        this.label.setForeground(Theme.Colors.TEXT_MUTED);
        return this;
    }

    public Text color(Color color) {
        this.label.setForeground(color);
        return this;
    }

    @Override
    public JComponent build() { 
        return label; 
    }
    
}
