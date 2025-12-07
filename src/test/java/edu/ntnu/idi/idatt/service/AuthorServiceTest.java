package edu.ntnu.idi.idatt.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.repository.AuthorRepository;
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
class AuthorServiceTest {

  @Mock
  private AuthorRepository authorRepository;

  private AuthorService authorService;

  @BeforeEach
  void setUp() {
    authorService = new AuthorService(authorRepository);
  }

  // constructor tests
  @Nested
  @DisplayName("Constructor")
  class ConstructorTests {

    @Test
    @DisplayName("should throw NullPointerException for null repository")
    void shouldThrowForNullRepository() {
      assertThrows(NullPointerException.class, () -> new AuthorService(null));
    }

    @Test
    @DisplayName("should create service with valid repository")
    void shouldCreateServiceWithValidRepository() {
      assertDoesNotThrow(() -> new AuthorService(authorRepository));
    }
  }

  // createAuthor tests
  @Nested
  @DisplayName("createAuthor()")
  class CreateAuthorTests {

    @Test
    @DisplayName("should return empty when email already exists")
    void shouldReturnEmptyWhenEmailExists() {
      when(authorRepository.existsByEmail("john@example.com")).thenReturn(true);

      Optional<Author> result = authorService.createAuthor("John", "Doe", "john@example.com");

      assertTrue(result.isEmpty());
      verify(authorRepository).existsByEmail("john@example.com");
      verify(authorRepository, never()).save(any());
    }

    @Test
    @DisplayName("should create and return author when email is unique")
    void shouldCreateAuthorWhenEmailIsUnique() {
      when(authorRepository.existsByEmail("john@example.com")).thenReturn(false);
      when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

      Optional<Author> result = authorService.createAuthor("John", "Doe", "john@example.com");

      assertTrue(result.isPresent());
      assertEquals("John", result.get().getFirstName());
      assertEquals("Doe", result.get().getLastName());
      assertEquals("john@example.com", result.get().getEmail());
      verify(authorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("should pass correct author to repository")
    void shouldPassCorrectAuthorToRepository() {
      when(authorRepository.existsByEmail("john@example.com")).thenReturn(false);
      when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

      authorService.createAuthor("John", "Doe", "john@example.com");

      ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
      verify(authorRepository).save(authorCaptor.capture());
      Author savedAuthor = authorCaptor.getValue();
      assertEquals("John", savedAuthor.getFirstName());
      assertEquals("Doe", savedAuthor.getLastName());
      assertEquals("john@example.com", savedAuthor.getEmail());
    }
  }

  // createAuthorOrThrow tests
  @Nested
  @DisplayName("createAuthorOrThrow()")
  class CreateAuthorOrThrowTests {

    @Test
    @DisplayName("should throw IllegalArgumentException when email already exists")
    void shouldThrowWhenEmailExists() {
      when(authorRepository.existsByEmail("john@example.com")).thenReturn(true);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> authorService.createAuthorOrThrow("John", "Doe", "john@example.com"));

      assertTrue(exception.getMessage().contains("john@example.com"));
      assertTrue(exception.getMessage().contains("already exists"));
      verify(authorRepository, never()).save(any());
    }

    @Test
    @DisplayName("should create and return author when email is unique")
    void shouldCreateAuthorWhenEmailIsUnique() {
      when(authorRepository.existsByEmail("john@example.com")).thenReturn(false);
      when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

      Author result = authorService.createAuthorOrThrow("John", "Doe", "john@example.com");

      assertNotNull(result);
      assertEquals("John", result.getFirstName());
      verify(authorRepository).save(any(Author.class));
    }
  }

  // findById tests
  @Nested
  @DisplayName("findById()")
  class FindByIdTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      Author author = new Author("John", "Doe", "john@example.com");
      when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

      Optional<Author> result = authorService.findById(1L);

      assertTrue(result.isPresent());
      assertEquals(author, result.get());
      verify(authorRepository).findById(1L);
    }

    @Test
    @DisplayName("should return empty when not found")
    void shouldReturnEmptyWhenNotFound() {
      when(authorRepository.findById(1L)).thenReturn(Optional.empty());

      Optional<Author> result = authorService.findById(1L);

      assertTrue(result.isEmpty());
      verify(authorRepository).findById(1L);
    }
  }

  // findByEmail tests
  @Nested
  @DisplayName("findByEmail()")
  class FindByEmailTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      Author author = new Author("John", "Doe", "john@example.com");
      when(authorRepository.findByEmail("john@example.com")).thenReturn(Optional.of(author));

      Optional<Author> result = authorService.findByEmail("john@example.com");

      assertTrue(result.isPresent());
      assertEquals(author, result.get());
      verify(authorRepository).findByEmail("john@example.com");
    }
  }

  // findAll tests
  @Nested
  @DisplayName("findAll()")
  class FindAllTests {

    @Test
    @DisplayName("should return empty list when no authors")
    void shouldReturnEmptyListWhenNoAuthors() {
      when(authorRepository.findAll()).thenReturn(Collections.emptyList());

      List<Author> result = authorService.findAll();

      assertTrue(result.isEmpty());
      verify(authorRepository).findAll();
    }

    @Test
    @DisplayName("should return all authors from repository")
    void shouldReturnAllAuthorsFromRepository() {
      List<Author> authors = List.of(
          new Author("John", "Doe", "john@example.com"),
          new Author("Jane", "Smith", "jane@example.com")
      );
      when(authorRepository.findAll()).thenReturn(authors);

      List<Author> result = authorService.findAll();

      assertEquals(2, result.size());
      verify(authorRepository).findAll();
    }
  }

  // update tests
  @Nested
  @DisplayName("update()")
  class UpdateTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      Author author = new Author("John", "Doe", "john@example.com");
      when(authorRepository.update(author)).thenReturn(author);

      Author result = authorService.update(author);

      assertEquals(author, result);
      verify(authorRepository).update(author);
    }
  }

  // delete tests
  @Nested
  @DisplayName("delete()")
  class DeleteTests {

    @Test
    @DisplayName("should delegate to repository")
    void shouldDelegateToRepository() {
      Author author = new Author("John", "Doe", "john@example.com");

      authorService.delete(author);

      verify(authorRepository).delete(author);
    }
  }

  // emailExists tests
  @Nested
  @DisplayName("emailExists()")
  class EmailExistsTests {

    @Test
    @DisplayName("should return true when email exists")
    void shouldReturnTrueWhenEmailExists() {
      when(authorRepository.existsByEmail("john@example.com")).thenReturn(true);

      assertTrue(authorService.emailExists("john@example.com"));
      verify(authorRepository).existsByEmail("john@example.com");
    }

    @Test
    @DisplayName("should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {
      when(authorRepository.existsByEmail("john@example.com")).thenReturn(false);

      assertFalse(authorService.emailExists("john@example.com"));
      verify(authorRepository).existsByEmail("john@example.com");
    }
  }
}


