package com.system.paperflow.presentation.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScreenUtils {

    private static final Map<String, Supplier<Screen>> routes = new HashMap<>();

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

    public static void register(String routeName, Supplier<Screen> screenFactory) {
        runOnEventDispatchThread(() -> {
            routes.put(routeName, screenFactory);
        });
    }

    public static void navigateTo(String routeName) {
        runOnEventDispatchThread(() -> show(routeName));
    }

    private static Screen createScreen(String routeName) {
        Supplier<Screen> screenFactory = routes.get(routeName);

        if (screenFactory == null) {
            return null;
        }

        Screen screen = screenFactory.get();
        JComponent component = screen.build();
        component.setName(routeName);

        removePreviousComponent(routeName);
        panel.add(component, routeName);

        return screen;
    }

    public static void start(String initialRoute) {
        runOnEventDispatchThread(() -> {
            show(initialRoute);
            frame.setVisible(true);
        });
    }

    private static void show(String routeName) {
        Screen screen = createScreen(routeName);
        if (screen != null) {
            frame.setTitle(screen.withTitle());

            frame.setPreferredSize(screen.initialSize());

            layout.show(panel, routeName);

            frame.pack();
            frame.setLocationRelativeTo(null);
        } else {
            throw new RuntimeException("Tela não encontrada");
        }
    }

    private static void removePreviousComponent(String routeName) {
        for (Component component : panel.getComponents()) {
            if (routeName.equals(component.getName())) {
                panel.remove(component);
                return;
            }
        }
    }

    private static void runOnEventDispatchThread(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) {
            action.run();
            return;
        }

        SwingUtilities.invokeLater(action);
    }
    
}
