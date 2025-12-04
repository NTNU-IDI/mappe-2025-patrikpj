package edu.ntnu.idi.idatt.view.components;

import java.util.Objects;
import java.util.Scanner;

/**
 * Utility class for handling user input and displaying messages in the console UI.
 * Provides consistent formatting and input handling across the application.
 */
public class InputHelper {

  private final Scanner scanner;

  /**
   * Creates a new InputHelper with the specified scanner.
   *
   * @param scanner the scanner for reading user input
   * @throws NullPointerException if scanner is null
   */
  public InputHelper(Scanner scanner) {
    this.scanner = Objects.requireNonNull(scanner, "Scanner cannot be null");
  }

  /**
   * Prompts the user for input with the specified message.
   *
   * @param message the prompt message to display
   * @return the user input (trimmed)
   */
  public String prompt(String message) {
    System.out.print(message);
    return scanner.nextLine().trim();
  }

  /**
   * Prompts for yes/no confirmation.
   *
   * @param message the confirmation message
   * @return true if user confirms (y/yes), false otherwise
   */
  public boolean confirm(String message) {
    String input = prompt(message + " (y/n): ").toLowerCase();
    return input.equals("y") || input.equals("yes");
  }

  /**
   * Displays a success message in green.
   *
   * @param message the success message
   */
  public void success(String message) {
    System.out.println(AnsiColors.GREEN + message + AnsiColors.RESET);
  }

  /**
   * Displays an error message in red.
   *
   * @param message the error message
   */
  public void error(String message) {
    System.out.println(AnsiColors.RED + message + AnsiColors.RESET);
  }

  /**
   * Displays a warning message in yellow.
   *
   * @param message the warning message
   */
  public void warning(String message) {
    System.out.println(AnsiColors.YELLOW + message + AnsiColors.RESET);
  }

  /**
   * Displays an info message (no color).
   *
   * @param message the info message
   */
  public void info(String message) {
    System.out.println(message);
  }

  /**
   * Pauses execution until the user presses Enter.
   */
  public void pause() {
    prompt("\nPress Enter to continue...");
  }

  /**
   * Reads multi-line content from the user.
   * Input ends when the user enters an empty line.
   *
   * @param promptMessage the prompt message to display before input
   * @return the content entered, or null if empty/cancelled
   */
  public String readMultilineContent(String promptMessage) {
    System.out.println(promptMessage);
    StringBuilder contentBuilder = new StringBuilder();
    String line;
    while (!(line = scanner.nextLine()).isEmpty()) {
      contentBuilder.append(line).append("\n");
    }
    String content = contentBuilder.toString().trim();
    return content.isEmpty() ? null : content;
  }

  /**
   * Truncates a string to the specified length, adding ellipsis if truncated.
   * Also replaces newlines with spaces for single-line display.
   *
   * @param text      the text to truncate
   * @param maxLength the maximum length
   * @return the truncated text
   */
  public String truncate(String text, int maxLength) {
    if (text == null) {
      return "";
    }
    String singleLine = text.replace("\n", " ");
    if (singleLine.length() <= maxLength) {
      return singleLine;
    }
    return singleLine.substring(0, maxLength) + "...";
  }
}

