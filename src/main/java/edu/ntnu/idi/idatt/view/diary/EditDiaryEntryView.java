package edu.ntnu.idi.idatt.view.diary;

import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;

/**
 * View for editing a diary entry's title and content.
 */
public class EditDiaryEntryView implements BaseView {

  /**
   * Renders the edit entry form header.
   *
   * @param entry the entry being edited
   * @param out   the output stream
   */
  public void render(DiaryEntry entry, PrintStream out) {
    out.println(AnsiColors.CLEAR_SCREEN);
    ConsoleFormatter.title("Edit Entry", out);
    out.println("Editing: " + ConsoleFormatter.coloredText(entry.getTitle(), AnsiColors.BLUE));
    out.println("By: " + entry.getAuthor().getFullName());
    out.println();
    out.println("Leave empty to keep current value.");
    out.println();
  }

  /**
   * Prompts for title with current value shown.
   *
   * @param currentTitle the current title
   * @param out          the output stream
   */
  public void promptTitle(String currentTitle, PrintStream out) {
    out.print(AnsiColors.RESET + "Title [" + currentTitle + "]: " + AnsiColors.CYAN);
  }

  /**
   * Prompts for content replacement.
   *
   * @param out the output stream
   */
  public void promptReplaceContent(PrintStream out) {
    out.print(AnsiColors.RESET + "Replace content? (y/N): " + AnsiColors.CYAN);
  }

  /**
   * Shows instructions for entering new content.
   *
   * @param out the output stream
   */
  public void showContentInstructions(PrintStream out) {
    out.println(AnsiColors.RESET);
    out.println("Enter new content (leave two empty lines to finish):");
    out.print(AnsiColors.CYAN);
  }

  /**
   * Shows a message confirming the entry was updated.
   *
   * @param title the updated entry's title
   * @param out   the output stream
   */
  public void showUpdated(String title, PrintStream out) {
    out.print(AnsiColors.RESET);
    showSuccess("Updated entry: " + title, out);
  }

  /**
   * Shows a message when no changes were made.
   *
   * @param out the output stream
   */
  public void showNoChanges(PrintStream out) {
    out.print(AnsiColors.RESET);
    showInfo("No changes made.", out);
  }
}
