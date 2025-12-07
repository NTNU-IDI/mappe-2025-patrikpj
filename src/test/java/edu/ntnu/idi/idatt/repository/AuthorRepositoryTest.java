package edu.ntnu.idi.idatt.repository;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.util.TestHibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AuthorRepositoryTest {

  private static SessionFactory sessionFactory;
  private AuthorRepository repository;

  @BeforeAll
  static void setUpClass() {
    sessionFactory = TestHibernateUtil.getSessionFactory();
  }

  @AfterAll
  static void tearDownClass() {
    TestHibernateUtil.shutdown();
  }

  @BeforeEach
  void setUp() {
    repository = new AuthorRepository(sessionFactory);
    // Clean database before each test
    clearDatabase();
  }

  private void clearDatabase() {
    try (var session = sessionFactory.openSession()) {
      var tx = session.beginTransaction();
      session.createMutationQuery("DELETE FROM DiaryEntry").executeUpdate();
      session.createMutationQuery("DELETE FROM Author").executeUpdate();
      tx.commit();
    }
  }

  // constructor tests
  @Nested
  @DisplayName("Constructor")
  class ConstructorTests {

    @Test
    @DisplayName("should throw NullPointerException for null sessionFactory")
    void shouldThrowForNullSessionFactory() {
      assertThrows(NullPointerException.class, () -> new AuthorRepository(null));
    }
  }

  // save tests
  @Nested
  @DisplayName("save()")
  class SaveTests {

    @Test
    @DisplayName("should persist author and assign ID")
    void shouldPersistAuthorAndAssignId() {
      Author author = new Author("John", "Doe", "john@example.com");

      Author saved = repository.save(author);

      assertNotNull(saved.getId());
      assertEquals("John", saved.getFirstName());
    }

    @Test
    @DisplayName("should throw NullPointerException for null author")
    void shouldThrowForNullAuthor() {
      assertThrows(NullPointerException.class, () -> repository.save(null));
    }

    @Test
    @DisplayName("should throw exception for duplicate email")
    void shouldThrowForDuplicateEmail() {
      repository.save(new Author("John", "Doe", "john@example.com"));

      assertThrows(Exception.class,
          () -> repository.save(new Author("Jane", "Smith", "john@example.com")));
    }
  }

  // findById tests
  @Nested
  @DisplayName("findById()")
  class FindByIdTests {

    @Test
    @DisplayName("should return author when found")
    void shouldReturnAuthorWhenFound() {
      Author saved = repository.save(new Author("John", "Doe", "john@example.com"));

      Optional<Author> found = repository.findById(saved.getId());

      assertTrue(found.isPresent());
      assertEquals("john@example.com", found.get().getEmail());
    }

    @Test
    @DisplayName("should return empty when not found")
    void shouldReturnEmptyWhenNotFound() {
      Optional<Author> found = repository.findById(999L);

      assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("should throw NullPointerException for null id")
    void shouldThrowForNullId() {
      assertThrows(NullPointerException.class, () -> repository.findById(null));
    }
  }

  // findByEmail tests
  @Nested
  @DisplayName("findByEmail()")
  class FindByEmailTests {

    @Test
    @DisplayName("should return author when email matches")
    void shouldReturnAuthorWhenEmailMatches() {
      repository.save(new Author("John", "Doe", "john@example.com"));

      Optional<Author> found = repository.findByEmail("john@example.com");

      assertTrue(found.isPresent());
      assertEquals("John", found.get().getFirstName());
    }

    @Test
    @DisplayName("should be case-insensitive")
    void shouldBeCaseInsensitive() {
      repository.save(new Author("John", "Doe", "john@example.com"));

      Optional<Author> found = repository.findByEmail("JOHN@EXAMPLE.COM");

      assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("should return empty when not found")
    void shouldReturnEmptyWhenNotFound() {
      Optional<Author> found = repository.findByEmail("nobody@example.com");

      assertTrue(found.isEmpty());
    }
  }

  // findAll tests
  @Nested
  @DisplayName("findAll()")
  class FindAllTests {

    @Test
    @DisplayName("should return empty list when no authors")
    void shouldReturnEmptyListWhenNoAuthors() {
      List<Author> authors = repository.findAll();

      assertTrue(authors.isEmpty());
    }

    @Test
    @DisplayName("should return all authors")
    void shouldReturnAllAuthors() {
      repository.save(new Author("John", "Doe", "john@example.com"));
      repository.save(new Author("Jane", "Smith", "jane@example.com"));

      List<Author> authors = repository.findAll();

      assertEquals(2, authors.size());
    }
  }

  // update tests
  @Nested
  @DisplayName("update()")
  class UpdateTests {

    @Test
    @DisplayName("should update author fields")
    void shouldUpdateAuthorFields() {
      Author author = repository.save(new Author("John", "Doe", "john@example.com"));
      author.setFirstName("Jonathan");

      repository.update(author);

      Optional<Author> found = repository.findById(author.getId());
      assertTrue(found.isPresent());
      assertEquals("Jonathan", found.get().getFirstName());
    }
  }

  // delete tests
  @Nested
  @DisplayName("delete()")
  class DeleteTests {

    @Test
    @DisplayName("should remove author from database")
    void shouldRemoveAuthorFromDatabase() {
      Author author = repository.save(new Author("John", "Doe", "john@example.com"));
      Long id = author.getId();

      repository.delete(author);

      assertTrue(repository.findById(id).isEmpty());
    }
  }

  // existsByEmail tests
  @Nested
  @DisplayName("existsByEmail()")
  class ExistsByEmailTests {

    @Test
    @DisplayName("should return true when email exists")
    void shouldReturnTrueWhenEmailExists() {
      repository.save(new Author("John", "Doe", "john@example.com"));

      assertTrue(repository.existsByEmail("john@example.com"));
    }

    @Test
    @DisplayName("should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {
      assertFalse(repository.existsByEmail("nobody@example.com"));
    }

    @Test
    @DisplayName("should be case-insensitive")
    void shouldBeCaseInsensitive() {
      repository.save(new Author("John", "Doe", "john@example.com"));

      assertTrue(repository.existsByEmail("JOHN@EXAMPLE.COM"));
    }
  }
}