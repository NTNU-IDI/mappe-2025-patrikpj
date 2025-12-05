package edu.ntnu.idi.idatt.view.diary;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;
import java.util.List;

/**
 * View for creating a new diary entry.
 */
public class CreateDiaryEntryView implements BaseView {

  /**
   * Renders the create entry form header.
   *
   * @param out the output stream
   */
  public void render(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Create Entry", out);
    out.println("Enter entry details (leave empty to cancel).");
    out.println();
  }

  /**
   * Prompts for the entry title.
   *
   * @param out the output stream
   */
  public void promptTitle(PrintStream out) {
    out.print(AnsiColors.RESET + "Title: " + AnsiColors.CYAN);
  }

  /**
   * Renders the author selection list.
   *
   * @param authors the list of available authors
   * @param out     the output stream
   */
  public void showAuthorSelection(List<Author> authors, PrintStream out) {
    out.println(AnsiColors.RESET);
    out.println("Select an author:");
    int index = 1;
    for (Author author : authors) {
      ConsoleFormatter.menuItem(String.valueOf(index++), author.getFullName(), out);
    }
    out.println();
  }

  /**
   * Prompts for author selection.
   *
   * @param out the output stream
   */
  public void promptAuthor(PrintStream out) {
    out.print(AnsiColors.RESET + "Author #: " + AnsiColors.CYAN);
  }

  /**
   * Shows the content input instructions.
   *
   * @param out the output stream
   */
  public void showContentInstructions(PrintStream out) {
    out.println(AnsiColors.RESET);
    out.println("Enter content (press Enter twice to finish):");
    out.print(AnsiColors.CYAN);
  }

  /**
   * Shows message when no authors exist.
   *
   * @param out the output stream
   */
  public void showNoAuthors(PrintStream out) {
    showWarning("No authors exist. Create an author first.", out);
    showWarning("Go back to the Main Menu and go to the Author Menu.", out);
  }

  /**
   * Shows a success message for entry creation.
   *
   * @param title the created entry's title
   * @param out   the output stream
   */
  public void showCreated(String title, PrintStream out) {
    out.print(AnsiColors.RESET);
    showSuccess("Created entry: " + title, out);
  }
}

