package edu.ntnu.idi.idatt.view.diary;

import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;

/**
 * View for displaying a single diary entry's details.
 */
public class DiaryEntryView implements BaseView {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' HH:mm");
    private static final int CONTENT_WIDTH = 60;

    /**
     * Renders the diary entry detail view with action menu.
     *
     * @param entry the diary entry to display
     * @param out   the output stream
     */
    public void render(DiaryEntry entry, PrintStream out) {
        out.println(AnsiColors.CLEAR_SCREEN);
        ConsoleFormatter.title(entry.getTitle(), out);
        
        // Metadata
        out.println("By: " + entry.getAuthor().getFullName());
        out.println("Created: " + entry.getCreatedAt().format(DATE_FORMAT));
        if (!entry.getCreatedAt().equals(entry.getUpdatedAt())) {
            out.println("Edited: " + entry.getUpdatedAt().format(DATE_FORMAT));
        }
        out.println();
        
        // Content section
        out.println("-".repeat(CONTENT_WIDTH));
        out.println();
        printWrappedContent(entry.getContent(), CONTENT_WIDTH - 4, out);
        out.println();
        out.println("-".repeat(CONTENT_WIDTH));
        
        // Word count
        int wordCount = entry.getContent().split("\\s+").length;
        out.println(wordCount + " words");
        out.println();

        // Actions
        ConsoleFormatter.menuItem("1", "Edit Entry", out);
        ConsoleFormatter.dangerItem("2", ConsoleFormatter.coloredText("Delete", AnsiColors.RED), out);
        ConsoleFormatter.dangerItem("b", "Back", out);
        ConsoleFormatter.prompt(out);
    }

    /**
     * Prints content with word wrapping and indentation.
     *
     * @param content the content to print
     * @param width   the maximum line width
     * @param out     the output stream
     */
    private void printWrappedContent(String content, int width, PrintStream out) {
        String[] paragraphs = content.split("\n");
        for (String paragraph : paragraphs) {
            if (paragraph.isBlank()) {
                out.println();
                continue;
            }
            String[] words = paragraph.split(" ");
            StringBuilder line = new StringBuilder("  ");
            for (String word : words) {
                if (line.length() + word.length() + 1 > width) {
                    out.println(line);
                    line = new StringBuilder("  " + word);
                } else {
                    if (line.length() > 2) {
                        line.append(" ");
                    }
                    line.append(word);
                }
            }
            if (line.length() > 2) {
                out.println(line);
            }
        }
    }

    /**
     * Prompts for delete confirmation with entry title.
     *
     * @param title the title of the entry to delete
     * @param out   the output stream
     */
    public void promptDeleteConfirmation(String title, PrintStream out) {
        out.println();
        out.println("Are you sure you want to delete \"" + title + "\"?");
        out.print("Type 'yes' to confirm: ");
    }

    /**
     * Shows a message confirming the entry was deleted.
     *
     * @param out the output stream
     */
    public void showDeleted(PrintStream out) {
        showSuccess("Entry deleted.", out);
    }
}