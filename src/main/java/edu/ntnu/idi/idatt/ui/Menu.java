package edu.ntnu.idi.idatt.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A text-based menu that displays options and handles user input.
 */
public class Menu {
  private final String title;
  private final List<MenuOption> options = new ArrayList<>();
  private boolean exitOnChoice = false;
  private final Scanner scanner;

  /**
   * Creates a new menu with the specified title.
   *
   * @param title   the menu title
   * @param scanner the scanner for reading user input
   */
  public Menu(String title, Scanner scanner) {
    this.title = title;
    this.scanner = scanner;
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
   * Sets whether the menu should exit after a choice is made.
   *
   * @param exitOnChoice true to exit after choice, false to continue
   */
  public void setExitOnChoice(boolean exitOnChoice) {
    this.exitOnChoice = exitOnChoice;
  }

  /**
   * Displays the menu and processes user input in a loop.
   */
  public void show() {
    while (true) {
      if (!title.isEmpty()) {
        System.out.println("\n" + title);
      }
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

      System.out.print("\n-> ");
      String input;
      try {
        input = scanner.nextLine();
      } catch (Exception e) {
        System.out.println("Input stream closed. Exiting...");
        return;
      }

      try {
        int choice = Integer.parseInt(input);
        if (choice < 1 || choice > options.size()) {
          System.out.println("Invalid input");
        } else {
          options.get(choice - 1).execute();
          if (exitOnChoice) {
            break;
          }
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input");
      }
    }
  }
}