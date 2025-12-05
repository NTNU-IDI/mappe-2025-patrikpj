package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.repository.AuthorRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for Author business logic.
 */
public class AuthorService {

  private final AuthorRepository authorRepository;

  /**
   * Creates a new AuthorService with the given repository.
   *
   * @param authorRepository the author repository
   * @throws NullPointerException if authorRepository is null
   */
  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = Objects.requireNonNull(authorRepository,
        "AuthorRepository cannot be null");
  }

  /**
   * Creates a new author if the email is not already in use.
   *
   * @param firstName the author's first name
   * @param lastName  the author's last name
   * @param email     the author's email
   * @return the created author, or empty if email already exists
   * @throws NullPointerException     if any argument is null
   * @throws IllegalArgumentException if any argument is blank or email is invalid
   */
  public Optional<Author> createAuthor(String firstName, String lastName, String email) {
    if (authorRepository.existsByEmail(email)) {
      return Optional.empty();
    }

    Author author = new Author(firstName, lastName, email);
    return Optional.of(authorRepository.save(author));
  }

  /**
   * Creates a new author, throwing an exception if email is already in use.
   *
   * @param firstName the author's first name
   * @param lastName  the author's last name
   * @param email     the author's email
   * @return the created author
   * @throws NullPointerException     if any argument is null
   * @throws IllegalArgumentException if any argument is blank, email is invalid, or email is
   *                                  already in use
   */
  public Author createAuthorOrThrow(String firstName, String lastName, String email) {
    if (authorRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("An author with email '" + email + "' already exists");
    }

    Author author = new Author(firstName, lastName, email);
    return authorRepository.save(author);
  }

  /**
   * Finds an author by their ID.
   *
   * @param id the author ID
   * @return an Optional containing the author, or empty if not found
   * @throws NullPointerException if id is null
   */
  public Optional<Author> findById(Long id) {
    return authorRepository.findById(id);
  }

  /**
   * Finds an author by their email.
   *
   * @param email the email to search for
   * @return an Optional containing the author, or empty if not found
   * @throws NullPointerException if email is null
   */
  public Optional<Author> findByEmail(String email) {
    return authorRepository.findByEmail(email);
  }

  /**
   * Retrieves all authors.
   *
   * @return a list of all authors
   */
  public List<Author> findAll() {
    return authorRepository.findAll();
  }

  /**
   * Updates an existing author.
   *
   * @param author the author to update
   * @return the updated author
   * @throws NullPointerException if author is null
   */
  public Author update(Author author) {
    return authorRepository.update(author);
  }

  /**
   * Deletes an author.
   *
   * @param author the author to delete
   * @throws NullPointerException if author is null
   */
  public void delete(Author author) {
    authorRepository.delete(author);
  }

  /**
   * Checks if an email is already in use.
   *
   * @param email the email to check
   * @return true if the email is already used by an author
   * @throws NullPointerException if email is null
   */
  public boolean emailExists(String email) {
    return authorRepository.existsByEmail(email);
  }
}
