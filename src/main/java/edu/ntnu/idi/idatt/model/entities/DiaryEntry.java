package edu.ntnu.idi.idatt.model.entities;

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
  protected DiaryEntry() {
  }

  /**
   * Creates a new DiaryEntry with a title, author, and content.
   *
   * @param title   the entry title (cannot be null or blank)
   * @param author  the entry author (cannot be null)
   * @param content the entry content (cannot be null or blank)
   * @throws NullPointerException     if any argument is null
   * @throws IllegalArgumentException if title or content is blank
   */
  public DiaryEntry(String title, Author author, String content) {
    setTitle(title);
    setAuthor(author);
    setContent(content);
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
   * Returns the unique identifier of this diary entry.
   *
   * @return the entry ID, or null if not yet persisted
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
   * Sets the entry title.
   *
   * @param title the new title (cannot be null or blank)
   * @throws NullPointerException     if title is null
   * @throws IllegalArgumentException if title is blank
   */
  public void setTitle(String title) {
    Objects.requireNonNull(title, "Title cannot be null");
    if (title.isBlank()) {
      throw new IllegalArgumentException("Title cannot be blank");
    }
    this.title = title.trim();
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
   * Sets the entry author.
   *
   * @param author the new author (cannot be null)
   * @throws NullPointerException if author is null
   */
  public void setAuthor(Author author) {
    this.author = Objects.requireNonNull(author, "Author cannot be null");
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
   * Sets the entry content.
   *
   * @param content the new content (cannot be null or blank)
   * @throws NullPointerException     if content is null
   * @throws IllegalArgumentException if content is blank
   */
  public void setContent(String content) {
    Objects.requireNonNull(content, "Content cannot be null");
    if (content.isBlank()) {
      throw new IllegalArgumentException("Content cannot be blank");
    }
    this.content = content.trim();
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
   * Returns a string representation of this entry for debugging/logging.
   *
   * @return string with class name and key fields
   */
  @Override
  public String toString() {
    return "DiaryEntry{id=" + id + ", title='" + title + "', authorId=" 
        + (author != null ? author.getId() : null) + "}";
  }

  /**
   * Returns a formatted string suitable for display.
   *
   * @return the entry title with author name
   */
  public String toDisplayString() {
    return title + " by " + author.getFullName();
  }

  /**
   * Returns a short summary of the content.
   *
   * @param maxLength the maximum length of the summary
   * @return truncated content with ellipsis if needed
   */
  public String getContentPreview(int maxLength) {
    if (content.length() <= maxLength) {
      return content;
    }
    return content.substring(0, maxLength - 3) + "...";
  }

  /**
   * Compares this diary entry to another based on id.
   *
   * @param o the object to compare with
   * @return true if IDs match, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DiaryEntry other)) {
      return false;
    }
    // Use id if persisted, otherwise fall back to identity
    return id != null && Objects.equals(id, other.id);
  }

  /**
   * Returns a hash code based on the id.
   *
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

