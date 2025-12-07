package edu.ntnu.idi.idatt.view.author;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;
import java.util.List;

/**
 * View for displaying a list of authors.
 */
public class ListAuthorView implements BaseView {

  /**
   * Renders the list of authors.
   *
   * @param authors the list of authors to display
   * @param out     the output stream
   */
  public void render(List<Author> authors, PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Authors List", out);

    out.println("Showing " + authors.size() + " authors, choose one to view details / edit:");
    out.println();

    // List authors or show empty message
    if (authors.isEmpty()) {
      out.println("No authors found.");
    } else {
      int index = 1;
      for (Author author : authors) {
        ConsoleFormatter.menuItem(String.valueOf(index++), author.toDisplayString(), out);
      }
    }

    // Navigation
    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }
}

