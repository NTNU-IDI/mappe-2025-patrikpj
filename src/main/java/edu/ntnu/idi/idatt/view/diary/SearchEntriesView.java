package edu.ntnu.idi.idatt.view.diary;

import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;

/**
 * View for the search entries menu.
 */
public class SearchEntriesView implements BaseView {

  /**
   * Renders the search entries menu.
   *
   * @param out the output stream
   */
  public void renderMenu(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Search Entries", out);
    ConsoleFormatter.menuItem("1", "Search by keyword", out);
    ConsoleFormatter.menuItem("2", "Search by date", out);
    ConsoleFormatter.menuItem("3", "Search by date range", out);
    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }
}

