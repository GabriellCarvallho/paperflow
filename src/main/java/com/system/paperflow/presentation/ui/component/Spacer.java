package com.system.paperflow.presentation.ui.component;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;

import com.system.paperflow.presentation.ui.View;

public class Spacer implements View {

    private final int width;
    private final int height;

    private Spacer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static Spacer create(int width, int height) {
        return new Spacer(width, height);
    }

    public static Spacer vertical(int height) {
        return new Spacer(1, height);
    }

    public static Spacer horizontal(int width) {
        return new Spacer(width, 1);
    }

    @Override
    public JComponent build() {
        JComponent spacer = (JComponent) Box.createRigidArea(new Dimension(width, height));
        spacer.setOpaque(false);
        return spacer;
    }
}
