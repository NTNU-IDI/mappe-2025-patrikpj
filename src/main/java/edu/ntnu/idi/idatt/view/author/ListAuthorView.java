package edu.ntnu.idi.idatt.view.author;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.view._components.BaseView;
import edu.ntnu.idi.idatt.view._components.ConsoleFormatter;
import edu.ntnu.idi.idatt.view._components.AnsiColors;
import java.io.PrintStream;
import java.util.List;

public class ListAuthorView implements BaseView {

    /**
     * Renders the list of authors.
     *
     * @param authors the list of authors to display
     * @param out     the output stream
     */
    public void render(List<Author> authors, PrintStream out) {
        out.println(AnsiColors.CLEAR_SCREEN);
        ConsoleFormatter.title("Authors List", out);
        
        out.println("Showing " + authors.size() + " authors, choose one to view details / edit:");
        out.println();
        if (authors.isEmpty()) {
            out.println("No authors found.");
        } else {
            int index = 1;
            for (Author author : authors) {
                ConsoleFormatter.menuItem(String.valueOf(index++), author.getFullName() + " (" + author.getEmail() + ")", out);
            }
        }
        
        ConsoleFormatter.dangerItem("b", "Back", out);
        ConsoleFormatter.prompt(out);
    }
}

