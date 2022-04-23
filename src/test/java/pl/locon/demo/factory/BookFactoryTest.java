package pl.locon.demo.factory;

import org.junit.jupiter.api.*;
import pl.locon.demo.dto.*;
import pl.locon.demo.model.*;

import static org.assertj.core.api.Assertions.assertThat;

public class BookFactoryTest {

  private final BookFactory bookFactory = new BookFactory();

  @Test
  public void testFromEntity() {
    BookEntity bookEntity = new BookEntity("1", "TITLE 1", "AUTHOR 1");

    Book book = bookFactory.fromEntity(bookEntity);

    assertThat(book).isNotNull()
            .extracting(Book::getId, Book::getTitle, Book::getAuthor)
            .containsExactly("1", "TITLE 1", "AUTHOR 1");
  }

}