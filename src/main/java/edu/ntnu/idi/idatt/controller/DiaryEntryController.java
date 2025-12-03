package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.service.AuthorService;
import edu.ntnu.idi.idatt.service.DiaryEntryService;
import edu.ntnu.idi.idatt.view.components.AnsiColors;
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
    System.out.println("\n== Create Entry ==");

    // Select author
    List<Author> authors = authorService.findAll();
    if (authors.isEmpty()) {
      System.out.println(AnsiColors.YELLOW + "No authors found." + AnsiColors.RESET);
      if (confirm("Create an author now?")) {
        Author newAuthor = createAuthorInline();
        if (newAuthor == null) {
          return;
        }
        authors = List.of(newAuthor);
      } else {
        return;
      }
    }

    System.out.println("\nSelect an author:");
    Paginator<Author> authorPaginator = new Paginator<>(
        authors,
        scanner,
        "Select Author",
        Author::toString
    );

    Author author = authorPaginator.show();
    if (author == null) {
      System.out.println("Cancelled.");
      return;
    }

    // Enter title
    String title = promptInput("Title: ");
    if (title.isBlank()) {
      System.out.println("Cancelled.");
      return;
    }

    // Enter content
    System.out.println("Content (enter empty line to finish):");
    StringBuilder contentBuilder = new StringBuilder();
    String line;
    while (!(line = scanner.nextLine()).isEmpty()) {
      contentBuilder.append(line).append("\n");
    }

    String content = contentBuilder.toString().trim();
    if (content.isEmpty()) {
      System.out.println("Cancelled - content cannot be empty.");
      return;
    }

    try {
      DiaryEntry entry = entryService.createEntry(title, author, content);
      System.out.println(AnsiColors.GREEN + "Created: " + entry + AnsiColors.RESET);
    } catch (IllegalArgumentException e) {
      System.out.println(AnsiColors.RED + "Error: " + e.getMessage() + AnsiColors.RESET);
    }
  }

  /**
   * Searches entries by title or content.
   */
  private void searchEntries() {
    System.out.println("\n== Search Entries ==");

    String searchTerm = promptInput("Search term: ");
    if (searchTerm.isBlank()) {
      return;
    }

    List<DiaryEntry> results = entryService.search(searchTerm);

    if (results.isEmpty()) {
      System.out.println("No entries found matching: " + searchTerm);
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
    MenuView actionsMenu = new MenuView("== " + entry.getTitle() + " ==", scanner);
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
    System.out.println("\n" + "=".repeat(50));
    System.out.println("Title:   " + entry.getTitle());
    System.out.println("Author:  " + entry.getAuthor().getFullName());
    System.out.println("Created: " + entry.getCreatedAt());
    System.out.println("Updated: " + entry.getUpdatedAt());
    System.out.println("=".repeat(50));
    System.out.println("\n" + entry.getContent());
    System.out.println("\n" + "=".repeat(50));

    promptInput("\nPress Enter to continue...");
  }

  /**
   * Edits an existing entry.
   *
   * @param entry the entry to edit
   */
  private void editEntry(DiaryEntry entry) {
    System.out.println("\n== Edit Entry: " + entry.getTitle() + " ==");
    System.out.println("(Leave empty to keep current value)\n");

    boolean changed = false;

    // Edit title
    System.out.print("Title [" + entry.getTitle() + "]: ");
    String title = scanner.nextLine().trim();
    if (!title.isEmpty() && !title.equals(entry.getTitle())) {
      try {
        entry.setTitle(title);
        changed = true;
      } catch (IllegalArgumentException e) {
        System.out.println(AnsiColors.RED + e.getMessage() + AnsiColors.RESET);
      }
    }

    // Edit content
    System.out.println("\nCurrent content preview: " + truncate(entry.getContent(), 100));
    System.out.print("Replace content? (y/n): ");
    String replaceContent = scanner.nextLine().trim().toLowerCase();

    if (replaceContent.equals("y") || replaceContent.equals("yes")) {
      System.out.println("New content (enter empty line to finish):");
      StringBuilder contentBuilder = new StringBuilder();
      String line;
      while (!(line = scanner.nextLine()).isEmpty()) {
        contentBuilder.append(line).append("\n");
      }

      String newContent = contentBuilder.toString().trim();
      if (!newContent.isEmpty()) {
        try {
          entry.setContent(newContent);
          changed = true;
        } catch (IllegalArgumentException e) {
          System.out.println(AnsiColors.RED + e.getMessage() + AnsiColors.RESET);
        }
      }
    }

    if (changed) {
      entryService.update(entry);
      System.out.println(AnsiColors.GREEN + "Entry updated." + AnsiColors.RESET);
    } else {
      System.out.println("No changes made.");
    }
  }

  /**
   * Deletes an entry after confirmation.
   *
   * @param entry the entry to delete
   */
  private void deleteEntry(DiaryEntry entry) {
    if (confirm("Delete entry '" + entry.getTitle() + "'?")) {
      entryService.delete(entry);
      System.out.println(AnsiColors.GREEN + "Entry deleted." + AnsiColors.RESET);
    } else {
      System.out.println("Cancelled.");
    }
  }

  /**
   * Prompts for input.
   *
   * @param prompt the prompt message
   * @return the user input (trimmed)
   */
  private String promptInput(String prompt) {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }

  /**
   * Prompts for yes/no confirmation.
   *
   * @param message the confirmation message
   * @return true if user confirms
   */
  private boolean confirm(String message) {
    System.out.print(message + " (y/n): ");
    String input = scanner.nextLine().trim().toLowerCase();
    return input.equals("y") || input.equals("yes");
  }

  /**
   * Creates an author inline during entry creation.
   *
   * @return the created author, or null if cancelled
   */
  private Author createAuthorInline() {
    System.out.println("\n== Quick Author Creation ==");

    String firstName = promptInput("First name: ");
    if (firstName.isBlank()) {
      System.out.println("Cancelled.");
      return null;
    }

    String lastName = promptInput("Last name: ");
    if (lastName.isBlank()) {
      System.out.println("Cancelled.");
      return null;
    }

    String email = promptInput("Email: ");
    if (email.isBlank()) {
      System.out.println("Cancelled.");
      return null;
    }

    return authorService.createAuthor(firstName, lastName, email)
        .orElseGet(() -> {
          System.out.println(AnsiColors.RED + "Email already exists." + AnsiColors.RESET);
          return null;
        });
  }

  /**
   * Truncates a string to the specified length.
   *
   * @param text      the text to truncate
   * @param maxLength the maximum length
   * @return the truncated text
   */
  private String truncate(String text, int maxLength) {
    String singleLine = text.replace("\n", " ");
    if (singleLine.length() <= maxLength) {
      return singleLine;
    }
    return singleLine.substring(0, maxLength) + "...";
  }
}
