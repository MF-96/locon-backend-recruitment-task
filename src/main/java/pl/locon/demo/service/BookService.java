package pl.locon.demo.service;

import lombok.*;
import org.springframework.stereotype.*;
import pl.locon.demo.dto.*;
import pl.locon.demo.exception.*;
import pl.locon.demo.factory.*;
import pl.locon.demo.model.*;
import pl.locon.demo.repository.*;

import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final BookFactory bookFactory;

  public Books getBookList() {
    List<Book> bookList = bookRepository.findAll().stream()
            .map(bookFactory::fromEntity)
            .collect(Collectors.toList());
    return new Books(bookList);
  }

  public Book getBook(String id) {
    BookEntity bookEntity = bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    return bookFactory.fromEntity(bookEntity);
  }
}
