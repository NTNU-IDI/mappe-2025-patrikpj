package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.handler.AuthorHandler;
import edu.ntnu.idi.idatt.handler.EntryHandler;
import edu.ntnu.idi.idatt.handler.SearchHandler;
import edu.ntnu.idi.idatt.handler.StatisticsHandler;
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
 * Handles initialization, main menu, and application lifecycle.
 */
public class DiaryApp {

  static {
    // Silence Hibernate logging
    Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
  }

  private Menu menu;
  private Scanner scanner;

  // Repositories
  private AuthorRepository authorRepository;
  private DiaryEntryRepository diaryEntryRepository;

  // Handlers
  private AuthorHandler authorHandler;
  private EntryHandler entryHandler;
  private SearchHandler searchHandler;
  private StatisticsHandler statisticsHandler;

  /**
   * Initializes the application components.
   */
  public void init() {
    this.scanner = new Scanner(System.in);
    this.authorRepository = new AuthorRepository();
    this.diaryEntryRepository = new DiaryEntryRepository();

    // Initialize handlers
    this.authorHandler = new AuthorHandler(scanner, authorRepository, diaryEntryRepository);
    this.entryHandler = new EntryHandler(scanner, authorRepository, diaryEntryRepository);
    this.searchHandler = new SearchHandler(scanner, authorRepository, diaryEntryRepository);
    this.statisticsHandler = new StatisticsHandler(scanner, authorRepository, diaryEntryRepository);

    // Initialize database connection early
    HibernateUtil.getSessionFactory();

    // Register shutdown hook for Ctrl+C
    Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));

    buildMenu();
  }

  /**
   * Builds the main menu with submenus from handlers.
   */
  private void buildMenu() {
    this.menu = new Menu("", this.scanner);

    menu.addOption(new MenuOption("Diary Entries", entryHandler.getMenu()));
    menu.addOption(new MenuOption("Search Entries", searchHandler.getMenu()));
    menu.addOption(new MenuOption("Authors", authorHandler.getMenu()));
    menu.addOption(new MenuOption("Statistics", statisticsHandler::showStatistics));
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
