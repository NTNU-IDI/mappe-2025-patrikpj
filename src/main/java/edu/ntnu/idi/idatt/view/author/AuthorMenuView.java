package edu.ntnu.idi.idatt.view.author;

import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;

/**
 * View for the author menu screen.
 */
public class AuthorMenuView implements BaseView {

  /**
   * Renders the author menu with available options.
   *
   * @param out the output stream
   */
  public void renderMenu(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Author Menu", out);

    // Menu options
    ConsoleFormatter.menuItem("1", "List all authors", out);
    ConsoleFormatter.menuItem("2", "Create author", out);
    ConsoleFormatter.menuItem("3", "Find by email", out);

    // Navigation
    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }
}

