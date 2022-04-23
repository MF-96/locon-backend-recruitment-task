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

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
    BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.getBook("999"));

    assertThat(exception.getMessage()).isEqualTo("Book not found");
  }

  @Test
  public void testAddBook() {
    Book bookToAdd = Book.builder()
            .title("TITLE")
            .author("AUTHOR")
            .build();

    Book book = bookService.addBook(bookToAdd);

    assertThat(book).isNotNull()
            .extracting(Book::getTitle, Book::getAuthor)
            .containsExactly("TITLE", "AUTHOR");
    assertThat(book.getId()).isNotNull();

    assertThat(bookRepository.findById(book.getId())).isPresent();
  }

  @Test
  public void testAddBookFailEmptyTitle() {
    Book bookToAdd = Book.builder()
            .title("")
            .author("AUTHOR")
            .build();

    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> bookService.addBook(bookToAdd));
    assertThat(exception.getMessage()).isEqualTo("Invalid input");
  }

  @Test
  public void testAddBookFailEmptyAuthor() {
    Book bookToAdd = Book.builder()
            .title("TITLE")
            .author("")
            .build();

    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> bookService.addBook(bookToAdd));
    assertThat(exception.getMessage()).isEqualTo("Invalid input");
  }

  @Test
  public void testUpdateBook() {
    Book bookToUpdate = Book.builder()
            .id("1")
            .title("NEW TITLE")
            .author("NEW AUTHOR")
            .build();

    Book updatedBook = bookService.updateBook(bookToUpdate);

    assertThat(updatedBook).isNotNull()
            .extracting(Book::getId, Book::getTitle, Book::getAuthor)
            .containsExactly("1", "NEW TITLE", "NEW AUTHOR");

    Optional<BookEntity> updatedBookEntity = bookRepository.findById("1");
    assertThat(updatedBookEntity).isPresent();
    assertThat(updatedBookEntity.get())
            .extracting(BookEntity::getId, BookEntity::getTitle, BookEntity::getAuthor)
            .containsExactly("1", "NEW TITLE", "NEW AUTHOR");
  }

  @Test
  public void testUpdateBookNotFound() {
    Book bookToUpdate = Book.builder()
            .id("99999")
            .title("NEW TITLE")
            .author("NEW AUTHOR")
            .build();

    BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookToUpdate));
    assertThat(exception.getMessage()).isEqualTo("Book not found");
  }

  @Test
  public void testUpdateBookEmptyTitle() {
    Book bookToUpdate = Book.builder()
            .id("1")
            .title("")
            .author("NEW AUTHOR")
            .build();

    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> bookService.updateBook(bookToUpdate));
    assertThat(exception.getMessage()).isEqualTo("Invalid input");
  }

  @Test
  public void testUpdateBookEmptyAuthor() {
    Book bookToUpdate = Book.builder()
            .id("1")
            .title("NEW TITLE")
            .author("")
            .build();

    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> bookService.updateBook(bookToUpdate));
    assertThat(exception.getMessage()).isEqualTo("Invalid input");
  }

  @Test
  public void testDeleteBook() {
    bookService.deleteBook("1");

    assertThat(bookRepository.findById("1")).isNotPresent();
  }

  @Test
  public void testDeleteBookNotFound() {
    BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.deleteBook("999"));

    assertThat(exception.getMessage()).isEqualTo("Book not found");
  }
}