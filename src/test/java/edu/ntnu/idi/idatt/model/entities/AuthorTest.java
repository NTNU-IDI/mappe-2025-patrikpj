package edu.ntnu.idi.idatt.model.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class AuthorTest {

  // email validation
  @Nested
  @DisplayName("isValidEmail()")
  class IsValidEmailTests {
    
    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "name.lastname@domain.org",
        "name@example.co.uk",
        "USER@EXAMPLE.COM",
        "a@b.no"
    })
    @DisplayName("should return true for valid emails")
    void shouldReturnTrueForValidEmails(String email) {
      assertTrue(Author.isValidEmail(email));
    }
    
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
        "",
        "   ",
        "notanemail",
        "@nodomain.com",
        "noat.com",
        "missing@tld",
        "spaces in@email.com"
    })
    @DisplayName("should return false for invalid emails")
    void shouldReturnFalseForInvalidEmails(String email) {
      assertFalse(Author.isValidEmail(email));
    }
  }

  // constructor tests
  @Nested
  @DisplayName("Constructor")
  class ConstructorTests {
    
    @Test
    @DisplayName("should create author with valid arguments")
    void shouldCreateAuthorWithValidArguments() {
      Author author = new Author("John", "Doe", "john.doe@example.com");
      
      assertEquals("John", author.getFirstName());
      assertEquals("Doe", author.getLastName());
      assertEquals("john.doe@example.com", author.getEmail());
    }
    
    @Test
    @DisplayName("should trim whitespace from names")
    void shouldTrimWhitespaceFromNames() {
      Author author = new Author("  John  ", "  Doe  ", "john@example.com");
      
      assertEquals("John", author.getFirstName());
      assertEquals("Doe", author.getLastName());
    }
    
    @Test
    @DisplayName("should normalize email to lowercase")
    void shouldNormalizeEmailToLowercase() {
      Author author = new Author("John", "Doe", "JOHN.DOE@EXAMPLE.COM");
      
      assertEquals("john.doe@example.com", author.getEmail());
    }
    
    @Test
    @DisplayName("should throw NullPointerException for null firstName")
    void shouldThrowForNullFirstName() {
      assertThrows(NullPointerException.class,
          () -> new Author(null, "Doe", "john@example.com"));
    }
    
    @Test
    @DisplayName("should throw NullPointerException for null lastName")
    void shouldThrowForNullLastName() {
      assertThrows(NullPointerException.class,
          () -> new Author("John", null, "john@example.com"));
    }
    
    @Test
    @DisplayName("should throw NullPointerException for null email")
    void shouldThrowForNullEmail() {
      assertThrows(NullPointerException.class,
          () -> new Author("John", "Doe", null));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("should throw IllegalArgumentException for blank firstName")
    void shouldThrowForBlankFirstName(String firstName) {
      assertThrows(IllegalArgumentException.class,
          () -> new Author(firstName, "Doe", "john@example.com"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("should throw IllegalArgumentException for blank lastName")
    void shouldThrowForBlankLastName(String lastName) {
      assertThrows(IllegalArgumentException.class,
          () -> new Author("John", lastName, "john@example.com"));
    }
    
    @Test
    @DisplayName("should throw IllegalArgumentException for invalid email format")
    void shouldThrowForInvalidEmail() {
      assertThrows(IllegalArgumentException.class,
          () -> new Author("John", "Doe", "not-an-email"));
    }
  }

  // setters test
  @Nested
  @DisplayName("Setters")
  class SetterTests {
    
    private Author author;
    
    @BeforeEach
    void setUp() {
      author = new Author("John", "Doe", "john@example.com");
    }
    
    @Test
    @DisplayName("setFirstName should update and trim the name")
    void setFirstNameShouldUpdateAndTrim() {
      author.setFirstName("  Jane  ");
      assertEquals("Jane", author.getFirstName());
    }
    
    @Test
    @DisplayName("setLastName should update and trim the name")
    void setLastNameShouldUpdateAndTrim() {
      author.setLastName("  Smith  ");
      assertEquals("Smith", author.getLastName());
    }
    
    @Test
    @DisplayName("setEmail should update and normalize to lowercase")
    void setEmailShouldUpdateAndNormalize() {
      author.setEmail("NEW.EMAIL@EXAMPLE.COM");
      assertEquals("new.email@example.com", author.getEmail());
    }
  }
}

// other methods
@Nested
@DisplayName("Other Methods")
class OtherMethodsTests {
  
  private Author author;
  
  @BeforeEach
  void setUp() {
    author = new Author("John", "Doe", "john@example.com");
  }
  
  @Test
  @DisplayName("getFullName should return firstName + lastName")
  void getFullNameShouldCombineNames() {
    assertEquals("John Doe", author.getFullName());
  }
  
  @Test
  @DisplayName("toDisplayString should return formatted string")
  void toDisplayStringShouldReturnFormattedString() {
    assertEquals("John Doe (john@example.com)", author.toDisplayString());
  }
  
  @Test
  @DisplayName("toString should contain relevant info")
  void toStringShouldContainRelevantInfo() {
    String result = author.toString();
    
    assertTrue(result.contains("Author"));
    assertTrue(result.contains("John Doe"));
    assertTrue(result.contains("john@example.com"));
  }

  // equals and hashCode tests
  @Nested
  @DisplayName("equals() and hashCode()")
  class EqualsHashCodeTests {
    
    @Test
    @DisplayName("equals should return true for same email")
    void equalsShouldReturnTrueForSameEmail() {
      Author author1 = new Author("John", "Doe", "same@example.com");
      Author author2 = new Author("Jane", "Smith", "same@example.com");
      
      assertEquals(author1, author2);
    }
    
    @Test
    @DisplayName("equals should return false for different email")
    void equalsShouldReturnFalseForDifferentEmail() {
      Author author1 = new Author("John", "Doe", "john@example.com");
      Author author2 = new Author("John", "Doe", "jane@example.com");
      
      assertNotEquals(author1, author2);
    }
    
    @Test
    @DisplayName("equals should return true for same object")
    void equalsShouldReturnTrueForSameObject() {
      Author author = new Author("John", "Doe", "john@example.com");
      
      assertEquals(author, author);
    }
    
    @Test
    @DisplayName("equals should return false for null")
    void equalsShouldReturnFalseForNull() {
      Author author = new Author("John", "Doe", "john@example.com");
      
      assertNotEquals(null, author);
    }
    
    @Test
    @DisplayName("equals should return false for different type")
    void equalsShouldReturnFalseForDifferentType() {
      Author author = new Author("John", "Doe", "john@example.com");
      
      assertNotEquals("john@example.com", author);
    }
    
    @Test
    @DisplayName("hashCode should be equal for equal objects")
    void hashCodeShouldBeEqualForEqualObjects() {
      Author author1 = new Author("John", "Doe", "same@example.com");
      Author author2 = new Author("Jane", "Smith", "same@example.com");
      
      assertEquals(author1.hashCode(), author2.hashCode());
    }
    
    @Test
    @DisplayName("hashCode should be consistent")
    void hashCodeShouldBeConsistent() {
      Author author = new Author("John", "Doe", "john@example.com");
      int hashCode1 = author.hashCode();
      int hashCode2 = author.hashCode();
      
      assertEquals(hashCode1, hashCode2);
    }
  }
}