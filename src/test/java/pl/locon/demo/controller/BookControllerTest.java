package pl.locon.demo.controller;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.http.converter.xml.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import pl.locon.demo.dto.*;
import pl.locon.demo.exception.*;
import pl.locon.demo.service.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MappingJackson2XmlHttpMessageConverter xmlMapper;

  @MockBean
  private BookService bookService;

  @Test
  public void testGetBookListAsJson() throws Exception {
    Books books = new Books(Arrays.asList(
            new Book("1", "TITLE 1", "AUTHOR 1"),
            new Book("2", "TITLE 2", "AUTHOR 2"),
            new Book("3", "TITLE 3", "AUTHOR 3")
    ));
    when(bookService.getBookList()).thenReturn(books);

    mockMvc.perform(MockMvcRequestBuilders.get("/bookstore-web/book")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.books").isArray())
            .andExpect(jsonPath("$.books[0].id").value("1"))
            .andExpect(jsonPath("$.books[0].title").value("TITLE 1"))
            .andExpect(jsonPath("$.books[0].author").value("AUTHOR 1"))
            .andExpect(jsonPath("$.books[1].id").value("2"))
            .andExpect(jsonPath("$.books[1].title").value("TITLE 2"))
            .andExpect(jsonPath("$.books[1].author").value("AUTHOR 2"))
            .andExpect(jsonPath("$.books[2].id").value("3"))
            .andExpect(jsonPath("$.books[2].title").value("TITLE 3"))
            .andExpect(jsonPath("$.books[2].author").value("AUTHOR 3"));
  }

  @Test
  public void testGetBookListAsXml() throws Exception {
    Books books = new Books(Arrays.asList(
            new Book("1", "TITLE 1", "AUTHOR 1"),
            new Book("2", "TITLE 2", "AUTHOR 2"),
            new Book("3", "TITLE 3", "AUTHOR 3")
    ));
    when(bookService.getBookList()).thenReturn(books);

    mockMvc.perform(MockMvcRequestBuilders.get("/bookstore-web/book")
                    .accept(MediaType.APPLICATION_XML_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
            .andExpect(xpath("//Books").exists())
            .andExpect(xpath("//Books/Book").nodeCount(3))
            .andExpect(xpath("//Books/Book[1]/id").string("1"))
            .andExpect(xpath("//Books/Book[1]/title").string("TITLE 1"))
            .andExpect(xpath("//Books/Book[1]/author").string("AUTHOR 1"))
            .andExpect(xpath("//Books/Book[2]/id").string("2"))
            .andExpect(xpath("//Books/Book[2]/title").string("TITLE 2"))
            .andExpect(xpath("//Books/Book[2]/author").string("AUTHOR 2"))
            .andExpect(xpath("//Books/Book[3]/id").string("3"))
            .andExpect(xpath("//Books/Book[3]/title").string("TITLE 3"))
            .andExpect(xpath("//Books/Book[3]/author").string("AUTHOR 3"));
  }

  @Test
  public void testGetBookAsJson() throws Exception {
    Book book = new Book("1", "TITLE 1", "AUTHOR 1");
    when(bookService.getBook("1")).thenReturn(book);

    mockMvc.perform(MockMvcRequestBuilders.get("/bookstore-web/book/1")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.title").value("TITLE 1"))
            .andExpect(jsonPath("$.author").value("AUTHOR 1"));
  }

  @Test
  public void testGetBookAsXml() throws Exception {
    Book book = new Book("1", "TITLE 1", "AUTHOR 1");
    when(bookService.getBook("1")).thenReturn(book);

    mockMvc.perform(MockMvcRequestBuilders.get("/bookstore-web/book/1")
                    .accept(MediaType.APPLICATION_XML_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
            .andExpect(xpath("//Book/id").string("1"))
            .andExpect(xpath("//Book/title").string("TITLE 1"))
            .andExpect(xpath("//Book/author").string("AUTHOR 1"));
  }

  @Test
  public void testGetBookNotFound() throws Exception {
    when(bookService.getBook(any(String.class))).thenThrow(new EntityNotFoundException("Book not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/bookstore-web/book/1")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getMessage(), "Book not found"));
  }

  @Test
  public void testAddBookJsonResponse() throws Exception {
    Book book = new Book("1", "TITLE 1", "AUTHOR 1");
    when(bookService.addBook(any(Book.class))).thenReturn(book);

    mockMvc.perform(MockMvcRequestBuilders.post("/bookstore-web/book")
                    .content(objectMapper.writeValueAsString(book))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("TITLE 1"))
            .andExpect(jsonPath("$.author").value("AUTHOR 1"));
  }

  @Test
  public void testAddBookXmlResponse() throws Exception {
    Book book = new Book("1", "TITLE 1", "AUTHOR 1");
    when(bookService.addBook(any(Book.class))).thenReturn(book);

    mockMvc.perform(MockMvcRequestBuilders.post("/bookstore-web/book")
                    .content(xmlMapper.getObjectMapper().writeValueAsString(book))
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .accept(MediaType.APPLICATION_XML_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(xpath("//Book/id").string("1"))
            .andExpect(xpath("//Book/title").string("TITLE 1"))
            .andExpect(xpath("//Book/author").string("AUTHOR 1"));
  }

  @Test
  public void testAddBookFailEmptyTitle() throws Exception {
    Book book = new Book("1", "", "AUTHOR 1");
    when(bookService.addBook(any(Book.class))).thenThrow(new InvalidInputException());

    mockMvc.perform(MockMvcRequestBuilders.post("/bookstore-web/book")
                    .content(objectMapper.writeValueAsString(book))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidInputException))
            .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getMessage(), "Invalid input"));
  }

  @Test
  public void testAddBookFailEmptyAuthor() throws Exception {
    Book book = new Book("1", "TITLE 1", "");
    when(bookService.addBook(any(Book.class))).thenThrow(new InvalidInputException());

    mockMvc.perform(MockMvcRequestBuilders.post("/bookstore-web/book")
                    .content(objectMapper.writeValueAsString(book))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidInputException))
            .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getMessage(), "Invalid input"));
  }
}