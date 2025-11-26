package edu.ntnu.idi.idatt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents an author entity in the database.
 *
 * <p>Instances of this class enforce non-null, non-blank names and a syntactically
 * valid email address format.</p>
 */
@Entity
@Table(name = "authors")
public class Author {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  /**
   * Checks if an email address has a valid format.
   *
   * @param email the email to validate
   * @return true if valid, false otherwise
   */
  public static boolean isValidEmail(String email) {
    if (email == null || email.isBlank()) {
      return false;
    }
    return EMAIL_PATTERN.matcher(email.toLowerCase()).matches();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  /**
   * Default constructor required by Hibernate.
   */
  public Author() {
  }

  /**
   * Creates a new Author with the given names and email.
   *
   * @param firstName the author's first name (cannot be null or blank)
   * @param lastName  the author's last name (cannot be null or blank)
   * @param email     the author's email (must be valid format)
   * @throws IllegalArgumentException if any argument is null, blank, or invalid
   */
  public Author(String firstName, String lastName, String email) {
    setFirstName(firstName);
    setLastName(lastName);
    setEmail(email);
  }

  /**
   * Sets timestamps before first save.
   */
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  /**
   * Updates timestamp before each update.
   */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  // Getters

  /**
   * Returns the unique identifier of this author.
   *
   * @return the author ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this author.
   *
   * @param id the author ID
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the first name of this author.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  // Setters

  /**
   * Sets the author's first name.
   *
   * @param firstName the first name (cannot be null or blank)
   * @throws IllegalArgumentException if firstName is null or blank
   */
  public void setFirstName(String firstName) {
    if (firstName == null || firstName.isBlank()) {
      throw new IllegalArgumentException("First name cannot be null or blank");
    }
    this.firstName = firstName;
  }

  /**
   * Returns the last name of this author.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the author's last name.
   *
   * @param lastName the last name (cannot be null or blank)
   * @throws IllegalArgumentException if lastName is null or blank
   */
  public void setLastName(String lastName) {
    if (lastName == null || lastName.isBlank()) {
      throw new IllegalArgumentException("Last name cannot be null or blank");
    }
    this.lastName = lastName;
  }

  /**
   * Returns the author's full name.
   *
   * @return first name + last name
   */
  public String getFullName() {
    return firstName + " " + lastName;
  }

  /**
   * Returns the email address of this author.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the author's email. The email is normalized to lowercase.
   *
   * @param email the email (must be valid format)
   * @throws IllegalArgumentException if email is null, blank, or invalid format
   */
  public void setEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be null or blank");
    }
    String normalizedEmail = email.toLowerCase();
    if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
      throw new IllegalArgumentException("Invalid email format");
    }
    this.email = normalizedEmail;
  }

  /**
   * Returns the creation timestamp.
   *
   * @return the creation date and time
   */
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  /**
   * Returns the last update timestamp.
   *
   * @return the last update date and time
   */
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  // Overrides

  /**
   * Returns a string representation of this author.
   *
   * @return the author's full name and email
   */
  @Override
  public String toString() {
    return firstName + " " + lastName + " (" + email + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Author author)) {
      return false;
    }
    return Objects.equals(firstName, author.firstName)
        && Objects.equals(lastName, author.lastName)
        && Objects.equals(email, author.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, email);
  }
}

