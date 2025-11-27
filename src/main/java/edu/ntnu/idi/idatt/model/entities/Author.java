package edu.ntnu.idi.idatt.model.entities;

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
  protected Author() {
  }

  /**
   * Creates a new Author with the given names and email.
   *
   * @param firstName the author's first name (cannot be null or blank)
   * @param lastName  the author's last name (cannot be null or blank)
   * @param email     the author's email (must be valid format)
   * @throws NullPointerException     if any argument is null
   * @throws IllegalArgumentException if any argument is blank or email is invalid
   */
  public Author(String firstName, String lastName, String email) {
    setFirstName(firstName);
    setLastName(lastName);
    setEmail(email);
  }

  /**
   * Sets timestamps before first persist.
   */
  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Updates timestamp before each update.
   */
  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Returns the unique identifier of this author.
   *
   * @return the author ID, or null if not yet persisted
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the first name of this author.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the author's first name.
   *
   * @param firstName the first name (cannot be null or blank)
   * @throws NullPointerException     if firstName is null
   * @throws IllegalArgumentException if firstName is blank
   */
  public void setFirstName(String firstName) {
    Objects.requireNonNull(firstName, "First name cannot be null");
    if (firstName.isBlank()) {
      throw new IllegalArgumentException("First name cannot be blank");
    }
    this.firstName = firstName.trim();
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
   * @throws NullPointerException     if lastName is null
   * @throws IllegalArgumentException if lastName is blank
   */
  public void setLastName(String lastName) {
    Objects.requireNonNull(lastName, "Last name cannot be null");
    if (lastName.isBlank()) {
      throw new IllegalArgumentException("Last name cannot be blank");
    }
    this.lastName = lastName.trim();
  }

  /**
   * Returns the author's full name.
   *
   * @return first name and last name
   */
  public String getFullName() {
    return firstName + " " + lastName;
  }

  /**
   * Returns the email address of this author.
   *
   * @return the email (always lowercase)
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the author's email. The email is normalized to lowercase and trimmed.
   *
   * @param email the email (must be valid format)
   * @throws NullPointerException     if email is null
   * @throws IllegalArgumentException if email is blank or invalid format
   */
  public void setEmail(String email) {
    Objects.requireNonNull(email, "Email cannot be null");
    if (email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be blank");
    }
    String normalizedEmail = email.trim().toLowerCase();
    if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
      throw new IllegalArgumentException("Invalid email format: " + email);
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

  /**
   * Returns a string representation of this author.
   *
   * @return the author's full name followed by email in parentheses
   */
  @Override
  public String toString() {
    return getFullName() + " (" + email + ")";
  }

  /**
   * Compares this author to another based on email.
   *
   * @param o the object to compare
   * @return true if emails match, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Author other)) {
      return false;
    }
    return Objects.equals(email, other.email);
  }

  /**
   * Returns a hash code based on email.
   *
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}
