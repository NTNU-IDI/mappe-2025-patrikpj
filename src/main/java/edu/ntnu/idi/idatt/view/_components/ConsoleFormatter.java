package edu.ntnu.idi.idatt.view._components;

import java.io.PrintStream;

public final class ConsoleFormatter {

  private ConsoleFormatter() {
  }

  private static final int LENGTH = 30;

  public static void title(String text, PrintStream out) {
    out.println(AnsiColors.RESET);
    int padding = (LENGTH - text.length()) / 2;
    out.println("=".repeat(LENGTH));
    out.println(
        " ".repeat(padding) + ConsoleFormatter.coloredText(text, AnsiColors.PURPLE) + " ".repeat(
            padding));
    out.println("=".repeat(LENGTH));
  }

  public static void menuItem(String key, String label, PrintStream out) {
    out.println(
        "[" + AnsiColors.CYAN + key + AnsiColors.RESET + "] - " + label);
  }

  public static void dangerItem(String key, String label, PrintStream out) {
    out.println(
        "[" + AnsiColors.RED + key + AnsiColors.RESET + "] - " + label);
  }

  public static void prompt(PrintStream out) {
    out.println();
    out.print("> ");
  }

  public static String coloredText(String text, String color) {
    return color + text + AnsiColors.RESET;
  }

  public static void error(String message, PrintStream out) {
    out.println(AnsiColors.RED + message + AnsiColors.RESET);
  }
}