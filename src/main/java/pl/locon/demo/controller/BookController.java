package pl.locon.demo.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pl.locon.demo.dto.*;
import pl.locon.demo.service.*;

@RestController
@RequestMapping("/bookstore-web/book")
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<Books> getBookList() {
    return ResponseEntity.ok(bookService.getBookList());
  }

  @GetMapping(value = "/{bookId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<Book> getBook(@PathVariable String bookId) {
    return ResponseEntity.ok(bookService.getBook(bookId));
  }

  @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
          consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<Book> addBook(@RequestBody Book bookToAdd) {
    return new ResponseEntity<>(bookService.addBook(bookToAdd), HttpStatus.CREATED);
  }

  @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
          consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<Book> updateBook(@RequestBody Book bookToUpdate) {
    return new ResponseEntity<>(bookService.updateBook(bookToUpdate), HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/{bookId}")
  public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
    bookService.deleteBook(bookId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
