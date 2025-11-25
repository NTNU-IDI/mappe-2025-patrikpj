package edu.ntnu.idi.idatt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

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

  public String getEmail() {
    return email;
  }

  /**
   * Sets the author's email.
   *
   * @param email the email (must be valid format)
   * @throws IllegalArgumentException if email is null, blank, or invalid format
   */
  public void setEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be null or blank");
    }
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      throw new IllegalArgumentException("Invalid email format");
    }
    this.email = email;
  }

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

