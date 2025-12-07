package edu.ntnu.idi.idatt.model.entities;

/**
 * Helper class for setting entity IDs in tests.
 * Located in the same package as entities to access package-private setters.
 */
public class TestEntityHelper {

  /**
   * Sets the ID on an Author for testing purposes.
   *
   * @param author the author
   * @param id the ID to set
   */
  public static void setAuthorId(Author author, Long id) {
    author.setId(id);
  }
}

