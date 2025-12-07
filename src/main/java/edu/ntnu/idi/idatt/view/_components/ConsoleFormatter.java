package edu.ntnu.idi.idatt.view._components;

import java.io.PrintStream;

/**
 * Utility class for formatting console output.
 */
public final class ConsoleFormatter {

  private ConsoleFormatter() {
  }

  private static final int LENGTH = 30;

  /**
   * Prints a centered title with decorative borders.
   *
   * @param text the title text
   * @param out  the output stream
   */
  public static void title(String text, PrintStream out) {
    out.println(AnsiColors.RESET);
    int padding = (LENGTH - text.length()) / 2;
    out.println("=".repeat(LENGTH));
    out.println(
        " ".repeat(padding) + ConsoleFormatter.coloredText(text, AnsiColors.PURPLE) + " ".repeat(
            padding));
    out.println("=".repeat(LENGTH));
  }

  /**
   * Prints a menu item with a highlighted key.
   *
   * @param key   the key/shortcut
   * @param label the menu item label
   * @param out   the output stream
   */
  public static void menuItem(String key, String label, PrintStream out) {
    out.println(
        "[" + AnsiColors.CYAN + key + AnsiColors.RESET + "] - " + label);
  }

  /**
   * Prints a danger/warning menu item with a red highlighted key.
   *
   * @param key   the key/shortcut
   * @param label the menu item label
   * @param out   the output stream
   */
  public static void dangerItem(String key, String label, PrintStream out) {
    out.println(
        "[" + AnsiColors.RED + key + AnsiColors.RESET + "] - " + label);
  }

  /**
   * Prints a prompt for user input.
   *
   * @param out the output stream
   */
  public static void prompt(PrintStream out) {
    out.println();
    out.print("> ");
  }

  /**
   * Wraps text with ANSI color codes.
   *
   * @param text  the text to color
   * @param color the ANSI color code
   * @return the colored text string
   */
  public static String coloredText(String text, String color) {
    return color + text + AnsiColors.RESET;
  }

  /**
   * Prints an error message in red.
   *
   * @param message the error message
   * @param out     the output stream
   */
  public static void error(String message, PrintStream out) {
    out.println(AnsiColors.RED + message + AnsiColors.RESET);
  }
}