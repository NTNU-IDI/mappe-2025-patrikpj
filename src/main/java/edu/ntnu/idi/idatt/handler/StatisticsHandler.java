package edu.ntnu.idi.idatt.handler;

import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import java.util.Scanner;

/**
 * Handler for displaying statistics.
 */
public class StatisticsHandler {

  private final Scanner scanner;
  private final AuthorRepository authorRepository;
  private final DiaryEntryRepository diaryEntryRepository;

  /**
   * Creates a new StatisticsHandler.
   *
   * @param scanner              the scanner for user input
   * @param authorRepository     the author repository
   * @param diaryEntryRepository the diary entry repository
   */
  public StatisticsHandler(Scanner scanner, AuthorRepository authorRepository,
      DiaryEntryRepository diaryEntryRepository) {
    this.scanner = scanner;
    this.authorRepository = authorRepository;
    this.diaryEntryRepository = diaryEntryRepository;
  }

  /**
   * Displays statistics about the diary system.
   */
  public void showStatistics() {
    System.out.println("Statistics (not implemented)");
  }
}

