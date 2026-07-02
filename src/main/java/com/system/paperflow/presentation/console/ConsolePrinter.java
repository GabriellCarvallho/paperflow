package com.system.paperflow.presentation.console;

import java.util.List;

public class ConsolePrinter {

    private static final int WIDTH = 72;

    public void appHeader() {
        line('=');
        row("PAPER FLOW");
        row("Sistema de Submissao e Avaliacao de Artigos Cientificos");
        line('=');
    }

    public void title(String title) {
        System.out.println();
        line('=');
        row(title);
        line('=');
    }

    public void section(String title) {
        System.out.println();
        line('-');
        row(title);
        line('-');
    }

    public void menu(String... items) {
        for (int i = 0; i < items.length; i++) {
            System.out.printf("%2d. %s%n", i + 1, items[i]);
        }
        System.out.println();
    }

    public void success(String message) {
        System.out.println("[OK] " + message);
    }

    public void error(String message) {
        System.out.println("[ERRO] " + message);
    }

    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public void empty(String message) {
        System.out.println("(vazio) " + message);
    }

    public void table(List<String> headers, List<List<String>> rows) {
        ConsoleTable.print(headers, rows);
    }

    private void line(char character) {
        System.out.println("+" + String.valueOf(character).repeat(WIDTH - 2) + "+");
    }

    private void row(String text) {
        String value = text == null ? "" : text;
        if (value.length() > WIDTH - 4) {
            value = value.substring(0, WIDTH - 7) + "...";
        }
        System.out.printf("| %-" + (WIDTH - 4) + "s |%n", value);
    }
}
