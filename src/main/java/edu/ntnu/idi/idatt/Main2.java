package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.view.components.AnsiColors;
import edu.ntnu.idi.idatt.view.components.MenuOption;
import edu.ntnu.idi.idatt.view.components.MenuView;
import java.util.Scanner;

/**
 * Test application for the menu system.
 */
public class Main2 {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    System.out.println("=== Menu System Test ===");

    // Create submenus first
    MenuView authorMenu = createAuthorMenu();
    MenuView entryMenu = createEntryMenu();

    // Create main menu
    MenuView mainMenu = new MenuView("== Main Menu ==", scanner, true);
    mainMenu.addOption(new MenuOption("Manage Authors", authorMenu));
    mainMenu.addOption(new MenuOption("Manage Entries", entryMenu));
    mainMenu.addOption(new MenuOption("Show Statistics", AnsiColors.GREEN, Main2::showStats));

    // Start the application
    mainMenu.show();
  }

  private static MenuView createAuthorMenu() {
    MenuView menu = new MenuView("== Author Menu ==", scanner);
    menu.addOption(new MenuOption("Create Author", Main2::createAuthor));
    menu.addOption(new MenuOption("List Authors", Main2::listAuthors));
    menu.addOption(new MenuOption("Find Author", Main2::findAuthor));
    return menu;
  }

  private static MenuView createEntryMenu() {
    MenuView menu = new MenuView("== Diary Entry Menu ==", scanner);
    menu.addOption(new MenuOption("Create Entry", Main2::createEntry));
    menu.addOption(new MenuOption("List Entries", Main2::listEntries));
    menu.addOption(new MenuOption("Search Entries", Main2::searchEntries));
    return menu;
  }

  // Dummy actions for testing
  private static void createAuthor() {
    System.out.println("\n[Action] Creating author...");
    System.out.print("Enter name: ");
    String name = scanner.nextLine();
    System.out.println("Created author: " + name);
  }

  private static void listAuthors() {
    System.out.println("\n[Action] Listing authors...");
    System.out.println("  - John Doe");
    System.out.println("  - Jane Smith");
    System.out.println("  - Bob Johnson");
  }

  private static void findAuthor() {
    System.out.println("\n[Action] Finding author...");
    System.out.print("Enter email: ");
    String email = scanner.nextLine();
    System.out.println("Found: Author with email " + email);
  }

  private static void createEntry() {
    System.out.println("\n[Action] Creating entry...");
    System.out.print("Enter title: ");
    String title = scanner.nextLine();
    System.out.println("Created entry: " + title);
  }

  private static void listEntries() {
    System.out.println("\n[Action] Listing entries...");
    System.out.println("  - My First Day");
    System.out.println("  - Learning Java");
    System.out.println("  - Coffee Thoughts");
  }

  private static void searchEntries() {
    System.out.println("\n[Action] Searching entries...");
    System.out.print("Enter search term: ");
    String term = scanner.nextLine();
    System.out.println("Found 2 entries matching: " + term);
  }

  private static void showStats() {
    System.out.println("\n[Action] Statistics:");
    System.out.println("  Authors: 3");
    System.out.println("  Entries: 5");
    System.out.println("  Most active: John Doe (3 entries)");
  }
}

