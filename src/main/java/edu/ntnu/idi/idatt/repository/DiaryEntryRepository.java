package edu.ntnu.idi.idatt.repository;

import edu.ntnu.idi.idatt.model.Author;
import edu.ntnu.idi.idatt.model.DiaryEntry;
import edu.ntnu.idi.idatt.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Repository class for DiaryEntry entity database operations.
 */
public class DiaryEntryRepository {

  /**
   * Saves a new diary entry to the database.
   *
   * @param entry the diary entry to save
   * @return the saved entry with generated ID
   */
  public DiaryEntry save(DiaryEntry entry) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(entry);
      transaction.commit();
      return entry;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    }
  }

  /**
   * Finds a diary entry by its ID.
   *
   * @param id the entry ID
   * @return an Optional containing the entry, or empty if not found
   */
  public Optional<DiaryEntry> findById(Long id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      DiaryEntry entry = session.get(DiaryEntry.class, id);
      return Optional.ofNullable(entry);
    }
  }

  /**
   * Retrieves all diary entries from the database.
   *
   * @return a list of all diary entries
   */
  public List<DiaryEntry> findAll() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery("FROM DiaryEntry", DiaryEntry.class).list();
    }
  }

  /**
   * Finds all diary entries by a specific author.
   *
   * @param author the author to search for
   * @return a list of diary entries by the author
   */
  public List<DiaryEntry> findByAuthor(Author author) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery("FROM DiaryEntry WHERE author = :author", DiaryEntry.class)
          .setParameter("author", author)
          .list();
    }
  }

  /**
   * Finds all diary entries containing a keyword in the title.
   *
   * @param keyword the keyword to search for
   * @return a list of matching diary entries
   */
  public List<DiaryEntry> findByTitleContaining(String keyword) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery("FROM DiaryEntry WHERE title LIKE :keyword", DiaryEntry.class)
          .setParameter("keyword", "%" + keyword + "%")
          .list();
    }
  }

  /**
   * Updates an existing diary entry in the database.
   *
   * @param entry the entry to update
   * @return the updated entry
   */
  public DiaryEntry update(DiaryEntry entry) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      DiaryEntry merged = session.merge(entry);
      transaction.commit();
      return merged;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    }
  }

  /**
   * Deletes a diary entry from the database.
   *
   * @param entry the entry to delete
   */
  public void delete(DiaryEntry entry) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.remove(entry);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    }
  }
}

