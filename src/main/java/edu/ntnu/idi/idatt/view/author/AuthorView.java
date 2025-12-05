package edu.ntnu.idi.idatt.view.author;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import java.io.PrintStream;

public class AuthorView implements BaseView {

    /**
     * Renders the author detail view with action menu.
     *
     * @param author the author to display
     * @param out    the output stream
     */
    public void render(Author author, PrintStream out) {
        out.println(AnsiColors.CLEAR_SCREEN);
        ConsoleFormatter.title("Author Details", out);
        
        out.println("Name: " + ConsoleFormatter.coloredText(author.getFullName(), AnsiColors.BLUE));
        out.println("Email: " + ConsoleFormatter.coloredText(author.getEmail(), AnsiColors.BLUE));
        out.println("Created At: " + ConsoleFormatter.coloredText(author.getCreatedAt().toString(), AnsiColors.BLUE));
        out.println("Updated At: " + ConsoleFormatter.coloredText(author.getUpdatedAt().toString(), AnsiColors.BLUE));
        out.println();
        ConsoleFormatter.menuItem("1", "View Entries", out);
        ConsoleFormatter.menuItem("2", "Edit Info", out);
        ConsoleFormatter.dangerItem("3", ConsoleFormatter.coloredText("Delete", AnsiColors.RED), out);
        ConsoleFormatter.dangerItem("b", "Back", out);
        ConsoleFormatter.prompt(out);
    }

    /**
     * Prompts for delete confirmation with author name.
     *
     * @param authorName the name of the author to delete
     * @param out        the output stream
     */
    public void promptDeleteConfirmation(String authorName, PrintStream out) {
        out.println();
        out.println("Are you sure you want to delete " + authorName + "?");
        out.print("Type 'yes' to confirm: ");
    }

    /**
     * Shows a message confirming the author was deleted.
     *
     * @param out the output stream
     */
    public void showDeleted(PrintStream out) {
        showSuccess("Author deleted.", out);
    }
}