package com.example.bookstoreapijava.integration.services;

import static com.example.bookstoreapijava.providers.BookCreatedVoProvider.createBookCreatedVo;
import static com.example.bookstoreapijava.providers.BookProvider.createBook;
import static com.example.bookstoreapijava.providers.BookProvider.createBookList;
import static com.example.bookstoreapijava.providers.BookUpdateDtoRequestProvider.createBookUpdateDtoRequest;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.data.dto.request.BookUpdateDtoRequest;
import com.example.bookstoreapijava.main.data.dto.response.PageResponse;
import com.example.bookstoreapijava.main.data.vo.BookCreatedVo;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.BookAlreadyExistsException;
import com.example.bookstoreapijava.main.exceptions.BookIsInactiveException;
import com.example.bookstoreapijava.main.exceptions.BookNotFoundException;
import com.example.bookstoreapijava.main.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.repositories.BookRepository;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.services.BookService;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookServiceIntegrationTest extends PostgresTestContainersBase {

  @Autowired
  BookRepository bookRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  BookService bookService;

  @AfterEach
  public void cleanUpDb() {
    bookRepository.deleteAll();
  }

  @Test
  @DisplayName("When book does not exists and is inserted, should returns correctly")
  void should_returnCorrectly_when_bookIsInsertedAndDoesNotExists() throws URISyntaxException {
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());
    BookCreatedVo bookCreatedMock = createBookCreatedVo(book);

    categoryRepository.save(book.getCategory());

    BookCreatedVo bookCreated = bookService.insertBook(book);

    assertNotNull(bookCreated);
    assertEquals(bookCreatedMock, bookCreated);
  }

  @Test
  @DisplayName("When book is inserted, already exists and is active, "
    + "should throw exception correctly")
  void should_throwException_when_bookIsInsertedIsActiveAndAlreadyExists() {
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    BookAlreadyExistsException bookAlreadyExistsException = assertThrows(
      BookAlreadyExistsException.class,
      () -> bookService.insertBook(book)
    );

    String expectedExceptionMessage = "Book already exists. Isbn: " + book.getIsbn();

    assertTrue(bookAlreadyExistsException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When book is inserted and category is not found, should throw exception correctly")
  void should_throwException_when_bookIsInsertedAndCategoryIsNotFound() {
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());

    CategoryNotFoundException categoryNotFoundException = assertThrows(
      CategoryNotFoundException.class,
      () -> bookService.insertBook(book)
    );

    String expectedExceptionMessage =
      "Category not found. CategoryId: " + book.getCategory().getCategoryId();

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When book is inserted, already exists and is inactivated, should reactivate it")
  void should_reactivateBook_when_bookIsInsertedAlreadyExistsAndIsInactivated()
    throws URISyntaxException {
    Category category = createCategory(Optional.empty());
    Book expectedBook = createBook(Optional.empty(), Optional.of(category), Optional.empty());
    LocalDateTime insertDate = LocalDateTime.now();

    expectedBook.setInactivatedAt(insertDate);
    expectedBook.setUpdatedAt(insertDate);

    categoryRepository.save(category);
    bookRepository.save(expectedBook);

    BookCreatedVo expectedBookCreated = createBookCreatedVo(expectedBook);
    BookCreatedVo actualBookCreated = bookService.insertBook(expectedBook);

    assertEquals(expectedBookCreated.book().getBookId(), actualBookCreated.book().getBookId());
    assertEquals(expectedBookCreated.book().getIsbn(), actualBookCreated.book().getIsbn());
    assertNotNull(actualBookCreated.book().getUpdatedAt());
    assertNull(actualBookCreated.book().getInactivatedAt());
  }

  @Test
  @DisplayName("When book list is searched, should return correctly")
  void should_returnEquals_when_bookListIsSearched() {
    Category category = createCategory(Optional.of("Book List Test"));
    List<Book> bookList = createBookList(Optional.of(category));

    categoryRepository.save(category);
    bookRepository.saveAll(bookList);

    PageResponse<Book> savedBookList = bookService.findAllBooks(0, 2);

    assertNotNull(savedBookList);
    assertEquals(bookList.getFirst(), savedBookList.content().getFirst());
    assertTrue(savedBookList.hasNextPage());
  }

  @Test
  @DisplayName("When book list is searched and nothing is found, should return correctly")
  void should_returnEquals_when_bookListIsSearchedAndNothingIsFound() {
    PageResponse<Book> savedBookList = bookService.findAllBooks(0, 5);

    assertFalse(savedBookList.hasNextPage());
  }

  @Test
  @DisplayName("When book is searched, should return correctly")
  void should_returnEquals_when_bookIsSearched() {
    Category category = createCategory(Optional.empty());
    Book book = createBook(Optional.empty(), Optional.of(category), Optional.empty());

    categoryRepository.save(category);
    bookRepository.save(book);

    Book savedBook = bookService.findBook(book.getBookId());

    assertNotNull(savedBook);
    assertEquals(book, savedBook);
  }

  @Test
  @DisplayName("When book is searched and is not found, should throw exception correctly")
  void should_throwException_when_bookIsSearchedAndIsNotFound() {
    UUID bookId = UUID.randomUUID();

    BookNotFoundException bookNotFoundException = assertThrows(
      BookNotFoundException.class,
      () -> bookService.findBook(bookId)
    );

    String expectedExceptionMessage = "Book not found. BookId: " + bookId;

    assertTrue(bookNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When book is updated and is not found, should throw exception")
  void should_throwException_when_bookIsUpdatedAndIsNotFound() {
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());

    Map<String, Optional<String>> bookInfo = new HashMap<>();
    bookInfo.put("title", Optional.empty());
    bookInfo.put("author", Optional.empty());
    bookInfo.put("isbn", Optional.empty());


    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, Optional.empty());

    BookNotFoundException bookNotFoundException = assertThrows(
      BookNotFoundException.class,
      () -> bookService.updateBook(book.getBookId(), bookUpdateDtoRequest)
    );

    String expectedExceptionMessage = "Book not found. BookId: " + book.getBookId();

    assertTrue(bookNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When book title is updated, should return correctly")
  void should_returnEquals_when_bookTitleIsUpdated() throws URISyntaxException {
    String newBookTitle = "New Book Title";
    Map<String, Optional<String>> bookInfo = new HashMap<>();
    bookInfo.put("title", Optional.of(newBookTitle));
    bookInfo.put("author", Optional.empty());
    bookInfo.put("isbn", Optional.empty());
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());

    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, Optional.empty());

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDtoRequest);

    assertNotNull(updatedBook);
    assertNotEquals(book, updatedBook);
    assertEquals(newBookTitle, updatedBook.getTitle());
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName("When book author is updated, should return correctly")
  void should_returnEquals_when_bookAuthorIsUpdated() throws URISyntaxException {
    String newBookAuthor = "New Book Author";
    Map<String, Optional<String>> bookInfo = new HashMap<>();
    bookInfo.put("title", Optional.empty());
    bookInfo.put("author", Optional.of(newBookAuthor));
    bookInfo.put("isbn", Optional.empty());
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());

    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, Optional.empty());

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDtoRequest);

    assertNotNull(updatedBook);
    assertNotEquals(book, updatedBook);
    assertEquals(newBookAuthor, updatedBook.getAuthor());
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName("When book isbn is updated, should return correctly")
  void should_returnEquals_when_bookIsbnIsUpdated() throws URISyntaxException {
    String newBookIsbn = "9876543210";
    Map<String, Optional<String>> bookInfo = new HashMap<>();
    bookInfo.put("title", Optional.empty());
    bookInfo.put("author", Optional.empty());
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());

    bookInfo.put("isbn", Optional.of(newBookIsbn));

    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, Optional.empty());

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDtoRequest);

    assertNotNull(updatedBook);
    assertNotEquals(book, updatedBook);
    assertEquals(newBookIsbn, updatedBook.getIsbn());
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName("When book category is updated, should return correctly")
  void should_returnEquals_when_bookCategoryIsUpdated() throws URISyntaxException {
    Map<String, Optional<String>> bookInfo = new HashMap<>();
    bookInfo.put("title", Optional.empty());
    bookInfo.put("author", Optional.empty());
    bookInfo.put("isbn", Optional.empty());
    Category newCategory = createCategory(Optional.of("New Category Name"));
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());

    categoryRepository.save(book.getCategory());
    categoryRepository.save(newCategory);
    bookRepository.save(book);

    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, Optional.of(newCategory.getCategoryId()));

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDtoRequest);

    assertNotNull(updatedBook);
    assertNotEquals(book, updatedBook);
    assertEquals(newCategory, updatedBook.getCategory());
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName("When book category is updated and is not found, should throw exception correctly")
  void should_throwException_when_bookCategoryIsNotFound() {
    Category newCategory = createCategory(Optional.of("New Category Name"));
    Map<String, Optional<String>> bookInfo = new HashMap<>();
    bookInfo.put("title", Optional.of("New Book Title"));
    bookInfo.put("author", Optional.of("New Book Author"));
    bookInfo.put("isbn", Optional.of("9876543210"));
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.empty());

    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, Optional.of(newCategory.getCategoryId()));

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    CategoryNotFoundException categoryNotFoundException = assertThrows(
      CategoryNotFoundException.class,
      () -> bookService.updateBook(book.getBookId(), bookUpdateDtoRequest)
    );

    String expectedExceptionMessage =
      "Category not found. CategoryId: " + newCategory.getCategoryId();

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When book is updated and is inactive, reactivate and update correctly")
  void should_updateAndReactive_whenBookIsInactiveAndUpdated() throws URISyntaxException {
    Map<String, Optional<String>> bookInfo = new HashMap<>();
    bookInfo.put("title", Optional.of("New Book Title"));
    bookInfo.put("author", Optional.of("New Book Author"));
    bookInfo.put("isbn", Optional.of("9876543210"));
    Book book = createBook(Optional.empty(), Optional.empty(), Optional.of(LocalDateTime.now()));

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    BookUpdateDtoRequest bookUpdateDtoRequest =
      createBookUpdateDtoRequest(bookInfo, Optional.empty());

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDtoRequest);

    assertNull(updatedBook.getInactivatedAt());
    assertNotNull(updatedBook.getUpdatedAt());
    assertNotEquals(book, updatedBook);
  }

  @Test
  @DisplayName("When book is inactivated, inactivate correctly")
  void should_inactivate_when_bookIsInactivated() {
    Category category = createCategory(Optional.empty());
    Book book = createBook(Optional.empty(), Optional.of(category), Optional.empty());

    categoryRepository.save(category);
    bookRepository.save(book);
    bookService.inactiveBook(book.getBookId());

    Book inactivatedBook = bookRepository.findById(book.getBookId()).get();

    assertNotNull(inactivatedBook);
    assertNotNull(inactivatedBook.getInactivatedAt());
    assertNotNull(inactivatedBook.getUpdatedAt());
    assertNotEquals(book, inactivatedBook);
  }

  @Test
  @DisplayName("When book is inactivated and is not found, throws exception")
  void should_throwException_when_bookIsInactivatedAndIsNotFound() {
    UUID bookId = UUID.randomUUID();

    BookNotFoundException bookNotFoundException = assertThrows(
      BookNotFoundException.class,
      () -> bookService.inactiveBook(bookId)
    );

    String expectedExceptionMessage = "Book not found. BookId: " + bookId;

    assertTrue(bookNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When book is inactivated and is not found, throws exception")
  void should_throwException_when_bookAlreadyIsInactivated() {
    Category category = createCategory(Optional.empty());
    Book book = createBook(Optional.empty(), Optional.of(category), Optional.empty());
    book.setInactivatedAt(LocalDateTime.now());
    UUID bookId = book.getBookId();

    categoryRepository.save(category);
    bookRepository.save(book);

    BookIsInactiveException bookNotFoundException = assertThrows(
      BookIsInactiveException.class,
      () -> bookService.inactiveBook(bookId)
    );

    String expectedExceptionMessage = "Book is inactive. BookId: " + bookId;

    assertTrue(bookNotFoundException.getMessage().contains(expectedExceptionMessage));
  }
}
