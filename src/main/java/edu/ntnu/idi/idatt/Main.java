package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.Author;
import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.util.HibernateUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demo application for sqlite testing
 */
public class Main {

  static {
    // Silence Hibernate java.util.logging output
    Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
  }

  public static void main(String[] args) {
    AuthorRepository authorRepository = new AuthorRepository();

    // Create and save a new author
    Author author = new Author("John", "Doe", "john@example.com");
    try {
      authorRepository.save(author);
      System.out.println("Saved: " + author);
    } catch (Exception e) {
      System.out.println("Author already exists, fetching from DB...");
      author = authorRepository.findByEmail("john@example.com").orElseThrow();
    }

    // Find all authors
    System.out.println("All authors: " + authorRepository.findAll());

    // Find by ID
    authorRepository.findById(author.getId())
        .ifPresent(a -> System.out.println("Found by ID: " + a));

    // Update author
    author.setLastName("Smith");
    authorRepository.update(author);
    System.out.println("Updated: " + author);

    // Cleanup
    HibernateUtil.shutdown();
  }
}