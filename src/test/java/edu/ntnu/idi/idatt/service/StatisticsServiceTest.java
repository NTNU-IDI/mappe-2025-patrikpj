package edu.ntnu.idi.idatt.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static edu.ntnu.idi.idatt.model.entities.TestEntityHelper.setAuthorId;

import edu.ntnu.idi.idatt.model.entities.Author;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

  @Mock
  private AuthorService authorService;

  @Mock
  private DiaryEntryService diaryEntryService;

  private StatisticsService statisticsService;

  @BeforeEach
  void setUp() {
    statisticsService = new StatisticsService(authorService, diaryEntryService);
  }

  // getTotalAuthors tests
  @Nested
  @DisplayName("getTotalAuthors()")
  class GetTotalAuthorsTests {

    @Test
    @DisplayName("should return 0 when no authors exist")
    void shouldReturnZeroWhenNoAuthorsExist() {
      when(authorService.findAll()).thenReturn(Collections.emptyList());

      assertEquals(0, statisticsService.getTotalAuthors());
      verify(authorService).findAll();
    }

    @Test
    @DisplayName("should return correct count of authors")
    void shouldReturnCorrectCountOfAuthors() {
      List<Author> authors = List.of(
          new Author("John", "Doe", "john@example.com"),
          new Author("Jane", "Smith", "jane@example.com"),
          new Author("Bob", "Wilson", "bob@example.com")
      );
      when(authorService.findAll()).thenReturn(authors);

      assertEquals(3, statisticsService.getTotalAuthors());
      verify(authorService).findAll();
    }
  }

  // getTotalEntries tests
  @Nested
  @DisplayName("getTotalEntries()")
  class GetTotalEntriesTests {

    @Test
    @DisplayName("should return 0 when no entries exist")
    void shouldReturnZeroWhenNoEntriesExist() {
      when(diaryEntryService.count()).thenReturn(0L);

      assertEquals(0, statisticsService.getTotalEntries());
      verify(diaryEntryService).count();
    }

    @Test
    @DisplayName("should return correct count of entries")
    void shouldReturnCorrectCountOfEntries() {
      when(diaryEntryService.count()).thenReturn(42L);

      assertEquals(42, statisticsService.getTotalEntries());
      verify(diaryEntryService).count();
    }
  }

  // getEntriesPerAuthor tests
  @Nested
  @DisplayName("getEntriesPerAuthor()")
  class GetEntriesPerAuthorTests {

    @Test
    @DisplayName("should return empty map when no authors exist")
    void shouldReturnEmptyMapWhenNoAuthorsExist() {
      when(authorService.findAll()).thenReturn(Collections.emptyList());

      Map<Author, Long> result = statisticsService.getEntriesPerAuthor();

      assertTrue(result.isEmpty());
      verify(authorService).findAll();
      verifyNoInteractions(diaryEntryService);
    }

    @Test
    @DisplayName("should return 0 entries for authors with no entries")
    void shouldReturnZeroEntriesForAuthorsWithNoEntries() {
      Author author = new Author("Test", "Author", "test@example.com");
      setAuthorId(author, 1L);
      
      when(authorService.findAll()).thenReturn(List.of(author));
      when(diaryEntryService.countByAuthorId(1L)).thenReturn(0L);

      Map<Author, Long> result = statisticsService.getEntriesPerAuthor();

      assertEquals(1, result.size());
      assertEquals(0L, result.get(author));
      verify(diaryEntryService).countByAuthorId(1L);
    }

    @Test
    @DisplayName("should return correct entry count per author")
    void shouldReturnCorrectEntryCountPerAuthor() {
      Author author1 = new Author("John", "Doe", "john@example.com");
      Author author2 = new Author("Jane", "Smith", "jane@example.com");
      setAuthorId(author1, 1L);
      setAuthorId(author2, 2L);
      
      when(authorService.findAll()).thenReturn(List.of(author1, author2));
      when(diaryEntryService.countByAuthorId(1L)).thenReturn(3L);
      when(diaryEntryService.countByAuthorId(2L)).thenReturn(1L);

      Map<Author, Long> result = statisticsService.getEntriesPerAuthor();

      assertEquals(2, result.size());
      assertEquals(3L, result.get(author1));
      assertEquals(1L, result.get(author2));
    }

    @Test
    @DisplayName("should call countByAuthorId for each author")
    void shouldCallCountByAuthorIdForEachAuthor() {
      Author author1 = new Author("John", "Doe", "john@example.com");
      Author author2 = new Author("Jane", "Smith", "jane@example.com");
      Author author3 = new Author("Bob", "Wilson", "bob@example.com");
      setAuthorId(author1, 1L);
      setAuthorId(author2, 2L);
      setAuthorId(author3, 3L);
      
      when(authorService.findAll()).thenReturn(List.of(author1, author2, author3));
      when(diaryEntryService.countByAuthorId(anyLong())).thenReturn(0L);

      statisticsService.getEntriesPerAuthor();

      verify(diaryEntryService).countByAuthorId(1L);
      verify(diaryEntryService).countByAuthorId(2L);
      verify(diaryEntryService).countByAuthorId(3L);
      verify(diaryEntryService, times(3)).countByAuthorId(anyLong());
    }
  }
}
