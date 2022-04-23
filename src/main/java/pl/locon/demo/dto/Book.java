package pl.locon.demo.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.*;

import java.io.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JacksonXmlRootElement(localName = "Book")
public class Book implements Serializable {

  @JacksonXmlProperty(localName = "id")
  @JsonProperty(value = "id")
  private String id;

  @JacksonXmlProperty(localName = "title")
  @JsonProperty(value = "title")
  private String title;

  @JacksonXmlProperty(localName = "author")
  @JsonProperty(value = "author")
  private String author;
}
