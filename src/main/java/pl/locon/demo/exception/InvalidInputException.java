package pl.locon.demo.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidInputException extends RuntimeException {

  public InvalidInputException() {
    super("Invalid input");
  }
}
