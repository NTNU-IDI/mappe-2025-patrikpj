package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.service.AuthorService;
import edu.ntnu.idi.idatt.view.author.AuthorMenuView;
import edu.ntnu.idi.idatt.view.author.AuthorView;
import edu.ntnu.idi.idatt.view.author.CreateAuthorView;
import edu.ntnu.idi.idatt.view.author.EditAuthorView;
import edu.ntnu.idi.idatt.view.author.FindAuthorView;
import edu.ntnu.idi.idatt.view.author.ListAuthorView;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Controller for author-related actions. Handles viewing and managing authors.
 */
public class AuthorController {

  private final AuthorService authorService;
  private final AuthorMenuView view;
  private final ListAuthorView listAuthorView;
  private final AuthorView authorView;
  private final CreateAuthorView createAuthorView;
  private final FindAuthorView findAuthorView;
  private final EditAuthorView editAuthorView;

  // Navigation reference
  private MainMenuController mainMenuController;

  /**
   * Creates a new AuthorController.
   *
   * @param authorService    the author service
   * @param view             the author menu view
   * @param listAuthorView   the list authors view
   * @param authorView       the author detail view
   * @param createAuthorView the create author view
   * @param findAuthorView   the find author view
   * @param editAuthorView   the edit author view
   */
  public AuthorController(AuthorService authorService, AuthorMenuView view,
      ListAuthorView listAuthorView, AuthorView authorView,
      CreateAuthorView createAuthorView, FindAuthorView findAuthorView,
      EditAuthorView editAuthorView) {
    this.authorService = authorService;
    this.view = view;
    this.listAuthorView = listAuthorView;
    this.authorView = authorView;
    this.createAuthorView = createAuthorView;
    this.findAuthorView = findAuthorView;
    this.editAuthorView = editAuthorView;
  }

  /**
   * Sets the main menu controller for navigation.
   *
   * @param mainMenuController the main menu controller
   */
  public void setMainMenuController(MainMenuController mainMenuController) {
    this.mainMenuController = mainMenuController;
  }

  /**
   * Shows the author screen.
   *
   * @param in  Scanner for user input
   * @param out PrintStream for output
   * @return the next action to execute
   */
  public Action showAuthorMenu(Scanner in, PrintStream out) {
    view.renderMenu(out);

    while (true) {
      String choice = in.nextLine().trim().toLowerCase();

      switch (choice) {
        case "1" -> {
          return this::showAuthorsList;
        }
        case "2" -> {
          return this::showCreateAuthor;
        }
        case "3" -> {
          return this::showFindAuthor;
        }
        case "b" -> {
          return (in2, out2) -> mainMenuController.showMenu(in2, out2);
        }
        default -> {
          view.showError("Invalid selection. Try again.", out);
          view.prompt(out);
        }
      }
    }
  }

  /**
   * Shows the create author form.
   *
   * @param in  Scanner for user input
   * @param out PrintStream for output
   * @return the next action to execute
   */
  public Action showCreateAuthor(Scanner in, PrintStream out) {
    createAuthorView.render(out);

    createAuthorView.promptFirstName(out);
    String firstName = in.nextLine().trim();
    if (firstName.isBlank()) {
      return this::showAuthorMenu;
    }

    createAuthorView.promptLastName(out);
    String lastName = in.nextLine().trim();
    if (lastName.isBlank()) {
      return this::showAuthorMenu;
    }

    // Retry loop for email validation
    String email;
    while (true) {
      createAuthorView.promptEmail(out);
      email = in.nextLine().trim();
      if (email.isBlank()) {
        return this::showAuthorMenu;
      }

      if (!Author.isValidEmail(email)) {
        createAuthorView.showError("Invalid email format, try again.", out);
        continue;
      }

      if (authorService.emailExists(email)) {
        createAuthorView.showError("Email already exists, try again.", out);
        continue;
      }

      break;
    }

    Author author = authorService.createAuthorOrThrow(firstName, lastName, email);
    createAuthorView.showCreated(author.getFullName(), out);

    createAuthorView.promptContinue(out);
    in.nextLine();
    return this::showAuthorMenu;
  }

  /**
   * Shows the find author by email form.
   *
   * @param in  Scanner for user input
   * @param out PrintStream for output
   * @return the next action to execute
   */
  public Action showFindAuthor(Scanner in, PrintStream out) {
    findAuthorView.render(out);

    // Retry loop for email validation and search
    while (true) {
      findAuthorView.promptEmail(out);
      String email = in.nextLine().trim();

      // Empty input cancels
      if (email.isBlank()) {
        return this::showAuthorMenu;
      }

      // Validate email format
      if (!Author.isValidEmail(email)) {
        findAuthorView.showError("Invalid email format. Try again.", out);
        continue;
      }

      // Search for author
      Optional<Author> result = authorService.findByEmail(email);

      if (result.isPresent()) {
        Author found = result.get();
        // Back from detail should return to author menu (not list)
        return (in2, out2) -> showAuthorDetail(found, this::showAuthorMenu, in2, out2);
      } else {
        // Not found - show warning and let user try again
        findAuthorView.showNotFound(email, out);
      }
    }
  }

  /**
   * Shows the list of all authors.
   *
   * @param in  Scanner for user input
   * @param out PrintStream for output
   * @return the next action to execute
   */
  public Action showAuthorsList(Scanner in, PrintStream out) {
    List<Author> authors = authorService.findAll();
    listAuthorView.render(authors, out);

    while (true) {
      String choice = in.nextLine().trim().toLowerCase();

      if (choice.equals("b")) {
        return this::showAuthorMenu;
      }

      // Try to parse as number for author selection
      try {
        int index = Integer.parseInt(choice) - 1;
        if (index >= 0 && index < authors.size()) {
          Author selected = authors.get(index);
          // Back from detail should return to list
          return (in2, out2) -> showAuthorDetail(selected, this::showAuthorsList, in2, out2);
        }
      } catch (NumberFormatException ignored) {
        // Fall through to error
      }

      listAuthorView.showError("Invalid selection. Try again.", out);
      listAuthorView.prompt(out);
    }
  }

  /**
   * Shows details for a selected author.
   *
   * @param author          the selected author
   * @param backDestination where to navigate when user presses back
   * @param in              Scanner for user input
   * @param out             PrintStream for output
   * @return the next action to execute
   */
  public Action showAuthorDetail(Author author, Action backDestination, Scanner in,
      PrintStream out) {
    authorView.render(author, out);

    while (true) {
      String choice = in.nextLine().trim().toLowerCase();

      switch (choice) {
        case "1" -> {
          return (in2, out2) -> viewAuthorEntries(author, backDestination, in2, out2);
        }
        case "2" -> {
          return (in2, out2) -> editAuthor(author, backDestination, in2, out2);
        }
        case "3" -> {
          return (in2, out2) -> deleteAuthor(author, backDestination, in2, out2);
        }
        case "b" -> {
          return backDestination;
        }
        default -> {
          authorView.showError("Invalid selection. Try again.", out);
          authorView.prompt(out);
        }
      }
    }
  }

  /**
   * Shows entries by the selected author.
   *
   * @param author          the author whose entries to view
   * @param backDestination where to navigate when returning from author detail
   */
  private Action viewAuthorEntries(Author author, Action backDestination, Scanner in,
      PrintStream out) {
    // TODO: Implement view entries
    authorView.showNotImplemented("View entries for " + author.getFullName(), out);
    authorView.promptContinue(out);
    in.nextLine();
    return (in2, out2) -> showAuthorDetail(author, backDestination, in2, out2);
  }

  /**
   * Edits the selected author's name.
   *
   * @param author          the author to edit
   * @param backDestination where to navigate when returning from author detail
   */
  private Action editAuthor(Author author, Action backDestination, Scanner in, PrintStream out) {
    editAuthorView.render(author, out);

    // Collect new values
    String newFirstName = null;
    String newLastName = null;

    // First name
    editAuthorView.promptFirstName(author.getFirstName(), out);
    String firstNameInput = in.nextLine().trim();
    if (!firstNameInput.isEmpty()) {
      if (firstNameInput.isBlank()) {
        editAuthorView.showError("First name cannot be blank.", out);
      } else {
        newFirstName = firstNameInput;
      }
    }

    // Last name
    editAuthorView.promptLastName(author.getLastName(), out);
    String lastNameInput = in.nextLine().trim();
    if (!lastNameInput.isEmpty()) {
      if (lastNameInput.isBlank()) {
        editAuthorView.showError("Last name cannot be blank.", out);
      } else {
        newLastName = lastNameInput;
      }
    }

    // Apply changes if any
    if (newFirstName == null && newLastName == null) {
      editAuthorView.showNoChanges(out);
    } else {
      if (newFirstName != null) {
        author.setFirstName(newFirstName);
      }
      if (newLastName != null) {
        author.setLastName(newLastName);
      }
      authorService.update(author);
      editAuthorView.showUpdated(author.getFullName(), out);
    }

    editAuthorView.promptContinue(out);
    in.nextLine();
    return (in2, out2) -> showAuthorDetail(author, backDestination, in2, out2);
  }

  /**
   * Deletes the selected author after confirmation.
   *
   * @param author          the author to delete
   * @param backDestination where to navigate after deletion (back to list or menu)
   */
  private Action deleteAuthor(Author author, Action backDestination, Scanner in, PrintStream out) {
    authorView.promptDeleteConfirmation(author.getFullName(), out);
    String confirmation = in.nextLine().trim().toLowerCase();

    if (confirmation.equals("yes")) {
      authorService.delete(author);
      authorView.showDeleted(out);
      authorView.promptContinue(out);
      in.nextLine();
      // After delete, go back to where we came from
      return backDestination;
    } else {
      authorView.showCancelled(out);
      authorView.promptContinue(out);
      in.nextLine();
      return (in2, out2) -> showAuthorDetail(author, backDestination, in2, out2);
    }
  }
}

