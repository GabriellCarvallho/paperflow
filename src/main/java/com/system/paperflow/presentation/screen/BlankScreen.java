package com.system.paperflow.presentation.screen;

import javax.swing.JComponent;

import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.Page;

public class BlankScreen implements Screen {

    @Override
    public JComponent build() {
        return Page.create(Column.create()).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow";
    }
}
