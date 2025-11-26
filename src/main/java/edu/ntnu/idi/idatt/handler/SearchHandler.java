package edu.ntnu.idi.idatt.handler;

import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import edu.ntnu.idi.idatt.ui.AnsiColors;
import edu.ntnu.idi.idatt.ui.Menu;
import edu.ntnu.idi.idatt.ui.MenuOption;
import java.util.Scanner;

/**
 * Handler for all search-related menu actions.
 */
public class SearchHandler {

  private final Scanner scanner;
  private final AuthorRepository authorRepository;
  private final DiaryEntryRepository diaryEntryRepository;

  /**
   * Creates a new SearchHandler.
   *
   * @param scanner              the scanner for user input
   * @param authorRepository     the author repository
   * @param diaryEntryRepository the diary entry repository
   */
  public SearchHandler(Scanner scanner, AuthorRepository authorRepository,
      DiaryEntryRepository diaryEntryRepository) {
    this.scanner = scanner;
    this.authorRepository = authorRepository;
    this.diaryEntryRepository = diaryEntryRepository;
  }

  /**
   * Builds and returns the search submenu.
   *
   * @return the search menu
   */
  public Menu getMenu() {
    Menu searchMenu = new Menu("== Search Entries ==", scanner);
    searchMenu.addOption(new MenuOption("Search by Date", this::searchByDate));
    searchMenu.addOption(new MenuOption("Search by Date Range", this::searchByDateRange));
    searchMenu.addOption(new MenuOption("Search by Author", this::searchByAuthor));
    searchMenu.addOption(new MenuOption("Search by Keyword", this::searchByKeyword));
    searchMenu.addOption(new MenuOption("Back", AnsiColors.RED, () -> {}));
    searchMenu.setExitOnChoice(true);
    return searchMenu;
  }

  /**
   * Searches entries by specific date.
   */
  private void searchByDate() {
    System.out.println("Search by Date (not implemented)");
  }

  /**
   * Searches entries within a date range.
   */
  private void searchByDateRange() {
    System.out.println("Search by Date Range (not implemented)");
  }

  /**
   * Searches entries by author.
   */
  private void searchByAuthor() {
    System.out.println("Search by Author (not implemented)");
  }

  /**
   * Searches entries by keyword in title or content.
   */
  private void searchByKeyword() {
    System.out.println("Search by Keyword (not implemented)");
  }
}

