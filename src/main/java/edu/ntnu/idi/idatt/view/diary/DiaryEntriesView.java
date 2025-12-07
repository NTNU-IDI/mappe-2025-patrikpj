package edu.ntnu.idi.idatt.view.diary;

import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;

/**
 * View for the diary entries menu.
 */
public class DiaryEntriesView implements BaseView {

  /**
   * Renders the diary entries menu.
   *
   * @param out the output stream
   */
  public void renderMenu(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Diary Entries Menu", out);

    // Menu options
    ConsoleFormatter.menuItem("1", "List All Entries", out);
    ConsoleFormatter.menuItem("2", "Create Entry", out);
    ConsoleFormatter.menuItem("3", "Search Entries [...]", out);

    // Navigation
    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }
}

