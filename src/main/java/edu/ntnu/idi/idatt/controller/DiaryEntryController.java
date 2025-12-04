package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.service.AuthorService;
import edu.ntnu.idi.idatt.service.DiaryEntryService;
import edu.ntnu.idi.idatt.view.components.AnsiColors;
import edu.ntnu.idi.idatt.view.components.InputHelper;
import edu.ntnu.idi.idatt.view.components.MenuOption;
import edu.ntnu.idi.idatt.view.components.MenuView;
import edu.ntnu.idi.idatt.view.components.Paginator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Controller for diary entry-related menu actions.
 */
public class DiaryEntryController {

  private final DiaryEntryService entryService;
  private final AuthorService authorService;
  private final Scanner scanner;
  private final InputHelper input;

  /**
   * Creates a new DiaryEntryController.
   *
   * @param entryService  the diary entry service
   * @param authorService the author service
   * @param scanner       the scanner for user input
   * @throws NullPointerException if any argument is null
   */
  public DiaryEntryController(DiaryEntryService entryService,
                              AuthorService authorService,
                              Scanner scanner) {
    this.entryService = Objects.requireNonNull(entryService, "DiaryEntryService cannot be null");
    this.authorService = Objects.requireNonNull(authorService, "AuthorService cannot be null");
    this.scanner = Objects.requireNonNull(scanner, "Scanner cannot be null");
    this.input = new InputHelper(scanner);
  }

  /**
   * Returns the diary entry management menu.
   *
   * @return the entries menu
   */
  public MenuView getMenu() {
    MenuView menu = new MenuView("== Diary Entries ==", scanner);
    menu.addOption(new MenuOption("List All Entries", this::listEntries));
    menu.addOption(new MenuOption("Create Entry", this::createEntry));
    menu.addOption(new MenuOption("Search Entries", this::searchEntries));
    return menu;
  }

  /**
   * Lists all entries and allows selection.
   */
  private void listEntries() {
    List<DiaryEntry> entries = entryService.findAll();

    Paginator<DiaryEntry> paginator = new Paginator<>(
        entries,
        scanner,
        "All Entries",
        entry -> entry.getTitle() + " - " + entry.getAuthor().getFullName()
    );

    DiaryEntry selected = paginator.show();
    if (selected != null) {
      showEntryActions(selected);
    }
  }

  /**
   * Creates a new diary entry.
   */
  private void createEntry() {
    input.info("\n== Create Entry ==");

    // Select author
    List<Author> authors = authorService.findAll();
    if (authors.isEmpty()) {
      input.warning("No authors found.");
      if (input.confirm("Create an author now?")) {
        Author newAuthor = createAuthorInline();
        if (newAuthor == null) {
          return;
        }
        authors = List.of(newAuthor);
      } else {
        return;
      }
    }

    input.info("\nSelect an author:");
    Paginator<Author> authorPaginator = new Paginator<>(
        authors,
        scanner,
        "Select Author",
        Author::toString
    );

    Author author = authorPaginator.show();
    if (author == null) {
      input.info("Cancelled.");
      return;
    }

    // Enter title
    String title = input.prompt("Title: ");
    if (title.isBlank()) {
      input.info("Cancelled.");
      return;
    }

    // Enter content
    String content = input.readMultilineContent("Content (enter empty line to finish):");
    if (content == null) {
      input.info("Cancelled - content cannot be empty.");
      return;
    }

    try {
      DiaryEntry entry = entryService.createEntry(title, author, content);
      input.success("Created: " + entry);
    } catch (IllegalArgumentException e) {
      input.error("Error: " + e.getMessage());
    }
  }

  /**
   * Searches entries by title or content.
   */
  private void searchEntries() {
    input.info("\n== Search Entries ==");

    String searchTerm = input.prompt("Search term: ");
    if (searchTerm.isBlank()) {
      return;
    }

    List<DiaryEntry> results = entryService.search(searchTerm);

    if (results.isEmpty()) {
      input.info("No entries found matching: " + searchTerm);
      return;
    }

    Paginator<DiaryEntry> paginator = new Paginator<>(
        results,
        scanner,
        "Search Results",
        entry -> entry.getTitle() + " - " + entry.getAuthor().getFullName()
    );

    DiaryEntry selected = paginator.show();
    if (selected != null) {
      showEntryActions(selected);
    }
  }

  /**
   * Shows action menu for a selected entry.
   *
   * @param entry the selected entry
   */
  private void showEntryActions(DiaryEntry entry) {
    MenuView actionsMenu = new MenuView("== Entry: " + entry.getTitle() + " ==", scanner);
    actionsMenu.addOption(new MenuOption("View Full Entry", () -> viewFullEntry(entry)));
    actionsMenu.addOption(new MenuOption("Edit Entry", () -> editEntry(entry)));
    actionsMenu.addOption(new MenuOption("Delete Entry", AnsiColors.RED, () -> deleteEntry(entry)));
    actionsMenu.show();
  }

  /**
   * Displays full entry details.
   *
   * @param entry the entry to display
   */
  private void viewFullEntry(DiaryEntry entry) {
    input.info("\n" + "=".repeat(50));
    input.info("Title:   " + entry.getTitle());
    input.info("Author:  " + entry.getAuthor().getFullName());
    input.info("Created: " + entry.getCreatedAt());
    input.info("Updated: " + entry.getUpdatedAt());
    input.info("=".repeat(50));
    input.info("\n" + entry.getContent());
    input.info("\n" + "=".repeat(50));

    input.pause();
  }

  /**
   * Edits an existing entry.
   *
   * @param entry the entry to edit
   */
  private void editEntry(DiaryEntry entry) {
    input.info("\n== Edit Entry: " + entry.getTitle() + " ==");
    input.info("(Leave empty to keep current value)\n");

    boolean changed = false;

    // Edit title
    String title = input.prompt("Title [" + entry.getTitle() + "]: ");
    if (!title.isEmpty() && !title.equals(entry.getTitle())) {
      try {
        entry.setTitle(title);
        changed = true;
      } catch (IllegalArgumentException e) {
        input.error(e.getMessage());
      }
    }

    // Edit content
    input.info("\nCurrent content preview: " + input.truncate(entry.getContent(), 100));

    if (input.confirm("Replace content?")) {
      String newContent = input.readMultilineContent("New content (enter empty line to finish):");
      if (newContent != null) {
        try {
          entry.setContent(newContent);
          changed = true;
        } catch (IllegalArgumentException e) {
          input.error(e.getMessage());
        }
      }
    }

    if (changed) {
      entryService.update(entry);
      input.success("Entry updated.");
    } else {
      input.info("No changes made.");
    }
  }

  /**
   * Deletes an entry after confirmation.
   *
   * @param entry the entry to delete
   */
  private void deleteEntry(DiaryEntry entry) {
    if (input.confirm("Delete entry '" + entry.getTitle() + "'?")) {
      entryService.delete(entry);
      input.success("Entry deleted.");
    } else {
      input.info("Cancelled.");
    }
  }

  /**
   * Creates an author inline during entry creation.
   *
   * @return the created author, or null if cancelled
   */
  private Author createAuthorInline() {
    input.info("\n== Quick Author Creation ==");

    String firstName = input.prompt("First name: ");
    if (firstName.isBlank()) {
      input.info("Cancelled.");
      return null;
    }

    String lastName = input.prompt("Last name: ");
    if (lastName.isBlank()) {
      input.info("Cancelled.");
      return null;
    }

    String email = input.prompt("Email: ");
    if (email.isBlank()) {
      input.info("Cancelled.");
      return null;
    }

    return authorService.createAuthor(firstName, lastName, email)
        .orElseGet(() -> {
          input.error("Email already exists.");
          return null;
        });
  }
}
