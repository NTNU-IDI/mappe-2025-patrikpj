package edu.ntnu.idi.idatt.repository;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.util.TestHibernateUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DiaryEntryRepositoryTest {

  private static SessionFactory sessionFactory;
  private DiaryEntryRepository repository;
  private AuthorRepository authorRepository;
  private Author testAuthor;

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
    repository = new DiaryEntryRepository(sessionFactory);
    authorRepository = new AuthorRepository(sessionFactory);
    clearDatabase();
    // Create a test author for diary entries
    testAuthor = authorRepository.save(new Author("John", "Doe", "john@example.com"));
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
      assertThrows(NullPointerException.class, () -> new DiaryEntryRepository(null));
    }
  }

  // save tests
  @Nested
  @DisplayName("save()")
  class SaveTests {

    @Test
    @DisplayName("should persist entry and assign ID")
    void shouldPersistEntryAndAssignId() {
      DiaryEntry entry = new DiaryEntry("My Day", testAuthor, "Today was great!");

      DiaryEntry saved = repository.save(entry);

      assertNotNull(saved.getId());
      assertEquals("My Day", saved.getTitle());
    }

    @Test
    @DisplayName("should throw NullPointerException for null entry")
    void shouldThrowForNullEntry() {
      assertThrows(NullPointerException.class, () -> repository.save(null));
    }
  }

  // findById tests
  @Nested
  @DisplayName("findById()")
  class FindByIdTests {

    @Test
    @DisplayName("should return entry when found")
    void shouldReturnEntryWhenFound() {
      DiaryEntry saved = repository.save(new DiaryEntry("Title", testAuthor, "Content"));

      Optional<DiaryEntry> found = repository.findById(saved.getId());

      assertTrue(found.isPresent());
      assertEquals("Title", found.get().getTitle());
    }

    @Test
    @DisplayName("should return empty when not found")
    void shouldReturnEmptyWhenNotFound() {
      Optional<DiaryEntry> found = repository.findById(999L);

      assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("should throw NullPointerException for null id")
    void shouldThrowForNullId() {
      assertThrows(NullPointerException.class, () -> repository.findById(null));
    }
  }

  // findAll tests
  @Nested
  @DisplayName("findAll()")
  class FindAllTests {

    @Test
    @DisplayName("should return empty list when no entries")
    void shouldReturnEmptyListWhenNoEntries() {
      List<DiaryEntry> entries = repository.findAll();

      assertTrue(entries.isEmpty());
    }

    @Test
    @DisplayName("should return all entries")
    void shouldReturnAllEntries() {
      repository.save(new DiaryEntry("Title 1", testAuthor, "Content 1"));
      repository.save(new DiaryEntry("Title 2", testAuthor, "Content 2"));

      List<DiaryEntry> entries = repository.findAll();

      assertEquals(2, entries.size());
    }
  }

  // findByAuthor tests
  @Nested
  @DisplayName("findByAuthor()")
  class FindByAuthorTests {

    @Test
    @DisplayName("should return entries by author")
    void shouldReturnEntriesByAuthor() {
      repository.save(new DiaryEntry("Title 1", testAuthor, "Content 1"));
      repository.save(new DiaryEntry("Title 2", testAuthor, "Content 2"));

      List<DiaryEntry> entries = repository.findByAuthor(testAuthor);

      assertEquals(2, entries.size());
    }

    @Test
    @DisplayName("should return empty list when author has no entries")
    void shouldReturnEmptyListWhenAuthorHasNoEntries() {
      Author otherAuthor = authorRepository.save(new Author("Jane", "Smith", "jane@example.com"));

      List<DiaryEntry> entries = repository.findByAuthor(otherAuthor);

      assertTrue(entries.isEmpty());
    }

    @Test
    @DisplayName("should throw NullPointerException for null author")
    void shouldThrowForNullAuthor() {
      assertThrows(NullPointerException.class, () -> repository.findByAuthor(null));
    }
  }

  // findByAuthorId tests
  @Nested
  @DisplayName("findByAuthorId()")
  class FindByAuthorIdTests {

    @Test
    @DisplayName("should return entries by author ID")
    void shouldReturnEntriesByAuthorId() {
      repository.save(new DiaryEntry("Title 1", testAuthor, "Content 1"));
      repository.save(new DiaryEntry("Title 2", testAuthor, "Content 2"));

      List<DiaryEntry> entries = repository.findByAuthorId(testAuthor.getId());

      assertEquals(2, entries.size());
    }

    @Test
    @DisplayName("should return empty list for non-existent author ID")
    void shouldReturnEmptyListForNonExistentAuthorId() {
      List<DiaryEntry> entries = repository.findByAuthorId(999L);

      assertTrue(entries.isEmpty());
    }

    @Test
    @DisplayName("should throw NullPointerException for null author ID")
    void shouldThrowForNullAuthorId() {
      assertThrows(NullPointerException.class, () -> repository.findByAuthorId(null));
    }
  }

  // searchByTitleOrContent tests
  @Nested
  @DisplayName("searchByTitleOrContent()")
  class SearchTests {

    @Test
    @DisplayName("should find entries matching title")
    void shouldFindEntriesMatchingTitle() {
      repository.save(new DiaryEntry("My Vacation", testAuthor, "Content"));
      repository.save(new DiaryEntry("Work Day", testAuthor, "Content"));

      List<DiaryEntry> results = repository.searchByTitleOrContent("vacation");

      assertEquals(1, results.size());
      assertEquals("My Vacation", results.get(0).getTitle());
    }

    @Test
    @DisplayName("should find entries matching content")
    void shouldFindEntriesMatchingContent() {
      repository.save(new DiaryEntry("Title", testAuthor, "I went to the beach today"));
      repository.save(new DiaryEntry("Title 2", testAuthor, "Stayed home"));

      List<DiaryEntry> results = repository.searchByTitleOrContent("beach");

      assertEquals(1, results.size());
    }

    @Test
    @DisplayName("should be case-insensitive")
    void shouldBeCaseInsensitive() {
      repository.save(new DiaryEntry("IMPORTANT Day", testAuthor, "Content"));

      List<DiaryEntry> results = repository.searchByTitleOrContent("important");

      assertEquals(1, results.size());
    }

    @Test
    @DisplayName("should return empty list when no match")
    void shouldReturnEmptyListWhenNoMatch() {
      repository.save(new DiaryEntry("Title", testAuthor, "Content"));

      List<DiaryEntry> results = repository.searchByTitleOrContent("xyz123");

      assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("should throw NullPointerException for null search text")
    void shouldThrowForNullSearchText() {
      assertThrows(NullPointerException.class, () -> repository.searchByTitleOrContent(null));
    }
  }

  // findByDate tests
  @Nested
  @DisplayName("findByDate()")
  class FindByDateTests {

    @Test
    @DisplayName("should find entries created today")
    void shouldFindEntriesCreatedToday() {
      repository.save(new DiaryEntry("Title", testAuthor, "Content"));

      List<DiaryEntry> results = repository.findByDate(LocalDate.now());

      assertEquals(1, results.size());
    }

    @Test
    @DisplayName("should return empty list for date with no entries")
    void shouldReturnEmptyListForDateWithNoEntries() {
      repository.save(new DiaryEntry("Title", testAuthor, "Content"));

      List<DiaryEntry> results = repository.findByDate(LocalDate.now().minusDays(10));

      assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("should throw NullPointerException for null date")
    void shouldThrowForNullDate() {
      assertThrows(NullPointerException.class, () -> repository.findByDate(null));
    }
  }

  // findByDateRange tests
  @Nested
  @DisplayName("findByDateRange()")
  class FindByDateRangeTests {

    @Test
    @DisplayName("should find entries within date range")
    void shouldFindEntriesWithinDateRange() {
      repository.save(new DiaryEntry("Title", testAuthor, "Content"));

      List<DiaryEntry> results = repository.findByDateRange(
          LocalDate.now().minusDays(1),
          LocalDate.now().plusDays(1)
      );

      assertEquals(1, results.size());
    }

    @Test
    @DisplayName("should return empty list when no entries in range")
    void shouldReturnEmptyListWhenNoEntriesInRange() {
      repository.save(new DiaryEntry("Title", testAuthor, "Content"));

      List<DiaryEntry> results = repository.findByDateRange(
          LocalDate.now().minusDays(10),
          LocalDate.now().minusDays(5)
      );

      assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("should throw NullPointerException for null start date")
    void shouldThrowForNullStartDate() {
      assertThrows(NullPointerException.class,
          () -> repository.findByDateRange(null, LocalDate.now()));
    }

    @Test
    @DisplayName("should throw NullPointerException for null end date")
    void shouldThrowForNullEndDate() {
      assertThrows(NullPointerException.class,
          () -> repository.findByDateRange(LocalDate.now(), null));
    }
  }

  // update tests
  @Nested
  @DisplayName("update()")
  class UpdateTests {

    @Test
    @DisplayName("should update entry fields")
    void shouldUpdateEntryFields() {
      DiaryEntry entry = repository.save(new DiaryEntry("Original", testAuthor, "Content"));
      entry.setTitle("Updated Title");

      repository.update(entry);

      Optional<DiaryEntry> found = repository.findById(entry.getId());
      assertTrue(found.isPresent());
      assertEquals("Updated Title", found.get().getTitle());
    }

    @Test
    @DisplayName("should throw NullPointerException for null entry")
    void shouldThrowForNullEntry() {
      assertThrows(NullPointerException.class, () -> repository.update(null));
    }
  }

  // delete tests
  @Nested
  @DisplayName("delete()")
  class DeleteTests {

    @Test
    @DisplayName("should remove entry from database")
    void shouldRemoveEntryFromDatabase() {
      DiaryEntry entry = repository.save(new DiaryEntry("Title", testAuthor, "Content"));
      Long id = entry.getId();

      repository.delete(entry);

      assertTrue(repository.findById(id).isEmpty());
    }

    @Test
    @DisplayName("should throw NullPointerException for null entry")
    void shouldThrowForNullEntry() {
      assertThrows(NullPointerException.class, () -> repository.delete(null));
    }
  }

  // count tests
  @Nested
  @DisplayName("count()")
  class CountTests {

    @Test
    @DisplayName("should return 0 when no entries")
    void shouldReturnZeroWhenNoEntries() {
      assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("should return correct count")
    void shouldReturnCorrectCount() {
      repository.save(new DiaryEntry("Title 1", testAuthor, "Content 1"));
      repository.save(new DiaryEntry("Title 2", testAuthor, "Content 2"));
      repository.save(new DiaryEntry("Title 3", testAuthor, "Content 3"));

      assertEquals(3, repository.count());
    }
  }

  // countByAuthorId tests
  @Nested
  @DisplayName("countByAuthorId()")
  class CountByAuthorIdTests {

    @Test
    @DisplayName("should return 0 when author has no entries")
    void shouldReturnZeroWhenAuthorHasNoEntries() {
      assertEquals(0, repository.countByAuthorId(testAuthor.getId()));
    }

    @Test
    @DisplayName("should return correct count for author")
    void shouldReturnCorrectCountForAuthor() {
      repository.save(new DiaryEntry("Title 1", testAuthor, "Content 1"));
      repository.save(new DiaryEntry("Title 2", testAuthor, "Content 2"));

      Author otherAuthor = authorRepository.save(new Author("Jane", "Smith", "jane@example.com"));
      repository.save(new DiaryEntry("Title 3", otherAuthor, "Content 3"));

      assertEquals(2, repository.countByAuthorId(testAuthor.getId()));
      assertEquals(1, repository.countByAuthorId(otherAuthor.getId()));
    }

    @Test
    @DisplayName("should throw NullPointerException for null author ID")
    void shouldThrowForNullAuthorId() {
      assertThrows(NullPointerException.class, () -> repository.countByAuthorId(null));
    }
  }
}

