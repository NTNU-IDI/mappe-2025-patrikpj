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

  /**
   * Renders the date search form.
   *
   * @param out the output stream
   */
  public void renderDateSearch(PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Search by Date", out);
    out.println("Enter a date to find entries from that day.");
    out.println("Format: DD-MM-YYYY (e.g. 05-12-2025)");
    out.println("Leave empty to cancel.");
    out.println();
  }

  /**
   * Prompts for date input.
   *
   * @param out the output stream
   */
  public void promptDate(PrintStream out) {
    out.print(AnsiColors.RESET + "Date: " + AnsiColors.CYAN);
  }

  /**
   * Shows message for invalid date format.
   *
   * @param out the output stream
   */
  public void showInvalidDateFormat(PrintStream out) {
    out.print(AnsiColors.RESET);
    showError("Invalid date format. Use DD-MM-YYYY (e.g. 05-12-2025)", out);
  }

  /**
   * Shows message when no results found for a date.
   *
   * @param dateStr the date string that was searched
   * @param out     the output stream
   */
  public void showNoResultsForDate(String dateStr, PrintStream out) {
    out.print(AnsiColors.RESET);
    showWarning("No entries found for: " + dateStr, out);
  }
}

