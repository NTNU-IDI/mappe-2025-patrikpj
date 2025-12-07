package edu.ntnu.idi.idatt.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiaryEntryServiceTest {

  @Mock
  private DiaryEntryRepository entryRepository;

  private DiaryEntryService diaryEntryService;
  private Author testAuthor;

    @BeforeEach
    void setUp() {
    diaryEntryService = new DiaryEntryService(entryRepository);
    testAuthor = new Author("John", "Doe", "john@example.com");
  }

  // constructor tests
  @Nested
  @DisplayName("Constructor")
  class ConstructorTests {

    @Test
    @DisplayName("should throw NullPointerException for null repository")
    void shouldThrowForNullRepository() {
      assertThrows(NullPointerException.class, () -> new DiaryEntryService(null));
    }

    @Test
    @DisplayName("should create service with valid repository")
    void shouldCreateServiceWithValidRepository() {
      assertDoesNotThrow(() -> new DiaryEntryService(entryRepository));
    }
  }

  // createEntry tests
  @Nested
  @DisplayName("createEntry()")
  class CreateEntryTests {

    @Test
    @DisplayName("should create and return entry")
    void shouldCreateAndReturnEntry() {
      when(entryRepository.save(any(DiaryEntry.class))).thenAnswer(inv -> inv.getArgument(0));

      DiaryEntry result = diaryEntryService.createEntry("My Day", testAuthor, "Great day!");

      assertNotNull(result);
      assertEquals("My Day", result.getTitle());
      assertEquals("Great day!", result.getContent());
      verify(entryRepository).save(any(DiaryEntry.class));
    }

    @Test
    @DisplayName("should pass correct entry to repository")
    void shouldPassCorrectEntryToRepository() {
      when(entryRepository.save(any(DiaryEntry.class))).thenAnswer(inv -> inv.getArgument(0));

      diaryEntryService.createEntry("Title", testAuthor, "Content");

      ArgumentCaptor<DiaryEntry> captor = ArgumentCaptor.forClass(DiaryEntry.class);
      verify(entryRepository).save(captor.capture());
      assertEquals("Title", captor.getValue().getTitle());
      assertEquals("Content", captor.getValue().getContent());
      assertEquals(testAuthor, captor.getValue().getAuthor());
    }
  }

  // findById tests
  @Nested
  @DisplayName("findById()")
  class FindByIdTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      DiaryEntry entry = new DiaryEntry("Title", testAuthor, "Content");
      when(entryRepository.findById(1L)).thenReturn(Optional.of(entry));

      Optional<DiaryEntry> result = diaryEntryService.findById(1L);

      assertTrue(result.isPresent());
      assertEquals(entry, result.get());
      verify(entryRepository).findById(1L);
    }

    @Test
    @DisplayName("should return empty when not found")
    void shouldReturnEmptyWhenNotFound() {
      when(entryRepository.findById(1L)).thenReturn(Optional.empty());

      Optional<DiaryEntry> result = diaryEntryService.findById(1L);

      assertTrue(result.isEmpty());
    }
  }

  // findAll tests
  @Nested
  @DisplayName("findAll()")
  class FindAllTests {

    @Test
    @DisplayName("should return empty list when no entries")
    void shouldReturnEmptyListWhenNoEntries() {
      when(entryRepository.findAll()).thenReturn(Collections.emptyList());

      List<DiaryEntry> result = diaryEntryService.findAll();

      assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should return all entries from repository")
    void shouldReturnAllEntriesFromRepository() {
      List<DiaryEntry> entries = List.of(
          new DiaryEntry("Title 1", testAuthor, "Content 1"),
          new DiaryEntry("Title 2", testAuthor, "Content 2")
      );
      when(entryRepository.findAll()).thenReturn(entries);

      List<DiaryEntry> result = diaryEntryService.findAll();

      assertEquals(2, result.size());
      verify(entryRepository).findAll();
    }
  }

  // findByAuthor tests
  @Nested
  @DisplayName("findByAuthor()")
  class FindByAuthorTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      List<DiaryEntry> entries = List.of(new DiaryEntry("Title", testAuthor, "Content"));
      when(entryRepository.findByAuthor(testAuthor)).thenReturn(entries);

      List<DiaryEntry> result = diaryEntryService.findByAuthor(testAuthor);

      assertEquals(1, result.size());
      verify(entryRepository).findByAuthor(testAuthor);
    }
  }

  // findByAuthorId tests
  @Nested
  @DisplayName("findByAuthorId()")
  class FindByAuthorIdTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      List<DiaryEntry> entries = List.of(new DiaryEntry("Title", testAuthor, "Content"));
      when(entryRepository.findByAuthorId(1L)).thenReturn(entries);

      List<DiaryEntry> result = diaryEntryService.findByAuthorId(1L);

      assertEquals(1, result.size());
      verify(entryRepository).findByAuthorId(1L);
    }
  }

  // search tests
  @Nested
  @DisplayName("search()")
  class SearchTests {

    @Test
    @DisplayName("should return empty list for null search text")
    void shouldReturnEmptyListForNullSearchText() {
      List<DiaryEntry> result = diaryEntryService.search(null);

      assertTrue(result.isEmpty());
      verifyNoInteractions(entryRepository);
    }

    @Test
    @DisplayName("should return empty list for blank search text")
    void shouldReturnEmptyListForBlankSearchText() {
      List<DiaryEntry> result = diaryEntryService.search("   ");

      assertTrue(result.isEmpty());
      verifyNoInteractions(entryRepository);
    }

    @Test
    @DisplayName("should delegate to repository for valid search text")
    void shouldDelegateToRepositoryForValidSearchText() {
      List<DiaryEntry> entries = List.of(new DiaryEntry("Vacation", testAuthor, "Beach day"));
      when(entryRepository.searchByTitleOrContent("vacation")).thenReturn(entries);

      List<DiaryEntry> result = diaryEntryService.search("vacation");

      assertEquals(1, result.size());
      verify(entryRepository).searchByTitleOrContent("vacation");
    }
  }

  // findByDate tests
  @Nested
  @DisplayName("findByDate()")
  class FindByDateTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      LocalDate today = LocalDate.now();
      List<DiaryEntry> entries = List.of(new DiaryEntry("Title", testAuthor, "Content"));
      when(entryRepository.findByDate(today)).thenReturn(entries);

      List<DiaryEntry> result = diaryEntryService.findByDate(today);

      assertEquals(1, result.size());
      verify(entryRepository).findByDate(today);
    }
  }

  // findByDateRange tests
  @Nested
  @DisplayName("findByDateRange()")
  class FindByDateRangeTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      LocalDate start = LocalDate.now().minusDays(7);
      LocalDate end = LocalDate.now();
      List<DiaryEntry> entries = List.of(new DiaryEntry("Title", testAuthor, "Content"));
      when(entryRepository.findByDateRange(start, end)).thenReturn(entries);

      List<DiaryEntry> result = diaryEntryService.findByDateRange(start, end);

      assertEquals(1, result.size());
      verify(entryRepository).findByDateRange(start, end);
    }
  }

  // updateTitle tests
  @Nested
  @DisplayName("updateTitle()")
  class UpdateTitleTests {

    @Test
    @DisplayName("should update title and delegate to repository")
    void shouldUpdateTitleAndDelegateToRepository() {
      DiaryEntry entry = new DiaryEntry("Old Title", testAuthor, "Content");
      when(entryRepository.update(entry)).thenReturn(entry);

      DiaryEntry result = diaryEntryService.updateTitle(entry, "New Title");

      assertEquals("New Title", result.getTitle());
      verify(entryRepository).update(entry);
    }
  }

  // updateContent tests
  @Nested
  @DisplayName("updateContent()")
  class UpdateContentTests {

    @Test
    @DisplayName("should update content and delegate to repository")
    void shouldUpdateContentAndDelegateToRepository() {
      DiaryEntry entry = new DiaryEntry("Title", testAuthor, "Old Content");
      when(entryRepository.update(entry)).thenReturn(entry);

      DiaryEntry result = diaryEntryService.updateContent(entry, "New Content");

      assertEquals("New Content", result.getContent());
      verify(entryRepository).update(entry);
    }
  }

  // update tests
  @Nested
  @DisplayName("update()")
  class UpdateTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      DiaryEntry entry = new DiaryEntry("Title", testAuthor, "Content");
      when(entryRepository.update(entry)).thenReturn(entry);

      DiaryEntry result = diaryEntryService.update(entry);

      assertEquals(entry, result);
      verify(entryRepository).update(entry);
    }
  }

  // delete tests
  @Nested
  @DisplayName("delete()")
  class DeleteTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      DiaryEntry entry = new DiaryEntry("Title", testAuthor, "Content");

      diaryEntryService.delete(entry);

      verify(entryRepository).delete(entry);
    }
  }

  // deleteById tests
  @Nested
  @DisplayName("deleteById()")
  class DeleteByIdTests {

    @Test
    @DisplayName("should return true and delete when entry exists")
    void shouldReturnTrueAndDeleteWhenEntryExists() {
      DiaryEntry entry = new DiaryEntry("Title", testAuthor, "Content");
      when(entryRepository.findById(1L)).thenReturn(Optional.of(entry));

      boolean result = diaryEntryService.deleteById(1L);

      assertTrue(result);
      verify(entryRepository).findById(1L);
      verify(entryRepository).delete(entry);
    }

    @Test
    @DisplayName("should return false when entry does not exist")
    void shouldReturnFalseWhenEntryDoesNotExist() {
      when(entryRepository.findById(1L)).thenReturn(Optional.empty());

      boolean result = diaryEntryService.deleteById(1L);

      assertFalse(result);
      verify(entryRepository).findById(1L);
      verify(entryRepository, never()).delete(any());
    }
  }

  // count tests
  @Nested
  @DisplayName("count()")
  class CountTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      when(entryRepository.count()).thenReturn(42L);

      long result = diaryEntryService.count();

      assertEquals(42L, result);
      verify(entryRepository).count();
    }
  }

  // countByAuthorId tests
  @Nested
  @DisplayName("countByAuthorId()")
  class CountByAuthorIdTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      when(entryRepository.countByAuthorId(1L)).thenReturn(5L);

      long result = diaryEntryService.countByAuthorId(1L);

      assertEquals(5L, result);
      verify(entryRepository).countByAuthorId(1L);
    }
  }
}

