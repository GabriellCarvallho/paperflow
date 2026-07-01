package com.system.paperflow.presentation.ui.component;

import java.awt.Dimension;

import javax.swing.JComponent;

import com.system.paperflow.presentation.ui.Theme;
import com.system.paperflow.presentation.ui.View;

public class FormField implements View {

    private final String label;
    private final View input;
    private int widthPercent = 100;

    private FormField(String label, TextField input) {
        this.label = label;
        this.input = input;
    }

    public static FormField create(String label, TextField input) {
        return new FormField(label, input);
    }

    public FormField withWidthPercent(int widthPercent) {
        this.widthPercent = widthPercent;
        return this;
    }

    public FormField fullWidth() {
        return withWidthPercent(100);
    }

    @Override
    public JComponent build() {
        JComponent content = Column.create()
            .gap(6)
            .children(
                Text.body(label),
                input
            )
            .build();

        Dimension size = new Dimension(Theme.Layout.widthFromPercent(widthPercent), 64);
        content.setPreferredSize(size);
        content.setMinimumSize(size);
        content.setMaximumSize(size);

        return content;
    }
}
