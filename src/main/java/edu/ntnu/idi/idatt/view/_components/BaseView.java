package edu.ntnu.idi.idatt.view._components;

import java.io.PrintStream;

/**
 * Base interface for all views providing common message display methods.
 * Ensures consistent messaging across the application.
 */
public interface BaseView {

  /**
   * Displays a success message (green).
   *
   * @param message the success message
   * @param out     the output stream
   */
  default void showSuccess(String message, PrintStream out) {
    out.println(AnsiColors.GREEN + message + AnsiColors.RESET);
  }

  /**
   * Displays an error message (red).
   *
   * @param message the error message
   * @param out     the output stream
   */
  default void showError(String message, PrintStream out) {
    out.println(AnsiColors.RED + message + AnsiColors.RESET);
  }

  /**
   * Displays a warning message (yellow).
   *
   * @param message the warning message
   * @param out     the output stream
   */
  default void showWarning(String message, PrintStream out) {
    out.println(AnsiColors.YELLOW + message + AnsiColors.RESET);
  }

  /**
   * Displays an info message (no color).
   *
   * @param message the info message
   * @param out     the output stream
   */
  default void showInfo(String message, PrintStream out) {
    out.println(message);
  }

  /**
   * Displays a message indicating an operation was cancelled.
   *
   * @param out the output stream
   */
  default void showCancelled(PrintStream out) {
    out.println("Cancelled.");
  }

  /**
   * Prompts the user to press Enter to continue.
   *
   * @param out the output stream
   */
  default void promptContinue(PrintStream out) {
    out.print("\nPress Enter to continue...");
  }

  /**
   * Displays a confirmation prompt.
   *
   * @param message the confirmation message
   * @param out     the output stream
   */
  default void promptConfirm(String message, PrintStream out) {
    out.print(message + " (y/n): ");
  }

  /**
   * Displays a "not implemented" message for features under development.
   *
   * @param feature the feature name
   * @param out     the output stream
   */
  default void showNotImplemented(String feature, PrintStream out) {
    showWarning(feature + " - not implemented yet", out);
  }

  /**
   * Displays the input prompt indicator.
   *
   * @param out the output stream
   */
  default void prompt(PrintStream out) {
    out.print("> ");
  }
}

