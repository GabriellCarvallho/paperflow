package com.system.paperflow.presentation.console;

import java.util.ArrayList;
import java.util.List;

public class ConsoleTable {

    private ConsoleTable() {
    }

    public static void print(List<String> headers, List<List<String>> rows) {
        List<Integer> widths = widths(headers, rows);
        printLine(widths);
        printRow(headers, widths);
        printLine(widths);
        for (List<String> row : rows) {
            printRow(row, widths);
        }
        printLine(widths);
    }

    private static List<Integer> widths(List<String> headers, List<List<String>> rows) {
        List<Integer> widths = new ArrayList<>();
        for (String header : headers) {
            widths.add(clean(header).length());
        }

        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                widths.set(i, Math.max(widths.get(i), Math.min(clean(row.get(i)).length(), 36)));
            }
        }

        return widths;
    }

    private static void printLine(List<Integer> widths) {
        StringBuilder builder = new StringBuilder("+");
        for (Integer width : widths) {
            builder.append("-".repeat(width + 2)).append("+");
        }
        System.out.println(builder);
    }

    private static void printRow(List<String> row, List<Integer> widths) {
        StringBuilder builder = new StringBuilder("|");
        for (int i = 0; i < widths.size(); i++) {
            String value = i < row.size() ? clean(row.get(i)) : "";
            if (value.length() > widths.get(i)) {
                value = value.substring(0, widths.get(i) - 3) + "...";
            }
            builder.append(" ").append(String.format("%-" + widths.get(i) + "s", value)).append(" |");
        }
        System.out.println(builder);
    }

    private static String clean(String value) {
        return value == null ? "" : value.replace('\n', ' ').replace('\r', ' ');
    }
}
