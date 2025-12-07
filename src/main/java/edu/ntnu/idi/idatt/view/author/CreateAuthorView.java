package edu.ntnu.idi.idatt.view.author;

import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;

/**
 * View for the create author form.
 */
public class CreateAuthorView implements BaseView {

  /**
   * Renders the create author form.
   *
   * @param out the output stream
   */
  public void render(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Create Author", out);

    // Instructions
    out.println("Enter author details (leave empty to cancel).");
    out.println();
  }

  /**
   * Prompts for the author's first name.
   *
   * @param out the output stream
   */
  public void promptFirstName(PrintStream out) {
    out.print(AnsiColors.RESET + "First Name: " + AnsiColors.CYAN);
  }

  /**
   * Prompts for the author's last name.
   *
   * @param out the output stream
   */
  public void promptLastName(PrintStream out) {
    out.print(AnsiColors.RESET + "Last Name: " + AnsiColors.CYAN);
  }

  /**
   * Prompts for the author's email.
   *
   * @param out the output stream
   */
  public void promptEmail(PrintStream out) {
    out.print(AnsiColors.RESET + "Email: " + AnsiColors.CYAN);
  }

  /**
   * Displays a success message for author creation.
   *
   * @param name the created author's name
   * @param out  the output stream
   */
  public void showCreated(String name, PrintStream out) {
    showSuccess("Created author: " + name, out);
  }
}

