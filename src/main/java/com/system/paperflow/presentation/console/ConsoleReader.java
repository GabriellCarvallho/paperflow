package com.system.paperflow.presentation.console;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleReader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Scanner scanner;

    public ConsoleReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String text(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    public String optionalText(String label) {
        System.out.print(label + " (opcional): ");
        return scanner.nextLine().trim();
    }

    public int option(String label, int min, int max) {
        while (true) {
            String value = text(label);
            try {
                int option = Integer.parseInt(value);
                if (option >= min && option <= max) {
                    return option;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("[ERRO] Escolha uma opcao entre " + min + " e " + max + ".");
        }
    }

    public LocalDate date(String label) {
        while (true) {
            String value = text(label + " (dd/MM/yyyy)");
            try {
                return LocalDate.parse(value, DATE_FORMAT);
            } catch (DateTimeParseException exception) {
                System.out.println("[ERRO] Data invalida. Use o formato dd/MM/yyyy.");
            }
        }
    }

    public List<String> csv(String label) {
        String value = optionalText(label);
        if (value.isBlank()) {
            return List.of();
        }

        String[] parts = value.split(",");
        List<String> values = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isBlank()) {
                values.add(trimmed);
            }
        }
        return values;
    }

    public void pause() {
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}
