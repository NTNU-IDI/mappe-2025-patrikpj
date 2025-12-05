package edu.ntnu.idi.idatt.view.author;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;

/**
 * View for editing an author's name.
 */
public class EditAuthorView implements BaseView {

  /**
   * Renders the edit author form.
   *
   * @param author the author being edited
   * @param out    the output stream
   */
  public void render(Author author, PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Edit Author", out);
    out.println("Editing: " + ConsoleFormatter.coloredText(author.getFullName(), AnsiColors.BLUE));
    out.println("Leave empty to keep current value.");
    out.println();
  }

  /**
   * Prompts for first name with current value shown.
   *
   * @param currentValue the current first name
   * @param out          the output stream
   */
  public void promptFirstName(String currentValue, PrintStream out) {
    out.print(AnsiColors.RESET + "First Name [" + currentValue + "]: " + AnsiColors.CYAN);
  }

  /**
   * Prompts for last name with current value shown.
   *
   * @param currentValue the current last name
   * @param out          the output stream
   */
  public void promptLastName(String currentValue, PrintStream out) {
    out.print(AnsiColors.RESET + "Last Name [" + currentValue + "]: " + AnsiColors.CYAN);
  }

  /**
   * Shows a message confirming the author was updated.
   *
   * @param name the updated author's name
   * @param out  the output stream
   */
  public void showUpdated(String name, PrintStream out) {
    out.print(AnsiColors.RESET);
    showSuccess("Updated author: " + name, out);
  }

  /**
   * Shows a message when no changes were made.
   *
   * @param out the output stream
   */
  public void showNoChanges(PrintStream out) {
    out.print(AnsiColors.RESET);
    showInfo("No changes made.", out);
  }
}

