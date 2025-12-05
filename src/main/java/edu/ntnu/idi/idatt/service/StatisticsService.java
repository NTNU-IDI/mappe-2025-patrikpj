package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.model.entities.Author;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatisticsService {

  private final AuthorService authorService;
  private final DiaryEntryService diaryEntryService;

  public StatisticsService(AuthorService authorService, DiaryEntryService diaryEntryService) {
    this.authorService = authorService;
    this.diaryEntryService = diaryEntryService;
  }

  public long getTotalAuthors() {
    return authorService.findAll().size();
  }

  public long getTotalEntries() {
    return diaryEntryService.count();
  }

  /**
   * Returns a map of authors to their entry count.
   *
   * @return map with author as key and entry count as value
   */
  public Map<Author, Long> getEntriesPerAuthor() {
    Map<Author, Long> entriesPerAuthor = new LinkedHashMap<>();
    for (Author author : authorService.findAll()) {
      long count = diaryEntryService.countByAuthorId(author.getId());
      entriesPerAuthor.put(author, count);
    }
    return entriesPerAuthor;
  }
}