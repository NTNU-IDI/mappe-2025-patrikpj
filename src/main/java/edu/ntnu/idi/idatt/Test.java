package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.repository.AuthorRepository;
import edu.ntnu.idi.idatt.service.AuthorService;
import edu.ntnu.idi.idatt.util.HibernateUtil;

/**
 * Test script to create sample authors.
 */
public class Test {

  public static void main(String[] args) {
    AuthorRepository repo = new AuthorRepository(HibernateUtil.getSessionFactory());
    AuthorService service = new AuthorService(repo);

    String[][] authors = {
        {"Alice", "Anderson", "alice@example.com"},
        {"Bob", "Brown", "bob@example.com"},
        {"Charlie", "Clark", "charlie@example.com"},
        {"Diana", "Davis", "diana@example.com"},
        {"Edward", "Evans", "edward@example.com"},
        {"Fiona", "Fisher", "fiona@example.com"},
        {"George", "Green", "george@example.com"},
        {"Hannah", "Harris", "hannah@example.com"},
        {"Ivan", "Irwin", "ivan@example.com"},
        {"Julia", "Jones", "julia@example.com"},
        {"Kevin", "King", "kevin@example.com"}
    };

    System.out.println("Creating 11 authors...");
    
    for (String[] data : authors) {
      service.createAuthor(data[0], data[1], data[2])
          .ifPresentOrElse(
              a -> System.out.println("Created: " + a),
              () -> System.out.println("Skipped: " + data[2] + " (already exists)")
          );
    }

    System.out.println("\nTotal authors: " + service.findAll().size());
    HibernateUtil.shutdown();
  }
}

