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
import java.util.Optional;
import java.util.Scanner;

/**
 * Controller for author-related menu actions.
 */
public class AuthorController {

  private final AuthorService authorService;
  private final DiaryEntryService entryService;
  private final Scanner scanner;
  private final InputHelper input;

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
    this.input = new InputHelper(scanner);
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
    input.info("\n== Create Author ==");

    String firstName = input.prompt("First name: ");
    if (firstName.isBlank()) {
      input.info("Cancelled.");
      return;
    }

    String lastName = input.prompt("Last name: ");
    if (lastName.isBlank()) {
      input.info("Cancelled.");
      return;
    }

    String email = promptUniqueEmail();
    if (email == null) {
      input.info("Cancelled.");
      return;
    }

    try {
      Optional<Author> result = authorService.createAuthor(firstName, lastName, email);
      result.ifPresentOrElse(
          author -> input.success("Created: " + author),
          () -> input.error("Email already exists.")
      );
    } catch (IllegalArgumentException e) {
      input.error("Error: " + e.getMessage());
    }
  }

  /**
   * Finds an author by email.
   */
  private void findByEmail() {
    input.info("\n== Find Author by Email ==");

    String email = input.prompt("Email: ");
    if (email.isBlank()) {
      return;
    }

    authorService.findByEmail(email).ifPresentOrElse(
        this::showAuthorActions,
        () -> input.info("Author not found.")
    );
  }

  /**
   * Shows action menu for a selected author.
   *
   * @param author the selected author
   */
  private void showAuthorActions(Author author) {
    MenuView actionsMenu = new MenuView("== Author: " + author.getFullName() + " ==", scanner);
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
    input.info("\n== Author Details ==");
    input.info("Name:    " + author.getFullName());
    input.info("Email:   " + author.getEmail());
    input.info("Created: " + author.getCreatedAt());
    input.info("Updated: " + author.getUpdatedAt());

    long entryCount = entryService.countByAuthorId(author.getId());
    input.info("Entries: " + entryCount);

    input.pause();
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
        entry -> entry.getTitle() + " - " + input.truncate(entry.getContent(), 40)
    );

    paginator.showReadOnly();
  }

  /**
   * Edits an existing author.
   *
   * @param author the author to edit
   */
  private void editAuthor(Author author) {
    input.info("\n== Edit Author: " + author.getFullName() + " ==");
    input.info("(Leave empty to keep current value)\n");

    boolean changed = false;

    // First name
    String firstName = input.prompt("First name [" + author.getFirstName() + "]: ");
    if (!firstName.isEmpty() && !firstName.equals(author.getFirstName())) {
      try {
        author.setFirstName(firstName);
        changed = true;
      } catch (IllegalArgumentException e) {
        input.error(e.getMessage());
      }
    }

    // Last name
    String lastName = input.prompt("Last name [" + author.getLastName() + "]: ");
    if (!lastName.isEmpty() && !lastName.equals(author.getLastName())) {
      try {
        author.setLastName(lastName);
        changed = true;
      } catch (IllegalArgumentException e) {
        input.error(e.getMessage());
      }
    }

    // Email
    while (true) {
      String email = input.prompt("Email [" + author.getEmail() + "]: ");

      if (email.isEmpty()) {
        break; // Keep current value
      }

      if (email.equalsIgnoreCase(author.getEmail())) {
        break; // Same as current
      }

      if (!Author.isValidEmail(email)) {
        input.error("Invalid email format.");
        continue;
      }

      if (authorService.emailExists(email)) {
        input.error("Email already in use.");
        continue;
      }

      author.setEmail(email);
      changed = true;
      break;
    }

    if (changed) {
      authorService.update(author);
      input.success("Author updated: " + author);
    } else {
      input.info("No changes made.");
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
      input.error("\nCannot delete: author has " + entryCount + " entries.");
      return;
    }

    if (input.confirm("Delete " + author.getFullName() + "?")) {
      authorService.delete(author);
      input.success("Author deleted.");
    } else {
      input.info("Cancelled.");
    }
  }

  /**
   * Prompts for a unique email with format validation.
   *
   * @return the valid unique email, or null if cancelled
   */
  private String promptUniqueEmail() {
    while (true) {
      String email = input.prompt("Email: ");

      if (email.isBlank()) {
        return null;
      }

      if (!Author.isValidEmail(email)) {
        input.error("Invalid email format.");
        if (!input.confirm("Try again?")) {
          return null;
        }
        continue;
      }

      if (authorService.emailExists(email)) {
        input.error("Email already exists.");
        if (!input.confirm("Try again?")) {
          return null;
        }
        continue;
      }

      return email;
    }
  }
}
