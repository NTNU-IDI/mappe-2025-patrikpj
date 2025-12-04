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
import java.util.Optional;
import java.util.Scanner;

/**
 * Controller for author-related menu actions.
 */
public class AuthorController {

  private final AuthorService authorService;
  private final DiaryEntryService entryService;
  private final Scanner scanner;

  /**
   * Creates a new AuthorController.
   *
   * @param authorService the author service
   * @param entryService  the diary entry service
   * @param scanner       the scanner for user input
   * @throws NullPointerException if any argument is null
   */
  public AuthorController(AuthorService authorService,
                          DiaryEntryService entryService,
                          Scanner scanner) {
    this.authorService = Objects.requireNonNull(authorService, "AuthorService cannot be null");
    this.entryService = Objects.requireNonNull(entryService, "DiaryEntryService cannot be null");
    this.scanner = Objects.requireNonNull(scanner, "Scanner cannot be null");
  }

  /**
   * Returns the author management menu.
   *
   * @return the authors menu
   */
  public MenuView getMenu() {
    MenuView menu = new MenuView("== Authors ==", scanner);
    menu.addOption(new MenuOption("List Authors", this::listAuthors));
    menu.addOption(new MenuOption("Create Author", this::createAuthor));
    menu.addOption(new MenuOption("Find by Email", this::findByEmail));
    return menu;
  }

  /**
   * Lists all authors and allows selection.
   */
  private void listAuthors() {
    List<Author> authors = authorService.findAll();

    Paginator<Author> paginator = new Paginator<>(
        authors,
        scanner,
        "All Authors",
        Author::toString
    );

    Author selected = paginator.show();
    if (selected != null) {
      showAuthorActions(selected);
    }
  }

  /**
   * Prompts user to create a new author.
   */
  private void createAuthor() {
    System.out.println("\n== Create Author ==");

    String firstName = promptInput("First name: ");
    if (firstName.isBlank()) {
      System.out.println("Cancelled.");
      return;
    }

    String lastName = promptInput("Last name: ");
    if (lastName.isBlank()) {
      System.out.println("Cancelled.");
      return;
    }

    String email = promptUniqueEmail();
    if (email == null) {
      System.out.println("Cancelled.");
      return;
    }

    try {
      Optional<Author> result = authorService.createAuthor(firstName, lastName, email);
      result.ifPresentOrElse(
          author -> System.out.println(AnsiColors.GREEN + "Created: " + author + AnsiColors.RESET),
          () -> System.out.println(AnsiColors.RED + "Email already exists." + AnsiColors.RESET)
      );
    } catch (IllegalArgumentException e) {
      System.out.println(AnsiColors.RED + "Error: " + e.getMessage() + AnsiColors.RESET);
    }
  }

  /**
   * Finds an author by email.
   */
  private void findByEmail() {
    System.out.println("\n== Find Author by Email ==");

    String email = promptInput("Email: ");
    if (email.isBlank()) {
      return;
    }

    authorService.findByEmail(email).ifPresentOrElse(
        this::showAuthorActions,
        () -> System.out.println("Author not found.")
    );
  }

  /**
   * Shows action menu for a selected author.
   *
   * @param author the selected author
   */
  private void showAuthorActions(Author author) {
    MenuView actionsMenu = new MenuView("== " + "Author: " + author.getFullName() + " ==", scanner);
    actionsMenu.addOption(new MenuOption("View Details", () -> viewDetails(author)));
    actionsMenu.addOption(new MenuOption("View Entries", () -> viewEntriesByAuthor(author)));
    actionsMenu.addOption(new MenuOption("Edit Author", () -> editAuthor(author)));
    actionsMenu.addOption(new MenuOption("Delete Author", AnsiColors.RED, () -> deleteAuthor(author)));
    actionsMenu.show();
  }

  /**
   * Displays author details.
   *
   * @param author the author
   */
  private void viewDetails(Author author) {
    System.out.println("\n== Author Details ==");
    System.out.println("Name:    " + author.getFullName());
    System.out.println("Email:   " + author.getEmail());
    System.out.println("Created: " + author.getCreatedAt());
    System.out.println("Updated: " + author.getUpdatedAt());

    long entryCount = entryService.countByAuthorId(author.getId());
    System.out.println("Entries: " + entryCount);
  }

  /**
   * Shows all diary entries by a specific author.
   *
   * @param author the author
   */
  private void viewEntriesByAuthor(Author author) {
    var entries = entryService.findByAuthor(author);

    Paginator<DiaryEntry> paginator = new Paginator<>(
        entries,
        scanner,
        "Entries by " + author.getFullName(),
        entry -> entry.getTitle() + " - " + truncate(entry.getContent(), 40)
    );

    paginator.showReadOnly();
  }

  /**
   * Edits an existing author.
   *
   * @param author the author to edit
   */
  private void editAuthor(Author author) {
    System.out.println("\n== Edit Author: " + author.getFullName() + " ==");
    System.out.println("(Leave empty to keep current value)\n");

    boolean changed = false;

    // First name
    System.out.print("First name [" + author.getFirstName() + "]: ");
    String firstName = scanner.nextLine().trim();
    if (!firstName.isEmpty() && !firstName.equals(author.getFirstName())) {
      try {
        author.setFirstName(firstName);
        changed = true;
      } catch (IllegalArgumentException e) {
        System.out.println(AnsiColors.RED + e.getMessage() + AnsiColors.RESET);
      }
    }

    // Last name
    System.out.print("Last name [" + author.getLastName() + "]: ");
    String lastName = scanner.nextLine().trim();
    if (!lastName.isEmpty() && !lastName.equals(author.getLastName())) {
      try {
        author.setLastName(lastName);
        changed = true;
      } catch (IllegalArgumentException e) {
        System.out.println(AnsiColors.RED + e.getMessage() + AnsiColors.RESET);
      }
    }

    // Email
    while (true) {
      System.out.print("Email [" + author.getEmail() + "]: ");
      String email = scanner.nextLine().trim();

      if (email.isEmpty()) {
        break; // Keep current value
      }

      if (email.equalsIgnoreCase(author.getEmail())) {
        break; // Same as current
      }

      if (!Author.isValidEmail(email)) {
        System.out.println(AnsiColors.RED + "Invalid email format." + AnsiColors.RESET);
        continue;
      }

      if (authorService.emailExists(email)) {
        System.out.println(AnsiColors.RED + "Email already in use." + AnsiColors.RESET);
        continue;
      }

      author.setEmail(email);
      changed = true;
      break;
    }

    if (changed) {
      authorService.update(author);
      System.out.println(AnsiColors.GREEN + "Author updated: " + author + AnsiColors.RESET);
    } else {
      System.out.println("No changes made.");
    }
  }

  /**
   * Deletes an author after confirmation.
   *
   * @param author the author to delete
   */
  private void deleteAuthor(Author author) {
    long entryCount = entryService.countByAuthorId(author.getId());

    if (entryCount > 0) {
      System.out.println(AnsiColors.RED + "\nCannot delete: author has " + entryCount
          + " entries." + AnsiColors.RESET);
      return;
    }

    if (confirm("Delete " + author.getFullName() + "?")) {
      authorService.delete(author);
      System.out.println(AnsiColors.GREEN + "Author deleted." + AnsiColors.RESET);
    } else {
      System.out.println("Cancelled.");
    }
  }

  /**
   * Prompts for a unique email with format validation.
   *
   * @return the valid unique email, or null if cancelled
   */
  private String promptUniqueEmail() {
    while (true) {
      String email = promptInput("Email: ");

      if (email.isBlank()) {
        return null;
      }

      if (!Author.isValidEmail(email)) {
        System.out.println(AnsiColors.RED + "Invalid email format." + AnsiColors.RESET);
        if (!confirm("Try again?")) {
          return null;
        }
        continue;
      }

      if (authorService.emailExists(email)) {
        System.out.println(AnsiColors.RED + "Email already exists." + AnsiColors.RESET);
        if (!confirm("Try again?")) {
          return null;
        }
        continue;
      }

      return email;
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
   * Truncates a string to the specified length.
   *
   * @param text      the text to truncate
   * @param maxLength the maximum length
   * @return the truncated text
   */
  private String truncate(String text, int maxLength) {
    if (text.length() <= maxLength) {
      return text;
    }
    return text.substring(0, maxLength) + "...";
  }
}
