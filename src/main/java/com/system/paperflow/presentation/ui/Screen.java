package com.system.paperflow.presentation.ui;

import java.awt.Dimension;

public interface Screen extends View {

    String withTitle();

    default Dimension initialSize() {
        return new Dimension(1280, 720);
    }
    
}
