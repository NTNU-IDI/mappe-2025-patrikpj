package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.model.entities.Author;
import edu.ntnu.idi.idatt.model.entities.DiaryEntry;
import edu.ntnu.idi.idatt.repository.DiaryEntryRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for DiaryEntry business logic.
 */
public class DiaryEntryService {

  private final DiaryEntryRepository entryRepository;

  /**
   * Creates a new DiaryEntryService with the given repository.
   *
   * @param entryRepository the diary entry repository
   * @throws NullPointerException if entryRepository is null
   */
  public DiaryEntryService(DiaryEntryRepository entryRepository) {
    this.entryRepository = Objects.requireNonNull(entryRepository,
        "DiaryEntryRepository cannot be null");
  }

  /**
   * Creates a new diary entry.
   *
   * @param title   the entry title
   * @param author  the entry author
   * @param content the entry content
   * @return the created diary entry
   * @throws NullPointerException     if any argument is null
   * @throws IllegalArgumentException if title or content is blank
   */
  public DiaryEntry createEntry(String title, Author author, String content) {
    DiaryEntry entry = new DiaryEntry(title, author, content);
    return entryRepository.save(entry);
  }

  /**
   * Finds a diary entry by its ID.
   *
   * @param id the entry ID
   * @return an Optional containing the entry, or empty if not found
   * @throws NullPointerException if id is null
   */
  public Optional<DiaryEntry> findById(Long id) {
    return entryRepository.findById(id);
  }

  /**
   * Retrieves all diary entries.
   *
   * @return a list of all diary entries
   */
  public List<DiaryEntry> findAll() {
    return entryRepository.findAll();
  }

  /**
   * Finds all diary entries by a specific author.
   *
   * @param author the author
   * @return a list of entries by the author
   * @throws NullPointerException if author is null
   */
  public List<DiaryEntry> findByAuthor(Author author) {
    return entryRepository.findByAuthor(author);
  }

  /**
   * Finds all diary entries by author ID.
   *
   * @param authorId the author ID
   * @return a list of entries by the author
   * @throws NullPointerException if authorId is null
   */
  public List<DiaryEntry> findByAuthorId(Long authorId) {
    return entryRepository.findByAuthorId(authorId);
  }

  /**
   * Searches for diary entries containing the given text in title or content.
   *
   * @param searchText the text to search for (null or blank returns empty list)
   * @return a list of matching entries
   */
  public List<DiaryEntry> search(String searchText) {
    if (searchText == null || searchText.isBlank()) {
      return List.of();
    }
    return entryRepository.searchByTitleOrContent(searchText);
  }

  /**
   * Finds all diary entries created on a specific date.
   *
   * @param date the date to search for
   * @return a list of entries created on that date
   * @throws NullPointerException if date is null
   */
  public List<DiaryEntry> findByDate(java.time.LocalDate date) {
    return entryRepository.findByDate(date);
  }

  /**
   * Updates a diary entry's title.
   *
   * @param entry    the entry to update
   * @param newTitle the new title
   * @return the updated entry
   * @throws NullPointerException     if entry or newTitle is null
   * @throws IllegalArgumentException if newTitle is blank
   */
  public DiaryEntry updateTitle(DiaryEntry entry, String newTitle) {
    entry.setTitle(newTitle);
    return entryRepository.update(entry);
  }

  /**
   * Updates a diary entry's content.
   *
   * @param entry      the entry to update
   * @param newContent the new content
   * @return the updated entry
   * @throws NullPointerException     if entry or newContent is null
   * @throws IllegalArgumentException if newContent is blank
   */
  public DiaryEntry updateContent(DiaryEntry entry, String newContent) {
    entry.setContent(newContent);
    return entryRepository.update(entry);
  }

  /**
   * Updates a diary entry completely.
   *
   * @param entry the entry to update
   * @return the updated entry
   * @throws NullPointerException if entry is null
   */
  public DiaryEntry update(DiaryEntry entry) {
    return entryRepository.update(entry);
  }

  /**
   * Deletes a diary entry.
   *
   * @param entry the entry to delete
   * @throws NullPointerException if entry is null
   */
  public void delete(DiaryEntry entry) {
    entryRepository.delete(entry);
  }

  /**
   * Deletes a diary entry by ID if it exists.
   *
   * @param id the entry ID
   * @return true if the entry was deleted, false if not found
   * @throws NullPointerException if id is null
   */
  public boolean deleteById(Long id) {
    Optional<DiaryEntry> entry = entryRepository.findById(id);
    if (entry.isPresent()) {
      entryRepository.delete(entry.get());
      return true;
    }
    return false;
  }

  /**
   * Gets the total count of diary entries.
   *
   * @return the total count
   */
  public long count() {
    return entryRepository.count();
  }

  /**
   * Gets the count of diary entries by a specific author.
   *
   * @param authorId the author ID
   * @return the count of entries by the author
   * @throws NullPointerException if authorId is null
   */
  public long countByAuthorId(Long authorId) {
    return entryRepository.countByAuthorId(authorId);
  }
}
