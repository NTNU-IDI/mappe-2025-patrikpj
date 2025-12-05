package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.service.AuthorService;
import edu.ntnu.idi.idatt.service.DiaryEntryService;
import edu.ntnu.idi.idatt.view.diary.CreateDiaryEntryView;
import edu.ntnu.idi.idatt.view.diary.DiaryEntriesView;
import edu.ntnu.idi.idatt.view.diary.DiaryEntryView;
import edu.ntnu.idi.idatt.view.diary.ListDiaryEntryView;
import edu.ntnu.idi.idatt.view.diary.SearchEntriesView;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Controller for diary-related actions. Handles viewing and managing diary entries.
 */
public class DiaryController {

  private final DiaryEntryService diaryEntryService;
  private final AuthorService authorService;
  private final DiaryEntriesView entriesView;
  private final ListDiaryEntryView listEntryView;
  private final DiaryEntryView entryView;
  private final CreateDiaryEntryView createEntryView;
  private final SearchEntriesView searchEntriesView;

  // Navigation reference
  private MainMenuController mainMenuController;

  /**
   * Creates a new DiaryController.
   *
   * @param diaryEntryService  the diary entry service
   * @param authorService      the author service for author selection
   * @param entriesView        the diary entries menu view
   * @param listEntryView      the diary entries list view
   * @param entryView          the single diary entry view
   * @param createEntryView    the create entry view
   * @param searchEntriesView  the search entries view
   */
  public DiaryController(DiaryEntryService diaryEntryService,
      AuthorService authorService,
      DiaryEntriesView entriesView,
      ListDiaryEntryView listEntryView,
      DiaryEntryView entryView,
      CreateDiaryEntryView createEntryView,
      SearchEntriesView searchEntriesView) {
    this.diaryEntryService = diaryEntryService;
    this.authorService = authorService;
    this.entriesView = entriesView;
    this.listEntryView = listEntryView;
    this.entryView = entryView;
    this.createEntryView = createEntryView;
    this.searchEntriesView = searchEntriesView;
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
   * Shows the diary entries menu.
   *
   * @param in  Scanner for user input
   * @param out PrintStream for output
   * @return the next action to execute
   */
  public Action showEntriesMenu(Scanner in, PrintStream out) {
    entriesView.renderMenu(out);

    while (true) {
      String choice = in.nextLine().trim().toLowerCase();

      switch (choice) {
        case "1" -> {
          return (in2, out2) -> listAllEntries(in2, out2);
        }
        case "2" -> {
          return (in2, out2) -> createEntry(in2, out2);
        }
        case "3" -> {
          return (in2, out2) -> searchEntries(in2, out2);
        }
        case "b" -> {
          return (in2, out2) -> mainMenuController.showMenu(in2, out2);
        }
        default -> {
          entriesView.showError("Invalid selection. Try again.", out);
          entriesView.prompt(out);
        }
      }
    }
  }

  /**
   * Lists all diary entries.
   *
   * @param in  Scanner for user input
   * @param out PrintStream for output
   * @return the next action to execute
   */
  public Action listAllEntries(Scanner in, PrintStream out) {
    List<DiaryEntry> entries = diaryEntryService.findAll();
    listEntryView.render(entries, out);

    while (true) {
      String choice = in.nextLine().trim().toLowerCase();

      if (choice.equals("b")) {
        return this::showEntriesMenu;
      }

      // Try to parse as number for entry selection
      try {
        int index = Integer.parseInt(choice) - 1;
        if (index >= 0 && index < entries.size()) {
          DiaryEntry selected = entries.get(index);
          // Back from detail should return to list
          return (in2, out2) -> showEntryDetail(selected, this::listAllEntries, in2, out2);
        }
      } catch (NumberFormatException ignored) {
        // Fall through to error
      }

      listEntryView.showError("Invalid selection. Try again.", out);
      listEntryView.prompt(out);
    }
  }

  /**
   * Shows the create entry form.
   *
   * @param in  Scanner for user input
   * @param out PrintStream for output
   * @return the next action to execute
   */
  public Action createEntry(Scanner in, PrintStream out) {
    createEntryView.render(out);

    // Check if any authors exist
    List<Author> authors = authorService.findAll();
    if (authors.isEmpty()) {
      createEntryView.showNoAuthors(out);
      createEntryView.promptContinue(out);
      in.nextLine();
      return this::showEntriesMenu;
    }

    // Get title
    createEntryView.promptTitle(out);
    String title = in.nextLine().trim();
    if (title.isBlank()) {
      return this::showEntriesMenu;
    }

    // Select author
    createEntryView.showAuthorSelection(authors, out);
    Author selectedAuthor = null;
    while (selectedAuthor == null) {
      createEntryView.promptAuthor(out);
      String authorInput = in.nextLine().trim();
      if (authorInput.isBlank()) {
        return this::showEntriesMenu;
      }

      try {
        int index = Integer.parseInt(authorInput) - 1;
        if (index >= 0 && index < authors.size()) {
          selectedAuthor = authors.get(index);
        } else {
          createEntryView.showError("Invalid selection. Try again.", out);
        }
      } catch (NumberFormatException e) {
        createEntryView.showError("Enter a number. Try again.", out);
      }
    }

    // Get content (multi-line, double enter to finish)
    createEntryView.showContentInstructions(out);
    StringBuilder contentBuilder = new StringBuilder();
    String previousLine = null;
    while (true) {
      String line = in.nextLine();
      if (line.isEmpty() && (previousLine != null && previousLine.isEmpty())) {
        break;
      }
      if (contentBuilder.length() > 0) {
        contentBuilder.append("\n");
      }
      contentBuilder.append(line);
      previousLine = line;
    }

    String content = contentBuilder.toString().trim();
    if (content.isBlank()) {
      createEntryView.showCancelled(out);
      createEntryView.promptContinue(out);
      in.nextLine();
      return this::showEntriesMenu;
    }

    // Create the entry
    DiaryEntry entry = diaryEntryService.createEntry(title, selectedAuthor, content);
    createEntryView.showCreated(entry.getTitle(), out);

    createEntryView.promptContinue(out);
    in.nextLine();
    return this::showEntriesMenu;
  }

  /**
   * Shows the search entries form.
   *
   * @param in  Scanner for user input
   * @param out PrintStream for output
   * @return the next action to execute
   */
  public Action searchEntries(Scanner in, PrintStream out) {
    searchEntriesView.renderMenu(out);

    while (true) {
      String choice = in.nextLine().trim().toLowerCase();

      switch (choice) {
        case "1" -> {
          return (in2, out2) -> searchByKeyword(in2, out2);
        }
        case "2" -> {
          return (in2, out2) -> searchByDate(in2, out2);
        }
        case "3" -> {
          return (in2, out2) -> searchByDateRange(in2, out2);
        }
        case "b" -> {
          return this::showEntriesMenu;
        }
        default -> {
          searchEntriesView.showError("Invalid selection. Try again.", out);
          searchEntriesView.prompt(out);
        }
      }
    }
  }

  /**
   * Search entries by keyword.
   */
  private Action searchByKeyword(Scanner in, PrintStream out) {
    searchEntriesView.showNotImplemented("Search by keyword", out);
    searchEntriesView.promptContinue(out);
    in.nextLine();
    return this::searchEntries;
  }

  /**
   * Search entries by date.
   */
  private Action searchByDate(Scanner in, PrintStream out) {
    searchEntriesView.showNotImplemented("Search by date", out);
    searchEntriesView.promptContinue(out);
    in.nextLine();
    return this::searchEntries;
  }

  /**
   * Search entries by date range.
   */
  private Action searchByDateRange(Scanner in, PrintStream out) {
    searchEntriesView.showNotImplemented("Search by date range", out);
    searchEntriesView.promptContinue(out);
    in.nextLine();
    return this::searchEntries;
  }

  /**
   * Shows details for a specific diary entry.
   *
   * @param entry           the diary entry to display
   * @param backDestination the action to return to when pressing back
   * @param in              Scanner for user input
   * @param out             PrintStream for output
   * @return the next action to execute
   */
  public Action showEntryDetail(DiaryEntry entry, Action backDestination, Scanner in,
      PrintStream out) {
    entryView.render(entry, out);

    while (true) {
      String choice = in.nextLine().trim().toLowerCase();

      switch (choice) {
        case "1" -> {
          return (in2, out2) -> editEntry(entry, backDestination, in2, out2);
        }
        case "2" -> {
          return (in2, out2) -> deleteEntry(entry, backDestination, in2, out2);
        }
        case "b" -> {
          return backDestination;
        }
        default -> {
          entryView.showError("Invalid selection. Try again.", out);
          entryView.prompt(out);
        }
      }
    }
  }

  /**
   * Edits a diary entry.
   *
   * @param entry           the entry to edit
   * @param backDestination the action to return to
   * @param in              Scanner for user input
   * @param out             PrintStream for output
   * @return the next action to execute
   */
  private Action editEntry(DiaryEntry entry, Action backDestination, Scanner in, PrintStream out) {
    // TODO: Implement entry editing
    entryView.showWarning("Edit entry - not implemented yet", out);
    entryView.promptContinue(out);
    in.nextLine();
    return (in2, out2) -> showEntryDetail(entry, backDestination, in2, out2);
  }

  /**
   * Deletes a diary entry after confirmation.
   *
   * @param entry           the entry to delete
   * @param backDestination the action to return to after deletion
   * @param in              Scanner for user input
   * @param out             PrintStream for output
   * @return the next action to execute
   */
  private Action deleteEntry(DiaryEntry entry, Action backDestination, Scanner in,
      PrintStream out) {
    entryView.promptDeleteConfirmation(entry.getTitle(), out);
    String confirmation = in.nextLine().trim().toLowerCase();

    if (confirmation.equals("yes")) {
      diaryEntryService.delete(entry);
      entryView.showDeleted(out);
      entryView.promptContinue(out);
      in.nextLine();
      return backDestination;
    }

    entryView.showCancelled(out);
    entryView.promptContinue(out);
    in.nextLine();
    return (in2, out2) -> showEntryDetail(entry, backDestination, in2, out2);
  }
}

