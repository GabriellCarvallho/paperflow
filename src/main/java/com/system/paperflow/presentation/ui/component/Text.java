package com.system.paperflow.presentation.ui.component;

import javax.swing.*;

import com.system.paperflow.presentation.ui.View;

import java.awt.Color;
import java.awt.Font;

public class Text implements View {

    private static final String DEFAULT_FONT = "Segoe UI";

    private final JLabel label;

    private Text(String content) {
        this.label = new JLabel(content);
        this.label.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 14));
        this.label.setForeground(Color.decode("#1A202C"));
    }

    public static Text create(String text) { 
        return new Text(text); 
    }

    public Text fontSize(int size) {
        this.label.setFont(new Font(DEFAULT_FONT, Font.BOLD, size));
        return this;
    }

    @Override
    public JComponent build() { 
        return label; 
    }
    
}
