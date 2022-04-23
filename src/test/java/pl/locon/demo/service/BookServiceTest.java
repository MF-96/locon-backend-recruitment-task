package pl.locon.demo.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.context.junit.jupiter.*;
import pl.locon.demo.dto.*;
import pl.locon.demo.exception.*;
import pl.locon.demo.factory.*;
import pl.locon.demo.model.*;
import pl.locon.demo.repository.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class BookServiceTest {

  @Autowired
  private BookRepository bookRepository;

  private BookService bookService;

  @BeforeAll
  public void setup() {
    BookFactory bookFactory = new BookFactory();
    bookService = new BookService(bookRepository, bookFactory);

    bookRepository.save(new BookEntity("1", "TITLE 1", "AUTHOR 1"));
    bookRepository.save(new BookEntity("2", "TITLE 2", "AUTHOR 2"));
    bookRepository.save(new BookEntity("3", "TITLE 3", "AUTHOR 3"));
  }

  @Test
  public void testGetBookList() {
    Books books = bookService.getBookList();

    assertThat(books).isNotNull();
    assertThat(books.getBooks()).isNotNull().hasSize(3)
            .extracting(Book::getId, Book::getTitle, Book::getAuthor)
            .containsExactlyInAnyOrder(
                    tuple("1", "TITLE 1", "AUTHOR 1"),
                    tuple("2", "TITLE 2", "AUTHOR 2"),
                    tuple("3", "TITLE 3", "AUTHOR 3")
            );
  }

  @Test
  public void testGetBook() {
    Book book = bookService.getBook("1");

    assertThat(book).isNotNull()
            .extracting(Book::getId, Book::getTitle, Book::getAuthor)
            .containsExactly("1", "TITLE 1", "AUTHOR 1");
  }

  @Test
  public void testGetBookNotFound() {
    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.getBook("999"));

    assertThat(exception.getMessage()).isEqualTo("Book not found");
  }
}