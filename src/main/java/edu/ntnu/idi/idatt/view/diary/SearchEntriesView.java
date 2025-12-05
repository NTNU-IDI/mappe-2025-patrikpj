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

  /**
   * Renders the keyword search form.
   *
   * @param out the output stream
   */
  public void renderKeywordSearch(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Search by Keyword", out);
    out.println("Enter a keyword to search in titles and content.");
    out.println("Leave empty to cancel.");
    out.println();
  }

  /**
   * Prompts for keyword input.
   *
   * @param out the output stream
   */
  public void promptKeyword(PrintStream out) {
    out.print(AnsiColors.RESET + "Keyword: " + AnsiColors.CYAN);
  }

  /**
   * Shows message when no results found.
   *
   * @param keyword the keyword that was searched
   * @param out     the output stream
   */
  public void showNoResults(String keyword, PrintStream out) {
    out.print(AnsiColors.RESET);
    showWarning("No entries found matching: " + keyword, out);
  }

  /**
   * Shows the number of results found.
   *
   * @param count   the number of results
   * @param keyword the keyword searched
   * @param out     the output stream
   */
  public void showResultCount(int count, String keyword, PrintStream out) {
    out.print(AnsiColors.RESET);
    out.println("Found " + count + " entries matching \"" + keyword + "\":");
    out.println();
  }
}

