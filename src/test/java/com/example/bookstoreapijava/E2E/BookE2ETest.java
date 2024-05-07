package com.example.bookstoreapijava.E2E;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDTOResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

import static com.example.bookstoreapijava.providers.BookProvider.createBook;
import static com.example.bookstoreapijava.providers.BookProvider.createBookList;
import static com.example.bookstoreapijava.providers.BookUpdateDTORequestProvider.createBookUpdateDTORequest;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.providers.ExceptionDTOResponseProvider.createExceptionDTOResponse;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class BookE2ETest extends PostgresTestContainersBase {

  @Value("${app.baseUrl}")
  private static String baseUrl;

  @Autowired
  BookRepository bookRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @BeforeAll
  void setup() {
    baseURI = "http://localhost:" + port;
  }

  @AfterEach
  void cleanUpDb() {
    bookRepository.deleteAll();
  }

  @Test
  @DisplayName(value = "When book is searched by id, returns correctly")
  void getBookByIdSuccessfully() {
    Book expectedBook = createBook(Optional.empty(), Optional.empty());

    categoryRepository.save(expectedBook.getCategory());
    bookRepository.save(expectedBook);

    Book actualBook = given()
        .baseUri(baseURI)
        .get("/bookstore/" + expectedBook.getBookId())
        .then()
        .statusCode(200)
        .extract()
        .as(Book.class);

    assertEquals(expectedBook, actualBook);
  }

  @Test
  @DisplayName(value = "When book is searched by id, returns correctly")
  void getBookByIdNotFound() {
    UUID bookId = UUID.randomUUID();

    ExceptionDTOResponse expectedExceptionDTOResponse =
        createExceptionDTOResponse(
            Optional.of(404),
            Optional.of("BookNotFoundException"),
            Optional.of("Book not found with id " + bookId)
        );

    ExceptionDTOResponse actualExceptionDTOResponse = given()
        .baseUri(baseURI)
        .get("/bookstore/" + bookId)
        .then()
        .statusCode(404)
        .extract()
        .as(ExceptionDTOResponse.class);

    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
  }

  @Test
  @DisplayName(value = "When book list is searched and there is not books, returns correctly")
  void getBookEmptyListSuccessfully() {
    List<Book> actualBookList = Arrays.asList(given()
        .baseUri(baseURI)
        .get("/bookstore")
        .then()
        .extract()
        .as(Book[].class));

    assertTrue(actualBookList.isEmpty());
  }

  @Test
  @DisplayName(value = "When book list is searched and there is categories, returns correctly")
  void getBookListSuccessfully() {
    Category category = createCategory(Optional.empty());
    List<Book> expectedBookList = createBookList(Optional.of(category));

    categoryRepository.save(category);
    bookRepository.saveAll(expectedBookList);

    List<Book> actualBookList = Arrays.asList(given()
        .baseUri(baseURI)
        .get("/bookstore")
        .then()
        .extract()
        .as(Book[].class));

    assertEquals(expectedBookList, actualBookList);
    assertFalse(actualBookList.isEmpty());
  }

  @Test
  @DisplayName(value = "When book is inserted, returns correctly")
  void postBookSuccessfully() {
    Category category = createCategory(Optional.empty());
    Book expectedBook = createBook(Optional.empty(), Optional.of(category));

    categoryRepository.save(category);

    Book actualBook = given()
        .contentType("application/json")
        .baseUri(baseURI)
        .body(expectedBook)
        .post("/bookstore")
        .then()
        .header("Location", baseUrl + "/bookstore/" + expectedBook.getBookId())
        .statusCode(201)
        .extract()
        .as(Book.class);

    assertEquals(expectedBook, actualBook);
  }

  @Test
  @DisplayName(value = "When book is inserted and already exists, throws exception correctly")
  void postBookAlreadyExists() {
    Category category = createCategory(Optional.empty());
    Book book = createBook(Optional.empty(), Optional.of(category));

    categoryRepository.save(category);
    bookRepository.save(book);

    ExceptionDTOResponse expectedExceptionDTOResponse = createExceptionDTOResponse(
        Optional.of(409),
        Optional.of("BookAlreadyExistsException"),
        Optional.of("Book already exists with isbn " + book.getIsbn())
    );

    ExceptionDTOResponse actualExceptionDTOResponse = given()
        .contentType("application/json")
        .baseUri(baseURI)
        .body(book)
        .post("/bookstore")
        .then()
        .statusCode(409)
        .extract()
        .as(ExceptionDTOResponse.class);

    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
  }

  @Test
  @DisplayName(value = "When book is updated, returns correctly")
  void updateBookSuccessfully() {
    String bookIsbn = "1313131313133";
    Category oldCategory = createCategory(Optional.of("Old Category"));
    Category newCategory = createCategory(Optional.of("New Category"));
    Book savedBook = createBook(Optional.of(bookIsbn), Optional.of(oldCategory));

    categoryRepository.save(oldCategory);
    categoryRepository.save(newCategory);
    bookRepository.save(savedBook);

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.of("New Title"));
      put("author", Optional.of("New Author"));
      put("isbn", Optional.of("0000000000"));
    }};

    BookUpdateDTORequest bookUpdateDTORequest =
        createBookUpdateDTORequest(bookInfo, Optional.of(newCategory.getCategoryId()));

    Book updatedBook = given()
        .baseUri(baseURI)
        .contentType("application/json")
        .body(bookUpdateDTORequest)
        .patch("/bookstore/" + savedBook.getBookId())
        .then()
        .statusCode(200)
        .extract()
        .as(Book.class);

    assertNotEquals(savedBook, updatedBook);
    assertEquals(updatedBook.getTitle(), bookInfo.get("title").get());
    assertEquals(updatedBook.getAuthor(), bookInfo.get("author").get());
    assertEquals(updatedBook.getIsbn(), bookInfo.get("isbn").get());
    assertEquals(updatedBook.getCategory(), newCategory);
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName(value = "When book is updated and is not found, throws exception correctly")
  void updateBookNotFound() {
    UUID bookId = UUID.randomUUID();

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.of("New Title"));
      put("author", Optional.of("New Author"));
      put("isbn", Optional.of("0000000000"));
    }};

    BookUpdateDTORequest bookUpdateDTORequest =
        createBookUpdateDTORequest(bookInfo, Optional.empty());

    ExceptionDTOResponse expectedExceptionDTOResponse = createExceptionDTOResponse(
        Optional.of(404),
        Optional.of("BookNotFoundException"),
        Optional.of("Book not found with id " + bookId)
    );

    ExceptionDTOResponse actualExceptionDTOResponse = given()
        .contentType("application/json")
        .baseUri(baseURI)
        .body(bookUpdateDTORequest)
        .patch("/bookstore/" + bookId)
        .then()
        .statusCode(404)
        .extract()
        .as(ExceptionDTOResponse.class);

    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
  }

  @Test
  @DisplayName(value = "When book is deleted, returns correctly")
  void deleteBookSuccessfully() {
    Category category = createCategory(Optional.empty());
    Book savedBook = createBook(Optional.empty(), Optional.of(category));

    categoryRepository.save(category);
    bookRepository.save(savedBook);

    given()
        .baseUri(baseURI)
        .delete("/bookstore/" + savedBook.getBookId())
        .then()
        .statusCode(204);

    Book deletedBook = bookRepository.findById(savedBook.getBookId()).get();

    assertNotEquals(savedBook, deletedBook);
    assertNotNull(deletedBook.getUpdatedAt());
    assertNotNull(deletedBook.getInactivatedAt());
  }

  @Test
  @DisplayName(value = "When book is deleted and is not found, throws exception correctly")
  void deleteBookNotFound() {
    UUID bookId = UUID.randomUUID();

    ExceptionDTOResponse expectedExceptionDTOResponse = createExceptionDTOResponse(
        Optional.of(404),
        Optional.of("BookNotFoundException"),
        Optional.of("Book not found with id " + bookId)
    );

    ExceptionDTOResponse actualExceptionDTOResponse = given()
        .baseUri(baseURI)
        .delete("/bookstore/" + bookId)
        .then()
        .statusCode(404)
        .extract()
        .as(ExceptionDTOResponse.class);

    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
  }

}
