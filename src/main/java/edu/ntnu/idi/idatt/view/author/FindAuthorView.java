package edu.ntnu.idi.idatt.view.author;

import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;

/**
 * View for finding an author by email.
 */
public class FindAuthorView implements BaseView {

    /**
     * Renders the find author form.
     *
     * @param out the output stream
     */
    public void render(PrintStream out) {
        out.println(AnsiColors.CLEAR_SCREEN);
        ConsoleFormatter.title("Find Author", out);
        out.println("Enter an email to search (leave empty to cancel).");
        out.println();
    }

    /**
     * Prompts for email input.
     *
     * @param out the output stream
     */
    public void promptEmail(PrintStream out) {
        out.print(AnsiColors.RESET + "Email: " + AnsiColors.CYAN);
    }

    /**
     * Shows a message when no author is found.
     *
     * @param email the email that was searched
     * @param out   the output stream
     */
    public void showNotFound(String email, PrintStream out) {
        out.print(AnsiColors.RESET);
        showWarning("No author found with email: " + email, out);
    }
}

