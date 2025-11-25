package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import edu.ntnu.idi.idatt.ui.AnsiColors;
import edu.ntnu.idi.idatt.ui.DiarySystemBanner;
import edu.ntnu.idi.idatt.ui.Menu;
import edu.ntnu.idi.idatt.ui.MenuOption;
import edu.ntnu.idi.idatt.util.HibernateUtil;
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
    Menu entriesMenu = new Menu("Diary Entries", this.scanner);
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
    Menu searchMenu = new Menu("Search Entries", this.scanner);
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
    Menu authorsMenu = new Menu("Authors", this.scanner);
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

  private void listAuthors() {
    System.out.println("List Authors (not implemented)");
  }

  private void createAuthor() {
    System.out.println("Create Author (not implemented)");
  }

  // --- Statistics ---

  private void showStatistics() {
    System.out.println("Statistics (not implemented)");
  }

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

