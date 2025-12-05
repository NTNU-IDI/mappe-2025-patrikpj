package edu.ntnu.idi.idatt.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A text-based menu that displays options and handles user input.
 */
public class MenuView {

  private final String title;
  private final List<MenuOption> options = new ArrayList<>();
  private final Scanner scanner;
  private final boolean isMainMenu;
  private final InputHelper input;
  private boolean running = true;

  /**
   * Creates a new menu with the specified title.
   *
   * @param title      the menu title
   * @param scanner    the scanner for reading user input
   * @param isMainMenu true if this is the main menu (shows "Exit" instead of "Back")
   */
  public MenuView(String title, Scanner scanner, boolean isMainMenu) {
    this.title = title;
    this.scanner = scanner;
    this.isMainMenu = isMainMenu;
    this.input = new InputHelper(scanner);
  }

  /**
   * Creates a submenu with the specified title.
   *
   * @param title   the menu title
   * @param scanner the scanner for reading user input
   */
  public MenuView(String title, Scanner scanner) {
    this(title, scanner, false);
  }

  /**
   * Adds an option to the menu.
   *
   * @param option the menu option to add
   */
  public void addOption(MenuOption option) {
    options.add(option);
  }

  /**
   * Displays the menu and processes user input in a loop.
   */
  public void show() {
    running = true;

    while (running) {
      printMenu();

      String userInput;
      try {
        userInput = scanner.nextLine().trim();
      } catch (Exception e) {
        System.out.println("Input stream closed. Exiting...");
        return;
      }

      handleInput(userInput);
    }
  }

  /**
   * Prints the menu title and all options.
   */
  private void printMenu() {
    if (!title.isEmpty()) {
      System.out.println("\n" + title);
    }

    // Print user-defined options
    for (int i = 0; i < options.size(); i++) {
      MenuOption option = options.get(i);
      System.out.printf(
          "[%s%d%s] - %s%n",
          option.getNumberColor(),
          i + 1,
          AnsiColors.RESET,
          option.getLabel()
      );
    }

    // Print back/exit option using 'B' (consistent with Paginator)
    String backLabel = isMainMenu ? "Exit" : "Back";
    System.out.printf(
        "[%sB%s] - %s%n",
        AnsiColors.RED,
        AnsiColors.RESET,
        backLabel
    );

    System.out.print("\n-> ");
  }

  /**
   * Handles user input and executes the selected option.
   *
   * @param userInput the user input
   */
  private void handleInput(String userInput) {
    // Check for back/exit command
    if (userInput.equalsIgnoreCase("b")) {
      handleBackOrExit();
      return;
    }

    // Try to parse as number for option selection
    try {
      int choice = Integer.parseInt(userInput);

      if (choice < 1 || choice > options.size()) {
        input.error("Invalid option. Please enter 1-" + options.size() + " or B.");
      } else {
        options.get(choice - 1).execute();
      }
    } catch (NumberFormatException e) {
      input.error("Invalid input. Please enter a number or B.");
    }
  }

  /**
   * Handles the back or exit action.
   */
  private void handleBackOrExit() {
    if (isMainMenu) {
      if (input.confirm("Are you sure you want to exit?")) {
        System.out.println("Goodbye!");
        System.exit(0);
      }
    } else {
      running = false;
    }
  }

  /**
   * Stops the menu loop. Call this to close the menu from an action.
   */
  public void close() {
    running = false;
  }
}
