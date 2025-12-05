package edu.ntnu.idi.idatt.repository;

import edu.ntnu.idi.idatt.model.entities.Author;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Repository class for Author entity database operations.
 */
public class AuthorRepository {

  private final SessionFactory sessionFactory;

  /**
   * Creates a new AuthorRepository with the given SessionFactory.
   *
   * @param sessionFactory the Hibernate SessionFactory
   * @throws NullPointerException if sessionFactory is null
   */
  public AuthorRepository(SessionFactory sessionFactory) {
    this.sessionFactory = Objects.requireNonNull(sessionFactory, "SessionFactory cannot be null");
  }

  /**
   * Saves a new author to the database.
   *
   * @param author the author to save
   * @return the saved author with generated ID
   * @throws NullPointerException if author is null
   */
  public Author save(Author author) {
    Objects.requireNonNull(author, "Author cannot be null");
    return executeInTransaction(session -> {
      session.persist(author);
      return author;
    });
  }

  /**
   * Finds an author by their ID.
   *
   * @param id the author ID
   * @return an Optional containing the author, or empty if not found
   * @throws NullPointerException if id is null
   */
  public Optional<Author> findById(Long id) {
    Objects.requireNonNull(id, "ID cannot be null");
    try (Session session = sessionFactory.openSession()) {
      return Optional.ofNullable(session.get(Author.class, id));
    }
  }

  /**
   * Finds an author by their email.
   *
   * @param email the email to search for
   * @return an Optional containing the author, or empty if not found
   * @throws NullPointerException if email is null
   */
  public Optional<Author> findByEmail(String email) {
    Objects.requireNonNull(email, "Email cannot be null");
    try (Session session = sessionFactory.openSession()) {
      return session
          .createQuery("FROM Author WHERE email = :email", Author.class)
          .setParameter("email", email.toLowerCase())
          .uniqueResultOptional();
    }
  }

  /**
   * Retrieves all authors from the database, sorted by creation date (newest first).
   *
   * @return a list of all authors (never null)
   */
  public List<Author> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM Author ORDER BY createdAt DESC", Author.class).list();
    }
  }

  /**
   * Updates an existing author in the database.
   *
   * @param author the author to update
   * @return the updated author
   * @throws NullPointerException if author is null
   */
  public Author update(Author author) {
    Objects.requireNonNull(author, "Author cannot be null");
    return executeInTransaction(session -> session.merge(author));
  }

  /**
   * Deletes an author from the database.
   *
   * @param author the author to delete
   * @throws NullPointerException if author is null
   */
  public void delete(Author author) {
    Objects.requireNonNull(author, "Author cannot be null");
    executeInTransaction(session -> {
      session.remove(session.contains(author) ? author : session.merge(author));
      return null;
    });
  }

  /**
   * Checks if an author with the given email exists.
   *
   * @param email the email to check
   * @return true if an author with this email exists
   * @throws NullPointerException if email is null
   */
  public boolean existsByEmail(String email) {
    Objects.requireNonNull(email, "Email cannot be null");
    try (Session session = sessionFactory.openSession()) {
      Long count = session
          .createQuery("SELECT COUNT(a) FROM Author a WHERE a.email = :email", Long.class)
          .setParameter("email", email.toLowerCase())
          .uniqueResult();
      return count != null && count > 0;
    }
  }

  /**
   * Executes an operation within a transaction, handling commit and rollback.
   *
   * @param operation the operation to execute
   * @param <T>       the return type
   * @return the result of the operation
   */
  private <T> T executeInTransaction(Function<Session, T> operation) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      T result = operation.apply(session);
      transaction.commit();
      return result;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    }
  }
}
