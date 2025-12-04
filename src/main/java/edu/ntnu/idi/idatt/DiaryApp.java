package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.controller.AuthorController;
import edu.ntnu.idi.idatt.controller.DiaryEntryController;
import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import edu.ntnu.idi.idatt.service.AuthorService;
import edu.ntnu.idi.idatt.service.DiaryEntryService;
import edu.ntnu.idi.idatt.service.StatisticsService;
import edu.ntnu.idi.idatt.util.HibernateUtil;
import edu.ntnu.idi.idatt.view.components.AnsiColors;
import edu.ntnu.idi.idatt.view.components.DiarySystemBanner;
import edu.ntnu.idi.idatt.view.components.MenuOption;
import edu.ntnu.idi.idatt.view.components.MenuView;
import java.util.Scanner;
import org.hibernate.SessionFactory;

/**
 * Main application class for the Diary System.
 */
public class DiaryApp {

  private Scanner scanner;

  // Repositories
  private AuthorRepository authorRepository;
  private DiaryEntryRepository diaryEntryRepository;

  // Services
  private AuthorService authorService;
  private DiaryEntryService diaryEntryService;
  private StatisticsService statisticsService;

  // Controllers
  private AuthorController authorController;
  private DiaryEntryController entryController;

  // Main menu
  private MenuView mainMenu;

  /**
   * Initializes all application components.
   */
  public void init() {
    // Create scanner
    this.scanner = new Scanner(System.in);

    // Initialize Hibernate (catches config errors early)
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Create repositories
    this.authorRepository = new AuthorRepository(sessionFactory);
    this.diaryEntryRepository = new DiaryEntryRepository(sessionFactory);

    // Create services
    this.authorService = new AuthorService(authorRepository);
    this.diaryEntryService = new DiaryEntryService(diaryEntryRepository);
    this.statisticsService = new StatisticsService(authorService, diaryEntryService);
    
    // Create controllers
    this.authorController = new AuthorController(authorService, diaryEntryService, scanner);
    this.entryController = new DiaryEntryController(diaryEntryService, authorService, scanner);

    // Build menus
    buildMenu();

    // Register shutdown hook for cleanup on Ctrl+C or exit
    Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
  }

  /**
   * Builds the main menu.
   */
  private void buildMenu() {
    this.mainMenu = new MenuView("== Diary System ==", scanner, true);

    mainMenu.addOption(new MenuOption("Diary Entries", entryController.getMenu()));
    mainMenu.addOption(new MenuOption("Authors", authorController.getMenu()));
    mainMenu.addOption(new MenuOption("Statistics", AnsiColors.GREEN, this::showStatistics));
  }

  /**
   * Starts the application.
   */
  public void start() {
    System.out.println(DiarySystemBanner.getColoredBanner());
    mainMenu.show();
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

  private void showStatistics() {
    System.out.println("\n== Statistics ==");
    System.out.println("Total authors: " + statisticsService.getTotalAuthors());
    System.out.println("Total entries: " + statisticsService.getTotalEntries());
    
    System.out.println("\nEntries per author:");
    statisticsService.getEntriesPerAuthor().forEach((author, count) ->
        System.out.println("  " + author.getFullName() + " - " + count));
  }
}
