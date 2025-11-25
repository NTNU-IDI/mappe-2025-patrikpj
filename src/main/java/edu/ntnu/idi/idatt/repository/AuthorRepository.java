package edu.ntnu.idi.idatt.repository;

import edu.ntnu.idi.idatt.model.Author;
import edu.ntnu.idi.idatt.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Repository class for Author entity database operations.
 */
public class AuthorRepository {

  /**
   * Saves a new author to the database.
   *
   * @param author the author to save
   * @return the saved author with generated ID
   */
  public Author save(Author author) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(author);
      transaction.commit();
      return author;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    }
  }

  /**
   * Finds an author by their ID.
   *
   * @param id the author ID
   * @return an Optional containing the author, or empty if not found
   */
  public Optional<Author> findById(Long id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Author author = session.get(Author.class, id);
      return Optional.ofNullable(author);
    }
  }

  /**
   * Retrieves all authors from the database.
   *
   * @return a list of all authors
   */
  public List<Author> findAll() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery("FROM Author", Author.class).list();
    }
  }

  /**
   * Updates an existing author in the database.
   *
   * @param author the author to update
   * @return the updated author
   */
  public Author update(Author author) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      Author merged = session.merge(author);
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
   * Deletes an author from the database.
   *
   * @param author the author to delete
   */
  public void delete(Author author) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.remove(author);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    }
  }

  /**
   * Finds an author by their email.
   *
   * @param email the email to search for
   * @return an Optional containing the author, or empty if not found
   */
  public Optional<Author> findByEmail(String email) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery("FROM Author WHERE email = :email", Author.class)
          .setParameter("email", email.toLowerCase())
          .uniqueResultOptional();
    }
  }
}

