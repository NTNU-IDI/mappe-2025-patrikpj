package edu.ntnu.idi.idatt.util;

import edu.ntnu.idi.idatt.model.Author;
import edu.ntnu.idi.idatt.ui.AnsiColors;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Utility class for handling user input with validation.
 */
public final class InputHelper {

  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private InputHelper() {
    throw new AssertionError("Utility class cannot be instantiated");
  }

  /**
   * Prompts user for input. Returns null if input is empty (for cancellation).
   *
   * @param scanner the scanner to read input from
   * @param prompt  the prompt message
   * @return the trimmed input, or null if empty
   */
  public static String promptInput(Scanner scanner, String prompt) {
    System.out.print(prompt);
    String input = scanner.nextLine().trim();
    if (input.isEmpty()) {
      System.out.println("Cancelled.");
      return null;
    }
    return input;
  }

  /**
   * Reads a non-empty string from user input. Loops until valid input.
   *
   * @param scanner the scanner to read input from
   * @param prompt  the prompt message
   * @return the non-empty trimmed input
   */
  public static String readNonEmptyString(Scanner scanner, String prompt) {
    String input;
    do {
      System.out.print(prompt);
      input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        System.out.println("Input cannot be empty. Please try again.");
      }
    } while (input.isEmpty());
    return input;
  }

  /**
   * Prompts for input with validation and retry option. Returns null if cancelled.
   *
   * @param scanner   the scanner to read input from
   * @param prompt    the prompt message
   * @param validator validation function (throws IllegalArgumentException if invalid)
   * @return the valid input, or null if cancelled
   */
  public static String promptValidatedInput(Scanner scanner, String prompt,
      Consumer<String> validator) {
    while (true) {
      String input = promptInput(scanner, prompt);
      if (input == null) {
        return null;
      }

      try {
        validator.accept(input);
        return input;
      } catch (IllegalArgumentException e) {
        System.out.println(AnsiColors.RED + "Invalid: " + e.getMessage() + AnsiColors.RESET);
        if (!promptRetry(scanner)) {
          return null;
        }
      }
    }
  }

  /**
   * Reads and validates an email address with retry option. Returns null if cancelled.
   *
   * @param scanner the scanner to read input from
   * @return the valid email, or null if cancelled
   */
  public static String promptEmail(Scanner scanner) {
    while (true) {
      String email = promptInput(scanner, "Email: ");
      if (email == null) {
        return null;
      }

      if (!Author.isValidEmail(email)) {
        System.out.println(AnsiColors.RED + "Invalid email format" + AnsiColors.RESET);
        if (!promptRetry(scanner)) {
          return null;
        }
        continue;
      }

      return email;
    }
  }

  /**
   * Reads multiline text for diary entry content.
   * Continues reading until user enters an empty line.
   *
   * @param scanner the scanner to read input from
   * @param prompt  the prompt message
   * @return the multiline text, or null if empty (cancelled)
   */
  public static String readMultilineText(Scanner scanner, String prompt) {
    System.out.println(prompt);
    System.out.println("(Press Enter twice to finish)");

    StringBuilder text = new StringBuilder();
    String line;
    while (!(line = scanner.nextLine()).isEmpty()) {
      if (!text.isEmpty()) {
        text.append("\n");
      }
      text.append(line);
    }

    if (text.isEmpty()) {
      System.out.println("Cancelled.");
      return null;
    }

    return text.toString();
  }

  /**
   * Prompts user to retry after an error.
   *
   * @param scanner the scanner to read input from
   * @return true if user wants to retry, false otherwise
   */
  public static boolean promptRetry(Scanner scanner) {
    System.out.print("Try again? (y/n): ");
    String input = scanner.nextLine().trim().toLowerCase();
    return input.equals("y") || input.equals("yes");
  }

  /**
   * Validates that a string is not blank.
   *
   * @param value the value to validate
   * @throws IllegalArgumentException if value is blank
   */
  public static void validateNotBlank(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Cannot be blank");
    }
  }
}

