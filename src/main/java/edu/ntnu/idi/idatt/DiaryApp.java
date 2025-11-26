package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.Author;
import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import edu.ntnu.idi.idatt.ui.AnsiColors;
import edu.ntnu.idi.idatt.ui.DiarySystemBanner;
import edu.ntnu.idi.idatt.ui.Menu;
import edu.ntnu.idi.idatt.ui.MenuOption;
import edu.ntnu.idi.idatt.util.HibernateUtil;
import edu.ntnu.idi.idatt.util.InputHelper;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class for the Diary System.
 */
public class DiaryApp {

  static {
    // Silence Hibernate logging
    Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
  }

  private Menu menu;
  private Scanner scanner;
  private AuthorRepository authorRepository;
  private DiaryEntryRepository diaryEntryRepository;

  /**
   * Initializes the application components.
   */
  public void init() {
    this.scanner = new Scanner(System.in);
    this.authorRepository = new AuthorRepository();
    this.diaryEntryRepository = new DiaryEntryRepository();

    // Initialize database connection early
    HibernateUtil.getSessionFactory();

    // Register shutdown hook for Ctrl+C
    Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));

    buildMenu();
  }

  /**
   * Builds the main menu with submenus.
   */
  private void buildMenu() {
    this.menu = new Menu("", this.scanner);

    menu.addOption(new MenuOption("Diary Entries", buildEntriesMenu()));
    menu.addOption(new MenuOption("Search Entries", buildSearchMenu()));
    menu.addOption(new MenuOption("Authors", buildAuthorsMenu()));
    menu.addOption(new MenuOption("Statistics", this::showStatistics));
    menu.addOption(new MenuOption("Exit", AnsiColors.RED, this::exit));
  }

  /**
   * Builds the diary entries submenu.
   *
   * @return the entries submenu
   */
  private Menu buildEntriesMenu() {
    Menu entriesMenu = new Menu("== Diary Entries ==", this.scanner);
    entriesMenu.addOption(new MenuOption("Create New Entry", this::createEntry));
    entriesMenu.addOption(new MenuOption("List All Entries", this::listEntries));
    entriesMenu.addOption(new MenuOption("Back", AnsiColors.RED, () -> {}));
    entriesMenu.setExitOnChoice(true);
    return entriesMenu;
  }

  /**
   * Builds the search submenu.
   *
   * @return the search submenu
   */
  private Menu buildSearchMenu() {
    Menu searchMenu = new Menu("== Search Entries ==", this.scanner);
    searchMenu.addOption(new MenuOption("Search by Date", this::searchByDate));
    searchMenu.addOption(new MenuOption("Search by Date Range", this::searchByDateRange));
    searchMenu.addOption(new MenuOption("Search by Author", this::searchByAuthor));
    searchMenu.addOption(new MenuOption("Search by Keyword", this::searchByKeyword));
    searchMenu.addOption(new MenuOption("Back", AnsiColors.RED, () -> {}));
    searchMenu.setExitOnChoice(true);
    return searchMenu;
  }

  /**
   * Builds the authors submenu.
   *
   * @return the authors submenu
   */
  private Menu buildAuthorsMenu() {
    Menu authorsMenu = new Menu("== Authors ==", this.scanner);
    authorsMenu.addOption(new MenuOption("List Authors", this::listAuthors));
    authorsMenu.addOption(new MenuOption("Add New Author", this::createAuthor));
    authorsMenu.addOption(new MenuOption("Back", AnsiColors.RED, () -> {}));
    authorsMenu.setExitOnChoice(true);
    return authorsMenu;
  }

  /**
   * Starts the application.
   */
  public void start() {
    System.out.println(DiarySystemBanner.getColoredBanner());

    if (menu != null) {
      menu.show();
    }
  }

  // --- Diary Entry Actions ---

  private void createEntry() {
    System.out.println("Create Entry (not implemented)");
  }

  private void listEntries() {
    System.out.println("List Entries (not implemented)");
  }

  // --- Search Actions ---

  private void searchByDate() {
    System.out.println("Search by Date (not implemented)");
  }

  private void searchByDateRange() {
    System.out.println("Search by Date Range (not implemented)");
  }

  private void searchByAuthor() {
    System.out.println("Search by Author (not implemented)");
  }

  private void searchByKeyword() {
    System.out.println("Search by Keyword (not implemented)");
  }

  // --- Author Actions ---

  /**
   * Lists all authors in the database.
   */
  private void listAuthors() {
    System.out.println("\n== All Authors ==");

    var authors = authorRepository.findAll();
    if (authors.isEmpty()) {
      System.out.println("No authors found.");
      return;
    }

    for (int i = 0; i < authors.size(); i++) {
      var author = authors.get(i);
      System.out.printf("%d. %s%n", i + 1, author);
    }
    System.out.println("\nTotal: " + authors.size() + " author(s)");
  }

  /**
   * Prompts user to create a new author with per-field validation.
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

      // Check for duplicate
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

  // --- Statistics ---

  private void showStatistics() {
    System.out.println("Statistics (not implemented)");
  }

  // --- Application Lifecycle ---

  private void exit() {
    System.out.println("Exiting...");
    System.exit(0);  // Shutdown hook will call cleanup()
  }

  /**
   * Cleans up resources before exit.
   */
  private void cleanup() {
    if (scanner != null) {
      scanner.close();
    }
    HibernateUtil.shutdown();
  }

}

