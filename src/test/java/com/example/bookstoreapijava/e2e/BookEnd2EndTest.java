package com.example.bookstoreapijava.e2e;

import static com.example.bookstoreapijava.providers.BookProvider.createBook;
import static com.example.bookstoreapijava.providers.BookProvider.createBookList;
import static com.example.bookstoreapijava.providers.BookUpdateDtoRequestProvider.createBookUpdateDtoRequest;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.providers.ExceptionDtoResponseProvider.createExceptionDtoResponse;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapijava.config.TestContainersBase;
import com.example.bookstoreapijava.main.data.dto.request.BookUpdateDtoRequest;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDtoResponse;
import com.example.bookstoreapijava.main.repositories.BookRepository;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import io.restassured.path.json.JsonPath;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class BookEnd2EndTest extends TestContainersBase {

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
    categoryRepository.deleteAll();
  }

  @Test
  @DisplayName("When book is searched by id, returns correctly")
  void getBookByIdSuccessfully() {
    Book expectedBook =
      createBook(null, null, null, null);

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
  @DisplayName("When book is searched by id and is not found, returns correctly")
  void getBookByIdNotFound() {
    UUID bookId = UUID.randomUUID();

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      404,
      "BookNotFoundException",
      "Book not found. BookId: " + bookId
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .baseUri(baseURI)
      .get("/bookstore/" + bookId)
      .then()
      .statusCode(404)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName("When book list is searched and there is no books, returns correctly")
  void getBookEmptyListSuccessfully() {
    JsonPath response = given()
      .baseUri(baseURI)
      .get("/bookstore")
      .then()
      .extract()
      .response()
      .jsonPath();

    List<Book> activeBooks = response.getList("content", Book.class);
    Boolean hasNextPage = response.getBoolean("hasNextPage");

    assertTrue(activeBooks.isEmpty());
    assertFalse(hasNextPage);
  }

  @Test
  @DisplayName("When book list is searched and there is books, returns correctly")
  void getBookListSuccessfully() {
    Category category = createCategory(null, null, null);
    List<Book> expectedBookList = createBookList(category);

    categoryRepository.save(category);
    bookRepository.saveAll(expectedBookList);

    JsonPath response = given()
      .baseUri(baseURI)
      .params("page", 0)
      .params("size", 1)
      .get("/bookstore")
      .then()
      .extract()
      .response()
      .jsonPath();

    List<Book> activeBooks = response.getList("content", Book.class);
    Boolean hasNextPage = response.getBoolean("hasNextPage");

    assertEquals(expectedBookList.getFirst(), activeBooks.getFirst());
    assertTrue(hasNextPage);
  }

  @Test
  @DisplayName("When book list is searched and there is active and inactive books, "
    + "returns only active books correctly")
  void getBookListSuccessfullyWithOnlyActiveBooks() {
    Category category = createCategory(null, null, null);
    List<Book> expectedBookList = createBookList(category);
    Book inactiveBook = createBook(null, category, LocalDateTime.now(), LocalDateTime.now());
    expectedBookList.add(inactiveBook);

    categoryRepository.save(category);
    bookRepository.saveAll(expectedBookList);

    List<Book> actualBookList = given()
      .baseUri(baseURI)
      .get("/bookstore")
      .then()
      .extract()
      .response()
      .jsonPath()
      .getList("content", Book.class);

    expectedBookList.remove(inactiveBook);

    assertEquals(expectedBookList, actualBookList);
    assertEquals(expectedBookList.size(), actualBookList.size());
  }

  @Test
  @DisplayName("When book is inserted and does not exist, returns correctly")
  void postBookSuccessfully() {
    Category category = createCategory(null, null, null);
    Book expectedBook = createBook(null, category, null, null);

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
  @DisplayName("When book is inserted, already exists and is active, throws exception correctly")
  void postBookAlreadyExists() {
    Category category = createCategory(null, null, null);
    Book book = createBook(null, category, null, null);

    categoryRepository.save(category);
    bookRepository.save(book);

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      409,
      "BookAlreadyExistsException",
      "Book already exists. Isbn: " + book.getIsbn()
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .contentType("application/json")
      .baseUri(baseURI)
      .body(book)
      .post("/bookstore")
      .then()
      .statusCode(409)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName("When book is inserted, already exist but is inactivated, "
    + "reactivate book and returns correctly")
  void postReactivateBookSuccessfully() {
    Category category = createCategory(null, null, null);
    LocalDateTime postTime = LocalDateTime.now();
    Book expectedBook = createBook(null, category, null, null);

    expectedBook.setInactivatedAt(postTime);
    expectedBook.setUpdatedAt(postTime);

    categoryRepository.save(category);
    bookRepository.save(expectedBook);

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

    assertEquals(expectedBook.getBookId(), actualBook.getBookId());
    assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
    assertNotNull(actualBook.getUpdatedAt());
    assertNull(actualBook.getInactivatedAt());
  }

  @Test
  @DisplayName("When book is updated, returns correctly")
  void patchBookSuccessfully() {
    String bookIsbn = "1313131313133";
    Category oldCategory = createCategory("Old Category", null, null);
    Category newCategory = createCategory("New Category", null, null);
    Book savedBook = createBook(bookIsbn, oldCategory, null, null);

    categoryRepository.save(oldCategory);
    categoryRepository.save(newCategory);
    bookRepository.save(savedBook);

    Map<String, String> bookInfo = new HashMap<>();
    bookInfo.put("title", "New Title");
    bookInfo.put("author", "New Author");
    bookInfo.put("isbn", "0000000000");

    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, newCategory.getCategoryId());

    Book updatedBook = given()
      .baseUri(baseURI)
      .contentType("application/json")
      .body(bookUpdateDtoRequest)
      .patch("/bookstore/" + savedBook.getBookId())
      .then()
      .statusCode(200)
      .extract()
      .as(Book.class);

    assertNotEquals(savedBook, updatedBook);
    assertEquals(updatedBook.getTitle(), bookInfo.get("title"));
    assertEquals(updatedBook.getAuthor(), bookInfo.get("author"));
    assertEquals(updatedBook.getIsbn(), bookInfo.get("isbn"));
    assertEquals(updatedBook.getCategory(), newCategory);
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName("When book is updated and is not found, throws exception correctly")
  void patchBookNotFound() {
    UUID bookId = UUID.randomUUID();

    Map<String, String> bookInfo = new HashMap<>();
    bookInfo.put("title", "New Title");
    bookInfo.put("author", "New Author");
    bookInfo.put("isbn", "0000000000");

    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, null);

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      404,
      "BookNotFoundException",
      "Book not found. BookId: " + bookId
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .contentType("application/json")
      .baseUri(baseURI)
      .body(bookUpdateDtoRequest)
      .patch("/bookstore/" + bookId)
      .then()
      .statusCode(404)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName("when book is updated and category is not found, throws exception correctly")
  void patchBookCategoryNotFound() {
    Book book = createBook(null, null, null, null);
    UUID notFoundCategoryId = UUID.randomUUID();
    Map<String, String> bookInfo = Map.of("title", "New title");
    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, notFoundCategoryId);

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      404,
      "CategoryNotFoundException",
      "Category not found. CategoryId: " + notFoundCategoryId
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .contentType("application/json")
      .baseUri(baseURI)
      .body(bookUpdateDtoRequest)
      .patch("/bookstore/" + book.getBookId())
      .then()
      .statusCode(404)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName("when book is updated and category is inactive, throws exception correctly")
  void patchBookCategoryInactive() {
    Category category = createCategory(null, LocalDateTime.now(), LocalDateTime.now());
    UUID categoryId = category.getCategoryId();
    Book book = createBook(null, category, null, null);
    Map<String, String> bookInfo = Map.of("title", "New title");
    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, categoryId);

    categoryRepository.save(category);
    bookRepository.save(book);

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      409,
      "CategoryIsInactiveException",
      "Category is inactive. CategoryId: " + categoryId
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .contentType("application/json")
      .baseUri(baseURI)
      .body(bookUpdateDtoRequest)
      .patch("/bookstore/" + book.getBookId())
      .then()
      .statusCode(409)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName("when book is updated and is inactive, reactivate book and returns correctly")
  void patchBookInactive() {
    Book book = createBook(null, null, LocalDateTime.now(), LocalDateTime.now());
    UUID categoryId = book.getCategory().getCategoryId();
    Map<String, String> bookInfo = Map.of("title", "New title");
    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, categoryId);

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    Book actualBook = given()
      .contentType("application/json")
      .baseUri(baseURI)
      .body(bookUpdateDtoRequest)
      .patch("/bookstore/" + book.getBookId())
      .then()
      .statusCode(200)
      .extract()
      .as(Book.class);

    assertNotEquals(book, actualBook);
    assertNotNull(actualBook.getUpdatedAt());
    assertNull(actualBook.getInactivatedAt());
  }

  @Test
  @DisplayName("When book is deleted, returns correctly")
  void deleteBookSuccessfully() {
    Category category = createCategory(null, null, null);
    Book savedBook = createBook(null, category, null, null);

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
  @DisplayName("When book is deleted and is not found, throws exception correctly")
  void deleteBookNotFound() {
    UUID bookId = UUID.randomUUID();

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      404,
      "BookNotFoundException",
      "Book not found. BookId: " + bookId
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .baseUri(baseURI)
      .delete("/bookstore/" + bookId)
      .then()
      .statusCode(404)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName("When book is already inactive and is deleted, throws exception correctly")
  void deleteBookAlreadyInactive() {
    Category category = createCategory(null, null, null);
    Book inactiveBook = createBook(
      null,
      category,
      LocalDateTime.now(),
      LocalDateTime.now()
    );
    UUID bookId = inactiveBook.getBookId();

    categoryRepository.save(category);
    bookRepository.save(inactiveBook);

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      409,
      "BookIsInactiveException",
      "Book is inactive. BookId: " + bookId
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .baseUri(baseURI)
      .delete("/bookstore/" + bookId)
      .then()
      .statusCode(409)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

}
