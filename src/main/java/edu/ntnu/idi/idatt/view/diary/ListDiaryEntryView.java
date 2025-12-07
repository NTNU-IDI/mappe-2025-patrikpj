package edu.ntnu.idi.idatt.view.diary;

import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * View for displaying a list of diary entries.
 */
public class ListDiaryEntryView implements BaseView {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy");

  /**
   * Renders the list of diary entries.
   *
   * @param entries the list of entries to display
   * @param out     the output stream
   */
  public void render(List<DiaryEntry> entries, PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Diary Entries", out);

    out.println("Showing " + entries.size() + " entries, choose one to view details:");
    out.println();

    if (entries.isEmpty()) {
      out.println("No entries found.");
    } else {
      int index = 1;
      for (DiaryEntry entry : entries) {
        String dateStr = entry.getCreatedAt().format(DATE_FORMAT);
        String summary = entry.getTitle() + " - "
            + ConsoleFormatter.coloredText(entry.getAuthor().getFullName(), AnsiColors.CYAN)
            + " (" + dateStr + ")";
        ConsoleFormatter.menuItem(String.valueOf(index++), summary, out);
      }
    }

    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }

  /**
   * Renders search results with the keyword shown.
   *
   * @param entries the list of matching entries
   * @param keyword the keyword that was searched
   * @param out     the output stream
   */
  public void renderSearchResults(List<DiaryEntry> entries, String keyword, PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Search Results", out);

    out.println("Found " + entries.size() + " entries matching \"" + keyword + "\":");
    out.println();

    int index = 1;
    for (DiaryEntry entry : entries) {
      String dateStr = entry.getCreatedAt().format(DATE_FORMAT);
      String summary = entry.getTitle() + " - "
          + ConsoleFormatter.coloredText(entry.getAuthor().getFullName(), AnsiColors.CYAN)
          + " (" + dateStr + ")";
      ConsoleFormatter.menuItem(String.valueOf(index++), summary, out);
    }

    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }

  /**
   * Renders search results for a specific date.
   *
   * @param entries the list of matching entries
   * @param dateStr the date string that was searched
   * @param out     the output stream
   */
  public void renderDateResults(List<DiaryEntry> entries, String dateStr, PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Search Results", out);

    out.println("Found " + entries.size() + " entries from " + dateStr + ":");
    out.println();

    int index = 1;
    for (DiaryEntry entry : entries) {
      String timeStr = entry.getCreatedAt().format(DATE_FORMAT);
      String summary = entry.getTitle() + " - "
          + ConsoleFormatter.coloredText(entry.getAuthor().getFullName(), AnsiColors.CYAN)
          + " (" + timeStr + ")";
      ConsoleFormatter.menuItem(String.valueOf(index++), summary, out);
    }

    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }

  /**
   * Renders search results for a date range.
   *
   * @param entries  the list of matching entries
   * @param startStr the start date string
   * @param endStr   the end date string
   * @param out      the output stream
   */
  public void renderDateRangeResults(List<DiaryEntry> entries, String startStr, String endStr,
      PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Search Results", out);

    out.println(
        "Found " + entries.size() + " entries between " + startStr + " and " + endStr + ":");
    out.println();

    int index = 1;
    for (DiaryEntry entry : entries) {
      String timeStr = entry.getCreatedAt().format(DATE_FORMAT);
      String summary = entry.getTitle() + " - "
          + ConsoleFormatter.coloredText(entry.getAuthor().getFullName(), AnsiColors.CYAN)
          + " (" + timeStr + ")";
      ConsoleFormatter.menuItem(String.valueOf(index++), summary, out);
    }

    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }

  /**
   * Renders entries by a specific author.
   *
   * @param entries    the list of entries by the author
   * @param authorName the author's name
   * @param out        the output stream
   */
  public void renderAuthorEntries(List<DiaryEntry> entries, String authorName, PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Entries by " + authorName, out);

    out.println("Showing " + entries.size() + " entries:");
    out.println();

    if (entries.isEmpty()) {
      out.println("No entries found for this author.");
    } else {
      int index = 1;
      for (DiaryEntry entry : entries) {
        String dateStr = entry.getCreatedAt().format(DATE_FORMAT);
        String summary = entry.getTitle() + " (" + dateStr + ")";
        ConsoleFormatter.menuItem(String.valueOf(index++), summary, out);
      }
    }

    ConsoleFormatter.dangerItem("b", "Back", out);
    ConsoleFormatter.prompt(out);
  }
}

