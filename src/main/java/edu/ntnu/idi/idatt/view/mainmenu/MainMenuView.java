package edu.ntnu.idi.idatt.view.mainmenu;

import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import edu.ntnu.idi.idatt.view._components.DiarySystemBanner;
import java.io.PrintStream;

public class MainMenuView implements BaseView {

  public void renderMenu(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    out.println(DiarySystemBanner.getColoredBanner());
    ConsoleFormatter.title("Main Menu", out);
    ConsoleFormatter.menuItem("1", "Diary Entries [...]", out);
    ConsoleFormatter.menuItem("2", "Author [...]", out);
    ConsoleFormatter.menuItem("3", "Statistics", out);
    ConsoleFormatter.dangerItem("q", "Quit", out);
    ConsoleFormatter.prompt(out);
  }
}