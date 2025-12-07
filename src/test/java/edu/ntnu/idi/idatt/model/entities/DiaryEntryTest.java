package edu.ntnu.idi.idatt.model.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiaryEntryTest {

  private Author validAuthor;

  @BeforeEach
  void setUp() {
    validAuthor = new Author("John", "Doe", "john@example.com");
  }

  // constructor tests
  @Nested
  @DisplayName("Constructor")
  class ConstructorTests {

    @Test
    @DisplayName("should create diary entry with valid arguments")
    void shouldCreateDiaryEntryWithValidArguments() {
      DiaryEntry entry = new DiaryEntry("My Title", validAuthor, "My content");

      assertEquals("My Title", entry.getTitle());
      assertEquals(validAuthor, entry.getAuthor());
      assertEquals("My content", entry.getContent());
    }

    @Test
    @DisplayName("should trim whitespace from title")
    void shouldTrimWhitespaceFromTitle() {
      DiaryEntry entry = new DiaryEntry("  My Title  ", validAuthor, "Content");

      assertEquals("My Title", entry.getTitle());
    }

    @Test
    @DisplayName("should trim whitespace from content")
    void shouldTrimWhitespaceFromContent() {
      DiaryEntry entry = new DiaryEntry("Title", validAuthor, "  My content  ");

      assertEquals("My content", entry.getContent());
    }

    @Test
    @DisplayName("should throw NullPointerException for null title")
    void shouldThrowForNullTitle() {
      assertThrows(NullPointerException.class,
          () -> new DiaryEntry(null, validAuthor, "Content"));
    }

    @Test
    @DisplayName("should throw NullPointerException for null author")
    void shouldThrowForNullAuthor() {
      assertThrows(NullPointerException.class,
          () -> new DiaryEntry("Title", null, "Content"));
    }

    @Test
    @DisplayName("should throw NullPointerException for null content")
    void shouldThrowForNullContent() {
      assertThrows(NullPointerException.class,
          () -> new DiaryEntry("Title", validAuthor, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("should throw IllegalArgumentException for blank title")
    void shouldThrowForBlankTitle(String title) {
      assertThrows(IllegalArgumentException.class,
          () -> new DiaryEntry(title, validAuthor, "Content"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("should throw IllegalArgumentException for blank content")
    void shouldThrowForBlankContent(String content) {
      assertThrows(IllegalArgumentException.class,
          () -> new DiaryEntry("Title", validAuthor, content));
    }
  }

  // setters tests
  @Nested
  @DisplayName("Setters")
  class SetterTests {

    private DiaryEntry entry;

    @BeforeEach
    void setUp() {
      entry = new DiaryEntry("Original Title", validAuthor, "Original content");
    }

    @Test
    @DisplayName("setTitle should update and trim the title")
    void setTitleShouldUpdateAndTrim() {
      entry.setTitle("  New Title  ");
      assertEquals("New Title", entry.getTitle());
    }

    @Test
    @DisplayName("setTitle should throw NullPointerException for null")
    void setTitleShouldThrowForNull() {
      assertThrows(NullPointerException.class, () -> entry.setTitle(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    @DisplayName("setTitle should throw IllegalArgumentException for blank")
    void setTitleShouldThrowForBlank(String title) {
      assertThrows(IllegalArgumentException.class, () -> entry.setTitle(title));
    }

    @Test
    @DisplayName("setAuthor should update the author")
    void setAuthorShouldUpdate() {
      Author newAuthor = new Author("Jane", "Smith", "jane@example.com");
      entry.setAuthor(newAuthor);
      assertEquals(newAuthor, entry.getAuthor());
    }

    @Test
    @DisplayName("setAuthor should throw NullPointerException for null")
    void setAuthorShouldThrowForNull() {
      assertThrows(NullPointerException.class, () -> entry.setAuthor(null));
    }

    @Test
    @DisplayName("setContent should update and trim the content")
    void setContentShouldUpdateAndTrim() {
      entry.setContent("  New content  ");
      assertEquals("New content", entry.getContent());
    }

    @Test
    @DisplayName("setContent should throw NullPointerException for null")
    void setContentShouldThrowForNull() {
      assertThrows(NullPointerException.class, () -> entry.setContent(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    @DisplayName("setContent should throw IllegalArgumentException for blank")
    void setContentShouldThrowForBlank(String content) {
      assertThrows(IllegalArgumentException.class, () -> entry.setContent(content));
    }
  }

  // other methods
  @Nested
  @DisplayName("Other Methods")
  class OtherMethodsTests {

    private DiaryEntry entry;

    @BeforeEach
    void setUp() {
      entry = new DiaryEntry("My Day", validAuthor, "Today was a great day!");
    }

    @Test
    @DisplayName("toDisplayString should return title with author name")
    void toDisplayStringShouldReturnTitleWithAuthorName() {
      assertEquals("My Day by John Doe", entry.toDisplayString());
    }

    @Test
    @DisplayName("toString should contain relevant info")
    void toStringShouldContainRelevantInfo() {
      String result = entry.toString();

      assertTrue(result.contains("DiaryEntry"));
      assertTrue(result.contains("My Day"));
    }

    @Nested
    @DisplayName("getContentPreview()")
    class ContentPreviewTests {

      @Test
      @DisplayName("should return full content when shorter than maxLength")
      void shouldReturnFullContentWhenShorter() {
        DiaryEntry shortEntry = new DiaryEntry("Title", validAuthor, "Short");
        
        assertEquals("Short", shortEntry.getContentPreview(100));
      }

      @Test
      @DisplayName("should return full content when equal to maxLength")
      void shouldReturnFullContentWhenEqual() {
        DiaryEntry entry = new DiaryEntry("Title", validAuthor, "12345");
        
        assertEquals("12345", entry.getContentPreview(5));
      }

      @Test
      @DisplayName("should truncate with ellipsis when longer than maxLength")
      void shouldTruncateWithEllipsisWhenLonger() {
        DiaryEntry longEntry = new DiaryEntry("Title", validAuthor, 
            "This is a very long content that should be truncated");
        
        String preview = longEntry.getContentPreview(20);
        
        assertEquals(20, preview.length());
        assertTrue(preview.endsWith("..."));
        assertEquals("This is a very lo...", preview);
      }

      @Test
      @DisplayName("should handle minimum maxLength correctly")
      void shouldHandleMinimumMaxLength() {
        DiaryEntry entry = new DiaryEntry("Title", validAuthor, "Hello World");
        
        String preview = entry.getContentPreview(4);
        
        assertEquals("H...", preview);
      }
    }
  }

  // equals and hashCode tests
  @Nested
  @DisplayName("equals() and hashCode()")
  class EqualsHashCodeTests {

    @Test
    @DisplayName("equals should return true for same object")
    void equalsShouldReturnTrueForSameObject() {
      DiaryEntry entry = new DiaryEntry("Title", validAuthor, "Content");

      assertEquals(entry, entry);
    }

    @Test
    @DisplayName("equals should return false for null")
    void equalsShouldReturnFalseForNull() {
      DiaryEntry entry = new DiaryEntry("Title", validAuthor, "Content");

      assertNotEquals(null, entry);
    }

    @Test
    @DisplayName("equals should return false for different type")
    void equalsShouldReturnFalseForDifferentType() {
      DiaryEntry entry = new DiaryEntry("Title", validAuthor, "Content");

      assertNotEquals("not a diary entry", entry);
    }

    @Test
    @DisplayName("unpersisted entries with null id should not be equal")
    void unpersistedEntriesShouldNotBeEqual() {
      // Both have null id, so equals returns false (by design for JPA entities)
      DiaryEntry entry1 = new DiaryEntry("Title", validAuthor, "Content");
      DiaryEntry entry2 = new DiaryEntry("Title", validAuthor, "Content");

      // id is null for both, so they should NOT be equal (per the implementation)
      assertNotEquals(entry1, entry2);
    }

    @Test
    @DisplayName("hashCode should be consistent")
    void hashCodeShouldBeConsistent() {
      DiaryEntry entry = new DiaryEntry("Title", validAuthor, "Content");
      int hashCode1 = entry.hashCode();
      int hashCode2 = entry.hashCode();

      assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("unpersisted entries should have same hashCode (both based on null id)")
    void unpersistedEntriesShouldHaveSameHashCode() {
      DiaryEntry entry1 = new DiaryEntry("Title1", validAuthor, "Content1");
      DiaryEntry entry2 = new DiaryEntry("Title2", validAuthor, "Content2");

      // Both have null id, so Objects.hash(null) returns same value
      assertEquals(entry1.hashCode(), entry2.hashCode());
    }
  }
}