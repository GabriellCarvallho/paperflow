package com.system.paperflow.presentation.console;

import com.system.paperflow.presentation.console.screen.ConsoleScreen;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConsoleRouter {

    public static final String PUBLIC_MENU = "public-menu";
    public static final String COORDINATOR_MENU = "coordinator-menu";
    public static final String RESEARCHER_MENU = "researcher-menu";
    public static final String EVENT = "event";
    public static final String THEMATIC_AREA = "thematic-area";
    public static final String COMMITTEE = "committee";
    public static final String PAPER = "paper";
    public static final String REVIEW = "review";
    public static final String DASHBOARD = "dashboard";
    public static final String EMAIL = "email";

    private final Map<String, ConsoleScreen> screens = new LinkedHashMap<>();
    private String currentRoute = PUBLIC_MENU;

    public void register(String route, ConsoleScreen screen) {
        screens.put(route, screen);
    }

    public void navigateTo(String route) {
        currentRoute = route;
    }

    public void showCurrent() {
        ConsoleScreen screen = screens.get(currentRoute);
        if (screen == null) {
            throw new IllegalStateException("Tela de console nao encontrada: " + currentRoute);
        }
        screen.show();
    }
}
