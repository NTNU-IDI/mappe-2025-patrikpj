package edu.ntnu.idi.idatt.view.mainmenu;

import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import edu.ntnu.idi.idatt.view._components.DiarySystemBanner;
import java.io.PrintStream;

/**
 * View for the main menu screen.
 */
public class MainMenuView implements BaseView {

  /**
   * Renders the main menu with available options.
   *
   * @param out the output stream
   */
  public void renderMenu(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);

    // Application banner
    out.println(DiarySystemBanner.getColoredBanner());
    ConsoleFormatter.title("Main Menu", out);

    // Menu options
    ConsoleFormatter.menuItem("1", "Diary Entries [...]", out);
    ConsoleFormatter.menuItem("2", "Author [...]", out);
    ConsoleFormatter.menuItem("3", "Statistics", out);

    // Exit option
    ConsoleFormatter.dangerItem("q", "Quit", out);
    ConsoleFormatter.prompt(out);
  }
}