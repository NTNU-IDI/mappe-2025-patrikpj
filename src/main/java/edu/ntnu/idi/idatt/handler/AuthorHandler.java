package edu.ntnu.idi.idatt.handler;

import edu.ntnu.idi.idatt.model.Author;
import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import edu.ntnu.idi.idatt.ui.AnsiColors;
import edu.ntnu.idi.idatt.ui.Menu;
import edu.ntnu.idi.idatt.ui.MenuOption;
import edu.ntnu.idi.idatt.util.InputHelper;
import edu.ntnu.idi.idatt.util.Paginator;
import java.util.Scanner;

/**
 * Handler for all author-related menu actions.
 */
public class AuthorHandler {

  private final Scanner scanner;
  private final AuthorRepository authorRepository;
  private final DiaryEntryRepository diaryEntryRepository;

  /**
   * Creates a new AuthorHandler.
   *
   * @param scanner              the scanner for user input
   * @param authorRepository     the author repository
   * @param diaryEntryRepository the diary entry repository
   */
  public AuthorHandler(Scanner scanner, AuthorRepository authorRepository,
      DiaryEntryRepository diaryEntryRepository) {
    this.scanner = scanner;
    this.authorRepository = authorRepository;
    this.diaryEntryRepository = diaryEntryRepository;
  }

  /**
   * Builds and returns the authors submenu.
   *
   * @return the authors menu
   */
  public Menu getMenu() {
    Menu authorsMenu = new Menu("== Authors ==", scanner);
    authorsMenu.addOption(new MenuOption("List Authors", this::listAuthors));
    authorsMenu.addOption(new MenuOption("Add New Author", this::createAuthor));
    authorsMenu.addOption(new MenuOption("Back", AnsiColors.RED, () -> {}));
    authorsMenu.setExitOnChoice(true);
    return authorsMenu;
  }

  /**
   * Lists all authors with pagination and selection.
   */
  private void listAuthors() {
    var authors = authorRepository.findAll();
    Author selected = new Paginator<>(authors, scanner, "All Authors", Author::toString).show();

    if (selected != null) {
      showAuthorActions(selected);
    }
  }

  /**
   * Shows action menu for a selected author.
   *
   * @param author the selected author
   */
  private void showAuthorActions(Author author) {
    Menu actionsMenu = new Menu("== " + author.getFullName() + " ==", scanner);
    actionsMenu.addOption(new MenuOption("View Entries", () -> viewEntriesByAuthor(author)));
    actionsMenu.addOption(new MenuOption("Edit Author", () -> editAuthor(author)));
    actionsMenu.addOption(new MenuOption("Delete Author", () -> deleteAuthor(author)));
    actionsMenu.addOption(new MenuOption("Back", AnsiColors.RED, () -> {}));
    actionsMenu.setExitOnChoice(true);
    actionsMenu.show();
  }

  /**
   * Shows all diary entries by a specific author.
   *
   * @param author the author
   */
  private void viewEntriesByAuthor(Author author) {
    var entries = diaryEntryRepository.findByAuthor(author);
    if (entries.isEmpty()) {
      System.out.println("\nNo entries found for " + author.getFullName());
      return;
    }
    new Paginator<>(entries, scanner, "Entries by " + author.getFullName(),
        e -> e.getTitle()).showReadOnly();
  }

  /**
   * Prompts user to create a new author.
   */
  private void createAuthor() {
    System.out.println("\n== Add New Author ==");

    String firstName = InputHelper.promptValidatedInput(scanner, "First name: ",
        InputHelper::validateNotBlank);
    if (firstName == null) {
      return;
    }

    String lastName = InputHelper.promptValidatedInput(scanner, "Last name: ",
        InputHelper::validateNotBlank);
    if (lastName == null) {
      return;
    }

    String email = promptUniqueEmail();
    if (email == null) {
      return;
    }

    Author author = new Author(firstName, lastName, email);
    authorRepository.save(author);
    System.out.println(AnsiColors.GREEN + "Author created: " + author + AnsiColors.RESET);
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

    System.out.print("First name [" + author.getFirstName() + "]: ");
    String firstName = scanner.nextLine().trim();
    if (!firstName.isEmpty() && !firstName.equals(author.getFirstName())) {
      author.setFirstName(firstName);
      changed = true;
    }

    System.out.print("Last name [" + author.getLastName() + "]: ");
    String lastName = scanner.nextLine().trim();
    if (!lastName.isEmpty() && !lastName.equals(author.getLastName())) {
      author.setLastName(lastName);
      changed = true;
    }

    while (true) {
      System.out.print("Email [" + author.getEmail() + "]: ");
      String email = scanner.nextLine().trim();

      if (email.isEmpty()) {
        break; // Keep current value
      }

      if (email.equalsIgnoreCase(author.getEmail())) {
        break; // Same as current, no change needed
      }

      if (!Author.isValidEmail(email)) {
        System.out.println(AnsiColors.RED + "Invalid email format. Try again." + AnsiColors.RESET);
        continue;
      }

      if (authorRepository.findByEmail(email).isPresent()) {
        System.out.println(AnsiColors.RED + "Email already in use. Try again." + AnsiColors.RESET);
        continue;
      }

      author.setEmail(email);
      changed = true;
      break;
    }

    if (changed) {
      authorRepository.update(author);
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
    System.out.print("\nDelete " + author.getFullName() + "? (y/n): ");
    String confirm = scanner.nextLine().trim().toLowerCase();

    if (confirm.equals("y") || confirm.equals("yes")) {
      authorRepository.delete(author);
      System.out.println(AnsiColors.GREEN + "Author deleted." + AnsiColors.RESET);
    } else {
      System.out.println("Cancelled.");
    }
  }

  /**
   * Prompts for email with format validation and duplicate checking.
   *
   * @return the valid unique email, or null if cancelled
   */
  private String promptUniqueEmail() {
    while (true) {
      String email = InputHelper.promptEmail(scanner);
      if (email == null) {
        return null;
      }

      if (authorRepository.findByEmail(email).isPresent()) {
        System.out.println(AnsiColors.RED + "An author with this email already exists"
            + AnsiColors.RESET);
        if (!InputHelper.promptRetry(scanner)) {
          return null;
        }
        continue;
      }

      return email;
    }
  }
}

