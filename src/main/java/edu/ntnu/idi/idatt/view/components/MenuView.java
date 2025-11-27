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
      
      String input;
      try {
        input = scanner.nextLine();
      } catch (Exception e) {
        System.out.println("Input stream closed. Exiting...");
        return;
      }

      handleInput(input);
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

    // Print auto back/exit option
    int backNumber = options.size() + 1;
    String backLabel = isMainMenu ? "Exit" : "Back";
    System.out.printf(
        "[%s%d%s] - %s%n",
        AnsiColors.RED,
        backNumber,
        AnsiColors.RESET,
        backLabel
    );

    System.out.print("\n-> ");
  }

  /**
   * Handles user input and executes the selected option.
   *
   * @param input the user input
   */
  private void handleInput(String input) {
    try {
      int choice = Integer.parseInt(input);
      int backNumber = options.size() + 1;

      if (choice < 1 || choice > backNumber) {
        System.out.println("Invalid input");
      } else if (choice == backNumber) {
        // Back/Exit selected
        if (isMainMenu) {
          System.out.println("Goodbye!");
          System.exit(0);
        } else {
          running = false;  // Exit this menu, return to parent
        }
      } else {
        options.get(choice - 1).execute();
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid input");
    }
  }

  /**
   * Stops the menu loop.
   */
  public void close() {
    running = false;
  }
}
