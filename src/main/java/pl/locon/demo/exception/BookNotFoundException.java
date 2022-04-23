package pl.locon.demo.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException() {
    super("Book not found");
  }
}
