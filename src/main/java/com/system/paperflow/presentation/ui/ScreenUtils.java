package com.system.paperflow.presentation.ui;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScreenUtils {

    private static final Map<String, Screen> routes = new HashMap<>();

    private static JFrame frame;
    private static JPanel panel;
    private static CardLayout layout;

    static {
        SwingUtilities.invokeLater(() -> {

            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            layout = new CardLayout();
            panel = new JPanel(layout);

            frame.add(panel);
        });
    }

    private ScreenUtils() {}

    public static void register(String routeName, Screen screen) {
        SwingUtilities.invokeLater(() -> {
            routes.put(routeName, screen);
            panel.add(screen.build(), routeName);
        });
    }

    public static void navigateTo(String routeName) {
        SwingUtilities.invokeLater(() -> {
            Screen screen = routes.get(routeName);
            if (screen != null) {
                frame.setTitle(screen.withTitle());

                frame.setPreferredSize(screen.withDimension());

                layout.show(panel, routeName);

                frame.pack();
                frame.setLocationRelativeTo(null);
            } else {
                throw new RuntimeException("Tela não encontrada");
            }
        });
    }

    public static void start(String initialRoute) {
        SwingUtilities.invokeLater(() -> {
            navigateTo(initialRoute);
            frame.setVisible(true);
        });
    }
    
}
