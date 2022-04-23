package pl.locon.demo.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.*;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JacksonXmlRootElement(localName = "Books")
public class Books {

  @JacksonXmlProperty(localName = "Book")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Book> books;
}
