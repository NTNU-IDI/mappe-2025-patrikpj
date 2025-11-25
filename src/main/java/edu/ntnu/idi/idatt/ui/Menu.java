package edu.ntnu.idi.idatt.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
  private final String title;
  private final List<MenuOption> options = new ArrayList<>();
  private boolean exitOnChoice = false;
  private final Scanner scanner;

  public Menu(String title, Scanner scanner) {
    this.title = title;
    this.scanner = scanner;
  }

  public void addOption(MenuOption option) {
    options.add(option);
  }

  public void setExitOnChoice(boolean exitOnChoice) {
    this.exitOnChoice = exitOnChoice;
  }

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