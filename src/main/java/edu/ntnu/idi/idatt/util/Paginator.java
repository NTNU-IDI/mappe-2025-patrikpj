package edu.ntnu.idi.idatt.util;

import edu.ntnu.idi.idatt.ui.AnsiColors;
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
   * Returns the selected item or null if user exits without selection.
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
   * Just allows browsing through pages.
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

  private void displayCurrentPage() {
    int totalPages = getTotalPages();
    int start = currentPage * pageSize;
    int end = Math.min(start + pageSize, items.size());

    System.out.println();
    System.out.printf("== %s (Page %d of %d) ==%n", title, currentPage + 1, totalPages);

    for (int i = start; i < end; i++) {
      T item = items.get(i);
      System.out.printf("[%s%d%s] - %s%n",
          AnsiColors.CYAN, i + 1, AnsiColors.RESET, formatter.apply(item));
    }

    System.out.printf("%nTotal: %d item(s)%n", items.size());
  }

  private String promptNavigation() {
    StringBuilder options = new StringBuilder();
    options.append("\n");

    if (hasNextPage()) {
      options.append(String.format("[%sN%s] Next  ", AnsiColors.CYAN, AnsiColors.RESET));
    }
    if (hasPreviousPage()) {
      options.append(String.format("[%sP%s] Previous  ", AnsiColors.CYAN, AnsiColors.RESET));
    }

    int start = currentPage * pageSize + 1;
    int end = Math.min(start + pageSize - 1, items.size());
    options.append(String.format("[%s%d-%d%s] Select  ",
        AnsiColors.CYAN, start, end, AnsiColors.RESET));
    options.append(String.format("[%sB%s] Back", AnsiColors.RED, AnsiColors.RESET));

    System.out.println(options);
    System.out.print("\n-> ");

    String input = scanner.nextLine().trim();
    return input.isEmpty() ? null : input;
  }

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

    String input = scanner.nextLine().trim();
    return input.isEmpty() ? null : input;
  }

  private T trySelect(String input) {
    try {
      int index = Integer.parseInt(input) - 1;
      if (index >= 0 && index < items.size()) {
        return items.get(index);
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

