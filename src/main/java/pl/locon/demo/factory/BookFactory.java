package pl.locon.demo.factory;

import org.springframework.stereotype.*;
import pl.locon.demo.dto.*;
import pl.locon.demo.model.*;

@Service
public class BookFactory {

  public Book fromEntity(BookEntity bookEntity) {
    return Book.builder()
            .id(bookEntity.getId())
            .title(bookEntity.getTitle())
            .author(bookEntity.getAuthor())
            .build();
  }

  public BookEntity toEntity(Book book) {
    return BookEntity.builder()
            .author(book.getAuthor())
            .title(book.getTitle())
            .build();
  }
}
