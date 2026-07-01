package com.system.paperflow.presentation.ui;

import java.awt.Color;

public final class Theme {

    public static final String FONT_FAMILY = "Segoe UI";

    private Theme() {}

    public static final class Colors {

        public static final Color BACKGROUND = Color.decode("#F8F9FA");
        public static final Color SURFACE = Color.decode("#FFFFFF");
        public static final Color TEXT = Color.decode("#1A202C");
        public static final Color TEXT_MUTED = Color.decode("#4A5568");
        public static final Color BORDER = Color.decode("#E2E8F0");
        public static final Color PRIMARY = Color.decode("#3182CE");
        public static final Color SUCCESS = Color.decode("#2F855A");
        public static final Color ERROR = Color.decode("#C53030");

        private Colors() {}
    }

    public static final class Layout {

        public static final int SCREEN_PADDING = 32;
        public static final int CONTENT_WIDTH = 320;
        public static final int CONTROL_HEIGHT = 35;
        public static final int CARD_PADDING = 20;
        public static final int GRID_GAP = 16;

        private Layout() {}

        public static int widthFromPercent(int percent) {
            if (percent < 1 || percent > 100) {
                throw new IllegalArgumentException("A porcentagem deve estar entre 1 e 100.");
            }

            return CONTENT_WIDTH * percent / 100;
        }
    }
}
