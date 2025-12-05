package edu.ntnu.idi.idatt.view.statistics;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;
import java.util.Map;

/**
 * View for displaying application statistics.
 */
public class StatisticsView implements BaseView {

    /**
     * Renders the statistics view.
     *
     * @param totalAuthors     total number of authors
     * @param totalEntries     total number of entries
     * @param entriesPerAuthor map of author to entry count
     * @param out              the output stream
     */
    public void render(long totalAuthors, long totalEntries,
                       Map<Author, Long> entriesPerAuthor, PrintStream out) {
        out.println(AnsiColors.CLEAR_SCREEN);
        ConsoleFormatter.title("Statistics", out);

        // Overview section
        out.println("  Authors:  " + ConsoleFormatter.coloredText(String.valueOf(totalAuthors), AnsiColors.CYAN));
        out.println("  Entries:  " + ConsoleFormatter.coloredText(String.valueOf(totalEntries), AnsiColors.CYAN));

        // Average
        if (totalAuthors > 0) {
            double avg = (double) totalEntries / totalAuthors;
            out.println("  Average:  " + ConsoleFormatter.coloredText(String.format("%.1f", avg), AnsiColors.CYAN) 
                    + " entries/author");
        }

        // Entries per author breakdown
        if (!entriesPerAuthor.isEmpty()) {
            out.println();
            out.println("-".repeat(30));
            out.println("  Entries by Author");
            out.println("-".repeat(30));

            // Find longest name for alignment
            int maxLen = entriesPerAuthor.keySet().stream()
                    .mapToInt(a -> a.getFullName().length())
                    .max().orElse(0);

            for (var entry : entriesPerAuthor.entrySet()) {
                String name = entry.getKey().getFullName();
                long count = entry.getValue();
                String padding = " ".repeat(maxLen - name.length());
                out.println("  " + name + padding + "  " 
                        + ConsoleFormatter.coloredText(String.valueOf(count), AnsiColors.CYAN));
            }
        }

        out.println();
        ConsoleFormatter.dangerItem("b", "Back", out);
        ConsoleFormatter.prompt(out);
    }
}