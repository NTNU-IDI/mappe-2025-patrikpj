package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.Author;
import edu.ntnu.idi.idatt.model.DiaryEntry;
import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import edu.ntnu.idi.idatt.util.HibernateUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demo application for sqlite testing.
 */
public class Main {

  static {
    // Silence Hibernate java.util.logging output
    Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
  }

  public static void main(String[] args) {
    AuthorRepository authorRepository = new AuthorRepository();
    DiaryEntryRepository diaryEntryRepository = new DiaryEntryRepository();

    // Create and save a new author
    Author author = new Author("John", "Doe", "john@example.com");
    try {
      authorRepository.save(author);
      System.out.println("Saved author: " + author);
    } catch (Exception e) {
      System.out.println("Author already exists, fetching from DB...");
      author = authorRepository.findByEmail("john@example.com").orElseThrow();
    }

    // Create and save a diary entry
    DiaryEntry entry = new DiaryEntry("My First Entry", author, "Today was a great day!");
    try {
      diaryEntryRepository.save(entry);
      System.out.println("Saved entry: " + entry);
    } catch (Exception e) {
      System.out.println("Entry could not be saved: " + e.getMessage());
    }

    // Find all diary entries
    System.out.println("All entries: " + diaryEntryRepository.findAll());

    // Find entries by author
    System.out.println("Entries by " + author.getFullName() + ": "
        + diaryEntryRepository.findByAuthor(author));

    // Cleanup
    HibernateUtil.shutdown();
  }
}