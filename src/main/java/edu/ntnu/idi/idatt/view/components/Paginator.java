package edu.ntnu.idi.idatt.view.components;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Generic paginator for displaying lists with navigation and selection.
 *
 * @param <T> the type of items to paginate
 */
public class Paginator<T> {

  private static final int DEFAULT_PAGE_SIZE = 9;

  private final List<T> items;
  private final Scanner scanner;
  private final String title;
  private final Function<T, String> formatter;
  private final int pageSize;
  private int currentPage = 0;

  /**
   * Creates a new paginator with default page size.
   *
   * @param items     the list of items to paginate
   * @param scanner   the scanner for user input
   * @param title     the title to display
   * @param formatter function to format each item for display
   */
  public Paginator(List<T> items, Scanner scanner, String title, Function<T, String> formatter) {
    this(items, scanner, title, formatter, DEFAULT_PAGE_SIZE);
  }

  /**
   * Creates a new paginator with custom page size.
   *
   * @param items     the list of items to paginate
   * @param scanner   the scanner for user input
   * @param title     the title to display
   * @param formatter function to format each item for display
   * @param pageSize  number of items per page
   */
  public Paginator(List<T> items, Scanner scanner, String title,
                   Function<T, String> formatter, int pageSize) {
    this.items = items;
    this.scanner = scanner;
    this.title = title;
    this.formatter = formatter;
    this.pageSize = pageSize;
  }

  /**
   * Displays the paginated list and handles navigation.
   *
   * @return the selected item, or null if cancelled
   */
  public T show() {
    if (items.isEmpty()) {
      System.out.println("\nNo items found.");
      return null;
    }

    while (true) {
      displayCurrentPage();
      String input = promptNavigation();

      if (input == null || input.equalsIgnoreCase("b")) {
        return null;
      }

      if (input.equalsIgnoreCase("n")) {
        nextPage();
      } else if (input.equalsIgnoreCase("p")) {
        previousPage();
      } else {
        T selected = trySelect(input);
        if (selected != null) {
          return selected;
        }
      }
    }
  }

  /**
   * Displays the paginated list without selection capability.
   */
  public void showReadOnly() {
    if (items.isEmpty()) {
      System.out.println("\nNo items found.");
      return;
    }

    while (true) {
      displayCurrentPage();
      String input = promptNavigationReadOnly();

      if (input == null || input.equalsIgnoreCase("b")) {
        return;
      }

      if (input.equalsIgnoreCase("n")) {
        nextPage();
      } else if (input.equalsIgnoreCase("p")) {
        previousPage();
      }
    }
  }

  /**
   * Displays the current page of items.
   */
  private void displayCurrentPage() {
    int totalPages = getTotalPages();
    int start = currentPage * pageSize;
    int end = Math.min(start + pageSize, items.size());

    System.out.println();
    System.out.printf("== %s (Page %d of %d) ==%n", title, currentPage + 1, totalPages);

    int displayIndex = 1;
    for (int i = start; i < end; i++) {
      T item = items.get(i);
      System.out.printf("[%s%d%s] - %s%n",
          AnsiColors.CYAN, displayIndex++, AnsiColors.RESET, formatter.apply(item));
    }

    System.out.printf("%nTotal: %d item(s)%n", items.size());
    System.out.println("Choose an option:");
  }

  /**
   * Prompts for navigation input with selection option.
   *
   * @return the user input, or null if input stream closed
   */
  private String promptNavigation() {
    StringBuilder options = new StringBuilder();
    options.append("\n");

    if (hasNextPage()) {
      options.append(String.format("[%sN%s] Next  ", AnsiColors.CYAN, AnsiColors.RESET));
    }
    if (hasPreviousPage()) {
      options.append(String.format("[%sP%s] Previous  ", AnsiColors.CYAN, AnsiColors.RESET));
    }

    int itemsOnPage = Math.min(pageSize, items.size() - currentPage * pageSize);
    options.append(String.format("[%s1-%d%s] Select  ",
        AnsiColors.CYAN, itemsOnPage, AnsiColors.RESET));
    options.append(String.format("[%sB%s] Back", AnsiColors.RED, AnsiColors.RESET));

    System.out.println(options);
    System.out.print("\n-> ");

    try {
      String input = scanner.nextLine().trim();
      return input.isEmpty() ? null : input;
    } catch (Exception e) {
      return "b";  // Treat as back on Ctrl+C or closed stream
    }
  }

  /**
   * Prompts for navigation input without selection option.
   *
   * @return the user input, or null if input stream closed
   */
  private String promptNavigationReadOnly() {
    StringBuilder options = new StringBuilder();
    options.append("\n");

    if (hasNextPage()) {
      options.append(String.format("[%sN%s] Next  ", AnsiColors.CYAN, AnsiColors.RESET));
    }
    if (hasPreviousPage()) {
      options.append(String.format("[%sP%s] Previous  ", AnsiColors.CYAN, AnsiColors.RESET));
    }
    options.append(String.format("[%sB%s] Back", AnsiColors.RED, AnsiColors.RESET));

    System.out.println(options);
    System.out.print("\n-> ");

    try {
      String input = scanner.nextLine().trim();
      return input.isEmpty() ? null : input;
    } catch (Exception e) {
      return "b";  // Treat as back on Ctrl+C or closed stream
    }
  }

  /**
   * Attempts to select an item by its page-local number.
   *
   * @param input the user input
   * @return the selected item, or null if invalid
   */
  private T trySelect(String input) {
    try {
      int pageIndex = Integer.parseInt(input) - 1;  // 0-based index on current page
      int itemsOnPage = Math.min(pageSize, items.size() - currentPage * pageSize);
      
      if (pageIndex >= 0 && pageIndex < itemsOnPage) {
        int globalIndex = currentPage * pageSize + pageIndex;
        return items.get(globalIndex);
      } else {
        System.out.println(AnsiColors.RED + "Invalid selection" + AnsiColors.RESET);
      }
    } catch (NumberFormatException e) {
      System.out.println(AnsiColors.RED + "Invalid input" + AnsiColors.RESET);
    }
    return null;
  }

  private void nextPage() {
    if (hasNextPage()) {
      currentPage++;
    }
  }

  private void previousPage() {
    if (hasPreviousPage()) {
      currentPage--;
    }
  }

  private boolean hasNextPage() {
    return (currentPage + 1) * pageSize < items.size();
  }

  private boolean hasPreviousPage() {
    return currentPage > 0;
  }

  private int getTotalPages() {
    return (int) Math.ceil((double) items.size() / pageSize);
  }
}

