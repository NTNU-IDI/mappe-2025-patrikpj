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

    buildMenu();
  }

  /**
   * Builds the main menu with options.
   */
  private void buildMenu() {
    this.menu = new Menu("", this.scanner);

    menu.addOption(new MenuOption("List Entries", this::listEntries));
    menu.addOption(new MenuOption("Create Entry", this::createEntry));
    menu.addOption(new MenuOption("Create Author", this::createAuthor));
    menu.addOption(new MenuOption("List Authors", this::listAuthors));
    menu.addOption(new MenuOption("Exit", AnsiColors.RED, this::exit));
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

  // Menu actions (to be implemented)

  private void listEntries() {
    System.out.println("List Entries selected (not implemented)");
  }

  private void createEntry() {
    System.out.println("Create Entry selected (not implemented)");
  }

  private void createAuthor() {
    System.out.println("Create Author selected (not implemented)");
  }

  private void listAuthors() {
    System.out.println("List Authors selected (not implemented)");
  }

  private void exit() {
    System.out.println("Exiting...");
    cleanup();
    System.exit(0);
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

