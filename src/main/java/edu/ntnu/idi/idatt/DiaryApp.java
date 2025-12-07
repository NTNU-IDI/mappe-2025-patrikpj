package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.controller.Action;
import edu.ntnu.idi.idatt.controller.Router;
import edu.ntnu.idi.idatt.controller.AuthorController;
import edu.ntnu.idi.idatt.controller.DiaryController;
import edu.ntnu.idi.idatt.controller.MainMenuController;
import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import edu.ntnu.idi.idatt.service.AuthorService;
import edu.ntnu.idi.idatt.service.DiaryEntryService;
import edu.ntnu.idi.idatt.service.StatisticsService;
import edu.ntnu.idi.idatt.util.HibernateUtil;
import edu.ntnu.idi.idatt.view.author.AuthorMenuView;
import edu.ntnu.idi.idatt.view.author.AuthorView;
import edu.ntnu.idi.idatt.view.author.CreateAuthorView;
import edu.ntnu.idi.idatt.view.author.EditAuthorView;
import edu.ntnu.idi.idatt.view.author.FindAuthorView;
import edu.ntnu.idi.idatt.view.author.ListAuthorView;
import edu.ntnu.idi.idatt.view.diary.CreateDiaryEntryView;
import edu.ntnu.idi.idatt.view.diary.DiaryEntriesView;
import edu.ntnu.idi.idatt.view.diary.DiaryEntryView;
import edu.ntnu.idi.idatt.view.diary.EditDiaryEntryView;
import edu.ntnu.idi.idatt.view.diary.ListDiaryEntryView;
import edu.ntnu.idi.idatt.view.diary.SearchEntriesView;
import edu.ntnu.idi.idatt.view.mainmenu.MainMenuView;
import edu.ntnu.idi.idatt.view.statistics.StatisticsView;
import java.io.PrintStream;
import java.util.Scanner;
import org.hibernate.SessionFactory;

/**
 * Composition root for the Diary application.
 * <p>
 * Responsibilities: - Initialize infrastructure (Hibernate, Scanner) - Create repositories,
 * services, views, and controllers - Wire all dependencies together - Build the initial Action and
 * start the Router loop - Register shutdown hooks for cleanup
 */
public class DiaryApp {

  // I/O
  private Scanner scanner;
  private PrintStream out;

  // Repositories
  private AuthorRepository authorRepository;
  private DiaryEntryRepository diaryEntryRepository;

  // Services
  private AuthorService authorService;
  private DiaryEntryService diaryEntryService;
  private StatisticsService statisticsService;

  // Views
  private MainMenuView mainMenuView;
  private AuthorMenuView authorMenuView;
  private ListAuthorView listAuthorView;
  private AuthorView authorView;
  private CreateAuthorView createAuthorView;
  private FindAuthorView findAuthorView;
  private EditAuthorView editAuthorView;
  private DiaryEntriesView diaryEntriesView;
  private ListDiaryEntryView listDiaryEntryView;
  private DiaryEntryView diaryEntryView;
  private CreateDiaryEntryView createDiaryEntryView;
  private SearchEntriesView searchEntriesView;
  private EditDiaryEntryView editDiaryEntryView;
  private StatisticsView statisticsView;

  // Controllers
  private MainMenuController mainMenuController;
  private AuthorController authorController;
  private DiaryController diaryController;

  /**
   * Initializes all application components. Call this before {@link #start()}.
   */
  public void init() {
    // I/O
    this.scanner = new Scanner(System.in);
    this.out = System.out;

    // Initialize Hibernate (fail-fast if config is bad)
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Repositories
    this.authorRepository = new AuthorRepository(sessionFactory);
    this.diaryEntryRepository = new DiaryEntryRepository(sessionFactory);

    // Services
    this.authorService = new AuthorService(authorRepository);
    this.diaryEntryService = new DiaryEntryService(diaryEntryRepository);
    this.statisticsService = new StatisticsService(authorService, diaryEntryService);

    // Views
    this.mainMenuView = new MainMenuView();
    this.authorMenuView = new AuthorMenuView();
    this.listAuthorView = new ListAuthorView();
    this.authorView = new AuthorView();
    this.createAuthorView = new CreateAuthorView();
    this.findAuthorView = new FindAuthorView();
    this.editAuthorView = new EditAuthorView();
    this.diaryEntriesView = new DiaryEntriesView();
    this.listDiaryEntryView = new ListDiaryEntryView();
    this.diaryEntryView = new DiaryEntryView();
    this.createDiaryEntryView = new CreateDiaryEntryView();
    this.searchEntriesView = new SearchEntriesView();
    this.editDiaryEntryView = new EditDiaryEntryView();
    this.statisticsView = new StatisticsView();

    // Controllers
    this.mainMenuController = new MainMenuController(mainMenuView, statisticsService,
        statisticsView);
    this.authorController = new AuthorController(authorService, authorMenuView,
        listAuthorView, authorView, createAuthorView, findAuthorView, editAuthorView);
    this.diaryController = new DiaryController(diaryEntryService, authorService,
        diaryEntriesView, listDiaryEntryView, diaryEntryView, createDiaryEntryView,
        searchEntriesView, editDiaryEntryView);

    // Wire navigation references (setter injection to break circular dependencies)
    mainMenuController.setAuthorController(authorController);
    mainMenuController.setDiaryController(diaryController);
    authorController.setMainMenuController(mainMenuController);
    diaryController.setMainMenuController(mainMenuController);

    // Register shutdown hook for cleanup on Ctrl+C or normal exit
    Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
  }

  /**
   * Starts the application: - Creates the initial action (main menu) - Hands control to the Router
   * (action loop)
   */
  public void start() {
    // Initial action: show main menu
    Action initialAction = (in, out) -> mainMenuController.showMenu(in, out);

    // Router runs the Action-based TUI loop
    Router router = new Router(initialAction, scanner, out);
    router.run();
  }

  /**
   * Cleans up resources before exit. This is called from the shutdown hook and can also be called
   * manually.
   */
  private void cleanup() {
    if (scanner != null) {
      scanner.close();
    }
    HibernateUtil.shutdown();
  }
}
