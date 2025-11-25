package edu.ntnu.idi.idatt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a diary entry with a title, an author, and textual content.
 *
 * <p>Each instance maintains timestamps for creation and the most recent update.
 * Timestamps are managed automatically by JPA lifecycle callbacks.</p>
 */
@Entity
@Table(name = "diary_entries")
public class DiaryEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  /**
   * Default constructor required by Hibernate.
   */
  public DiaryEntry() {
  }

  /**
   * Creates a new DiaryEntry with a title, author, and content.
   *
   * @param title   the entry title (cannot be null or blank)
   * @param author  the entry author (cannot be null)
   * @param content the entry content (cannot be null or blank)
   * @throws IllegalArgumentException if any argument is invalid
   */
  public DiaryEntry(String title, Author author, String content) {
    setTitle(title);
    setAuthor(author);
    setContent(content);
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
   * Returns the unique identifier of this diary entry.
   *
   * @return the entry ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the title of this diary entry.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the author of this diary entry.
   *
   * @return the author
   */
  public Author getAuthor() {
    return author;
  }

  /**
   * Returns the textual content of this diary entry.
   *
   * @return the content
   */
  public String getContent() {
    return content;
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

  // Setters

  /**
   * Sets the unique identifier of this diary entry.
   *
   * @param id the entry ID
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Updates the entry title.
   *
   * @param title the new title (cannot be null or blank)
   * @throws IllegalArgumentException if the title is null or blank
   */
  public void setTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("Title cannot be null or blank");
    }
    this.title = title;
  }

  /**
   * Updates the entry author.
   *
   * @param author the new author (cannot be null)
   * @throws IllegalArgumentException if the author is null
   */
  public void setAuthor(Author author) {
    if (author == null) {
      throw new IllegalArgumentException("Author cannot be null");
    }
    this.author = author;
  }

  /**
   * Updates the entry content.
   *
   * @param content the new content (cannot be null or blank)
   * @throws IllegalArgumentException if the content is null or blank
   */
  public void setContent(String content) {
    if (content == null || content.isBlank()) {
      throw new IllegalArgumentException("Content cannot be null or blank");
    }
    this.content = content;
  }

  // Overrides

  /**
   * Returns a string with the entry title and author name.
   *
   * @return a short summary of the entry
   */
  @Override
  public String toString() {
    return title + " - " + author.getFullName();
  }

  /**
   * Compares this diary entry to another object for equality.
   * Uses title, author, and content (not timestamps or id).
   *
   * @param o the object to compare with
   * @return true if both entries are equal; false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DiaryEntry that)) {
      return false;
    }
    return Objects.equals(title, that.title)
        && Objects.equals(author, that.author)
        && Objects.equals(content, that.content);
  }

  /**
   * Returns a hash code for this diary entry.
   *
   * @return the hash code value
   */
  @Override
  public int hashCode() {
    return Objects.hash(title, author, content);
  }
}

