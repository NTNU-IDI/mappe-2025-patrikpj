package edu.ntnu.idi.idatt.handler;

import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import edu.ntnu.idi.idatt.ui.AnsiColors;
import edu.ntnu.idi.idatt.ui.Menu;
import edu.ntnu.idi.idatt.ui.MenuOption;
import java.util.Scanner;

/**
 * Handler for all diary entry-related menu actions.
 */
public class EntryHandler {

  private final Scanner scanner;
  private final AuthorRepository authorRepository;
  private final DiaryEntryRepository diaryEntryRepository;

  /**
   * Creates a new EntryHandler.
   *
   * @param scanner              the scanner for user input
   * @param authorRepository     the author repository
   * @param diaryEntryRepository the diary entry repository
   */
  public EntryHandler(Scanner scanner, AuthorRepository authorRepository,
      DiaryEntryRepository diaryEntryRepository) {
    this.scanner = scanner;
    this.authorRepository = authorRepository;
    this.diaryEntryRepository = diaryEntryRepository;
  }

  /**
   * Builds and returns the diary entries submenu.
   *
   * @return the entries menu
   */
  public Menu getMenu() {
    Menu entriesMenu = new Menu("== Diary Entries ==", scanner);
    entriesMenu.addOption(new MenuOption("Create New Entry", this::createEntry));
    entriesMenu.addOption(new MenuOption("List All Entries", this::listEntries));
    entriesMenu.addOption(new MenuOption("Back", AnsiColors.RED, () -> {}));
    entriesMenu.setExitOnChoice(true);
    return entriesMenu;
  }

  /**
   * Creates a new diary entry.
   */
  private void createEntry() {
    System.out.println("Create Entry (not implemented)");
  }

  /**
   * Lists all diary entries with pagination.
   */
  private void listEntries() {
    System.out.println("List Entries (not implemented)");
  }
}

