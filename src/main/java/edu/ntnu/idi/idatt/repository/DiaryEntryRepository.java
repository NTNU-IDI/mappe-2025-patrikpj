package edu.ntnu.idi.idatt.repository;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Repository class for DiaryEntry entity database operations.
 */
public class DiaryEntryRepository {

  private final SessionFactory sessionFactory;

  /**
   * Creates a new DiaryEntryRepository with the given SessionFactory.
   *
   * @param sessionFactory the Hibernate SessionFactory
   * @throws NullPointerException if sessionFactory is null
   */
  public DiaryEntryRepository(SessionFactory sessionFactory) {
    this.sessionFactory = Objects.requireNonNull(sessionFactory, "SessionFactory cannot be null");
  }

  /**
   * Saves a new diary entry to the database.
   *
   * @param entry the diary entry to save
   * @return the saved entry with generated ID
   * @throws NullPointerException if entry is null
   */
  public DiaryEntry save(DiaryEntry entry) {
    Objects.requireNonNull(entry, "DiaryEntry cannot be null");
    return executeInTransaction(session -> {
      session.persist(entry);
      return entry;
    });
  }

  /**
   * Finds a diary entry by its ID.
   *
   * @param id the entry ID
   * @return an Optional containing the entry, or empty if not found
   * @throws NullPointerException if id is null
   */
  public Optional<DiaryEntry> findById(Long id) {
    Objects.requireNonNull(id, "ID cannot be null");
    try (Session session = sessionFactory.openSession()) {
      return Optional.ofNullable(session.get(DiaryEntry.class, id));
    }
  }

  /**
   * Retrieves all diary entries from the database, sorted by creation date (newest first).
   *
   * @return a list of all diary entries (never null)
   */
  public List<DiaryEntry> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM DiaryEntry ORDER BY createdAt DESC", DiaryEntry.class)
          .list();
    }
  }

  /**
   * Finds all diary entries by a specific author, sorted by creation date (newest first).
   *
   * @param author the author to search for
   * @return a list of entries by the author (never null)
   * @throws NullPointerException if author is null
   */
  public List<DiaryEntry> findByAuthor(Author author) {
    Objects.requireNonNull(author, "Author cannot be null");
    try (Session session = sessionFactory.openSession()) {
      return session
          .createQuery("FROM DiaryEntry WHERE author = :author ORDER BY createdAt DESC",
              DiaryEntry.class)
          .setParameter("author", author)
          .list();
    }
  }

  /**
   * Finds all diary entries by author ID, sorted by creation date (newest first).
   *
   * @param authorId the author ID to search for
   * @return a list of entries by the author (never null)
   * @throws NullPointerException if authorId is null
   */
  public List<DiaryEntry> findByAuthorId(Long authorId) {
    Objects.requireNonNull(authorId, "Author ID cannot be null");
    try (Session session = sessionFactory.openSession()) {
      return session
          .createQuery("FROM DiaryEntry WHERE author.id = :authorId ORDER BY createdAt DESC",
              DiaryEntry.class)
          .setParameter("authorId", authorId)
          .list();
    }
  }

  /**
   * Searches for diary entries containing the given text in title or content. Results are sorted by
   * creation date (newest first).
   *
   * @param searchText the text to search for (case-insensitive)
   * @return a list of matching entries (never null)
   * @throws NullPointerException if searchText is null
   */
  public List<DiaryEntry> searchByTitleOrContent(String searchText) {
    Objects.requireNonNull(searchText, "Search text cannot be null");
    try (Session session = sessionFactory.openSession()) {
      String pattern = "%" + searchText.toLowerCase() + "%";
      return session
          .createQuery(
              "FROM DiaryEntry WHERE LOWER(title) LIKE :pattern OR LOWER(content) LIKE :pattern "
                  + "ORDER BY createdAt DESC",
              DiaryEntry.class)
          .setParameter("pattern", pattern)
          .list();
    }
  }

  /**
   * Finds all diary entries created on a specific date.
   *
   * @param date the date to search for
   * @return a list of entries created on that date (never null)
   * @throws NullPointerException if date is null
   */
  public List<DiaryEntry> findByDate(java.time.LocalDate date) {
    Objects.requireNonNull(date, "Date cannot be null");
    try (Session session = sessionFactory.openSession()) {
      java.time.LocalDateTime startOfDay = date.atStartOfDay();
      java.time.LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
      return session
          .createQuery(
              "FROM DiaryEntry WHERE createdAt >= :start AND createdAt < :end ORDER BY createdAt DESC",
              DiaryEntry.class)
          .setParameter("start", startOfDay)
          .setParameter("end", endOfDay)
          .list();
    }
  }

  /**
   * Finds all diary entries created within a date range (inclusive).
   *
   * @param startDate the start date (inclusive)
   * @param endDate   the end date (inclusive)
   * @return a list of entries within the date range (never null)
   * @throws NullPointerException if either date is null
   */
  public List<DiaryEntry> findByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
    Objects.requireNonNull(startDate, "Start date cannot be null");
    Objects.requireNonNull(endDate, "End date cannot be null");
    try (Session session = sessionFactory.openSession()) {
      java.time.LocalDateTime start = startDate.atStartOfDay();
      java.time.LocalDateTime end = endDate.plusDays(1).atStartOfDay();
      return session
          .createQuery(
              "FROM DiaryEntry WHERE createdAt >= :start AND createdAt < :end ORDER BY createdAt DESC",
              DiaryEntry.class)
          .setParameter("start", start)
          .setParameter("end", end)
          .list();
    }
  }

  /**
   * Updates an existing diary entry in the database.
   *
   * @param entry the diary entry to update
   * @return the updated entry
   * @throws NullPointerException if entry is null
   */
  public DiaryEntry update(DiaryEntry entry) {
    Objects.requireNonNull(entry, "DiaryEntry cannot be null");
    return executeInTransaction(session -> session.merge(entry));
  }

  /**
   * Deletes a diary entry from the database.
   *
   * @param entry the diary entry to delete
   * @throws NullPointerException if entry is null
   */
  public void delete(DiaryEntry entry) {
    Objects.requireNonNull(entry, "DiaryEntry cannot be null");
    executeInTransaction(session -> {
      session.remove(session.contains(entry) ? entry : session.merge(entry));
      return null;
    });
  }

  /**
   * Counts the total number of diary entries.
   *
   * @return the total count
   */
  public long count() {
    try (Session session = sessionFactory.openSession()) {
      Long count = session
          .createQuery("SELECT COUNT(e) FROM DiaryEntry e", Long.class)
          .uniqueResult();
      return count != null ? count : 0;
    }
  }

  /**
   * Counts the number of diary entries by a specific author.
   *
   * @param authorId the author ID
   * @return the count of entries by the author
   * @throws NullPointerException if authorId is null
   */
  public long countByAuthorId(Long authorId) {
    Objects.requireNonNull(authorId, "Author ID cannot be null");
    try (Session session = sessionFactory.openSession()) {
      Long count = session
          .createQuery("SELECT COUNT(e) FROM DiaryEntry e WHERE e.author.id = :authorId",
              Long.class)
          .setParameter("authorId", authorId)
          .uniqueResult();
      return count != null ? count : 0;
    }
  }

  /**
   * Counts diary entries grouped by author in a single query.
   *
   * @return a map of author ID to entry count
   */
  public java.util.Map<Long, Long> countEntriesGroupedByAuthor() {
    try (Session session = sessionFactory.openSession()) {
      List<Object[]> results = session
          .createQuery("SELECT e.author.id, COUNT(e) FROM DiaryEntry e GROUP BY e.author.id",
              Object[].class)
          .list();
      java.util.Map<Long, Long> countsByAuthorId = new java.util.HashMap<>();
      for (Object[] row : results) {
        Long authorId = (Long) row[0];
        Long count = (Long) row[1];
        countsByAuthorId.put(authorId, count);
      }
      return countsByAuthorId;
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

