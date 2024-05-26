package com.example.bookstoreapijava.integration.services;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.BookAlreadyExistsException;
import com.example.bookstoreapijava.main.exceptions.BookNotFoundException;
import com.example.bookstoreapijava.main.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.repositories.BookRepository;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.services.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.bookstoreapijava.providers.BookCreatedVOProvider.createBookCreatedVO;
import static com.example.bookstoreapijava.providers.BookProvider.createBook;
import static com.example.bookstoreapijava.providers.BookProvider.createBookList;
import static com.example.bookstoreapijava.providers.BookUpdateDTORequestProvider.createBookUpdateDTORequest;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static org.junit.jupiter.api.Assertions.*;

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
  @DisplayName(value = "When book does not exists and is inserted, should returns correctly")
  void should_returnCorrectly_when_bookIsInsertedAndDoesNotExists() throws URISyntaxException {
    Book book = createBook(Optional.empty(), Optional.empty());
    BookCreatedVO bookCreatedMock = createBookCreatedVO(book);

    categoryRepository.save(book.getCategory());

    BookCreatedVO bookCreated = bookService.insertBook(book);

    assertNotNull(bookCreated);
    assertEquals(bookCreatedMock, bookCreated);
  }

  @Test
  @DisplayName(value = "When book is inserted, already exists and is active, should throw exception correctly")
  void should_throwException_when_bookIsInsertedIsActiveAndAlreadyExists() {
    Book book = createBook(Optional.empty(), Optional.empty());

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    BookAlreadyExistsException bookAlreadyExistsException = assertThrows(
        BookAlreadyExistsException.class,
        () -> bookService.insertBook(book)
    );

    String expectedExceptionMessage = "Book already exists with isbn " + book.getIsbn();

    assertTrue(bookAlreadyExistsException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName(value = "When book is inserted and category is not found, should throw exception correctly")
  void should_throwException_when_bookIsInsertedAndCategoryIsNotFound() {
    Book book = createBook(Optional.empty(), Optional.empty());

    CategoryNotFoundException categoryNotFoundException = assertThrows(
        CategoryNotFoundException.class,
        () -> bookService.insertBook(book)
    );

    String expectedExceptionMessage =
        "Category not found with id " + book.getCategory().getCategoryId();

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName(value = "When book is inserted, already exists and is inactivated, should reactivate it")
  void should_reactivateBook_when_bookIsInsertedAlreadyExistsAndIsInactivated() throws URISyntaxException {
    Category category = createCategory(Optional.empty());
    Book expectedBook = createBook(Optional.empty(), Optional.of(category));
    BookCreatedVO expectedBookCreated = createBookCreatedVO(expectedBook);
    LocalDateTime insertDate = LocalDateTime.now();

    expectedBook.setInactivatedAt(insertDate);
    expectedBook.setUpdatedAt(insertDate);

    categoryRepository.save(category);
    bookRepository.save(expectedBook);

    BookCreatedVO actualBookCreated = bookService.insertBook(expectedBook);

    assertEquals(expectedBookCreated.book().getBookId(), actualBookCreated.book().getBookId());
    assertEquals(expectedBookCreated.book().getIsbn(), actualBookCreated.book().getIsbn());
    assertNotNull(actualBookCreated.book().getUpdatedAt());
    assertNull(actualBookCreated.book().getInactivatedAt());
  }

  @Test
  @DisplayName(value = "When book list is searched, should return correctly")
  void should_returnEquals_when_bookListIsSearched() {
    Category category = createCategory(Optional.of("Book List Test"));
    List<Book> bookList = createBookList(Optional.of(category));

    categoryRepository.save(category);
    bookRepository.saveAll(bookList);

    Page<Book> savedBookList = bookService.findAllBooks(0, 10);

    assertNotNull(savedBookList);
    assertEquals(bookList, savedBookList);
  }

  @Test
  @DisplayName(value = "When book is searched, should return correctly")
  void should_returnEquals_when_bookIsSearched() {
    Category category = createCategory(Optional.empty());
    Book book = createBook(Optional.empty(), Optional.of(category));

    categoryRepository.save(category);
    bookRepository.save(book);

    Book savedBook = bookService.findBook(book.getBookId());

    assertNotNull(savedBook);
    assertEquals(book, savedBook);
  }

  @Test
  @DisplayName(value = "When book is searched and is not found, should throw exception correctly")
  void should_throwException_when_bookIsSearchedAndIsNotFound() {
    UUID bookId = UUID.randomUUID();

    BookNotFoundException bookNotFoundException = assertThrows(
        BookNotFoundException.class,
        () -> bookService.findBook(bookId)
    );

    String expectedExceptionMessage = "Book not found with id " + bookId;

    assertTrue(bookNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName(value = "When book is updated and is not found, should throw exception")
  void should_throwException_when_bookIsUpdatedAndIsNotFound() {
    Book book = createBook(Optional.empty(), Optional.empty());

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.empty());
      put("author", Optional.empty());
      put("isbn", Optional.empty());
    }};

    BookUpdateDTORequest bookUpdateDTORequest = createBookUpdateDTORequest(bookInfo, Optional.empty());

    BookNotFoundException bookNotFoundException = assertThrows(
        BookNotFoundException.class,
        () -> bookService.updateBook(book.getBookId(), bookUpdateDTORequest)
    );

    String expectedExceptionMessage = "Book not found with id " + book.getBookId();

    assertTrue(bookNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName(value = "When book title is updated, should return correctly")
  void should_returnEquals_when_bookTitleIsUpdated() throws URISyntaxException {
    Book book = createBook(Optional.empty(), Optional.empty());

    String newBookTitle = "New Book Title";

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.of(newBookTitle));
      put("author", Optional.empty());
      put("isbn", Optional.empty());
    }};

    BookUpdateDTORequest bookUpdateDTORequest = createBookUpdateDTORequest(bookInfo, Optional.empty());

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDTORequest);

    assertNotNull(updatedBook);
    assertNotEquals(book, updatedBook);
    assertEquals(newBookTitle, updatedBook.getTitle());
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName(value = "When book author is updated, should return correctly")
  void should_returnEquals_when_bookAuthorIsUpdated() throws URISyntaxException {
    Book book = createBook(Optional.empty(), Optional.empty());

    String newBookAuthor = "New Book Author";

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.empty());
      put("author", Optional.of(newBookAuthor));
      put("isbn", Optional.empty());
    }};

    BookUpdateDTORequest bookUpdateDTORequest = createBookUpdateDTORequest(bookInfo, Optional.empty());

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDTORequest);

    assertNotNull(updatedBook);
    assertNotEquals(book, updatedBook);
    assertEquals(newBookAuthor, updatedBook.getAuthor());
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName(value = "When book isbn is updated, should return correctly")
  void should_returnEquals_when_bookIsbnIsUpdated() throws URISyntaxException {
    Book book = createBook(Optional.empty(), Optional.empty());

    String newBookIsbn = "9876543210";

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.empty());
      put("author", Optional.empty());
      put("isbn", Optional.of(newBookIsbn));
    }};

    BookUpdateDTORequest bookUpdateDTORequest = createBookUpdateDTORequest(bookInfo, Optional.empty());

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDTORequest);

    assertNotNull(updatedBook);
    assertNotEquals(book, updatedBook);
    assertEquals(newBookIsbn, updatedBook.getIsbn());
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName(value = "When book category is updated, should return correctly")
  void should_returnEquals_when_bookCategoryIsUpdated() throws URISyntaxException {
    Book book = createBook(Optional.empty(), Optional.empty());

    Category newCategory = createCategory(Optional.of("New Category Name"));

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.empty());
      put("author", Optional.empty());
      put("isbn", Optional.empty());
    }};

    BookUpdateDTORequest bookUpdateDTORequest =
        createBookUpdateDTORequest(bookInfo, Optional.of(newCategory.getCategoryId()));

    categoryRepository.save(book.getCategory());
    categoryRepository.save(newCategory);
    bookRepository.save(book);

    Book updatedBook = bookService.updateBook(book.getBookId(), bookUpdateDTORequest);

    assertNotNull(updatedBook);
    assertNotEquals(book, updatedBook);
    assertEquals(newCategory, updatedBook.getCategory());
    assertNotNull(updatedBook.getUpdatedAt());
  }

  @Test
  @DisplayName(value = "When book category is updated and is not found, should throw exception correctly")
  void should_throwException_when_bookCategoryIsNotFound() {
    Book book = createBook(Optional.empty(), Optional.empty());

    Category newCategory = createCategory(Optional.of("New Category Name"));

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.of("New Book Title"));
      put("author", Optional.of("New Book Author"));
      put("isbn", Optional.of("9876543210"));
    }};

    BookUpdateDTORequest bookUpdateDTORequest =
        createBookUpdateDTORequest(bookInfo, Optional.of(newCategory.getCategoryId()));

    categoryRepository.save(book.getCategory());
    bookRepository.save(book);

    CategoryNotFoundException categoryNotFoundException = assertThrows(
        CategoryNotFoundException.class,
        () -> bookService.updateBook(book.getBookId(), bookUpdateDTORequest)
    );

    String expectedExceptionMessage =
        "Category not found with id " + newCategory.getCategoryId();

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName(value = "When book is deleted, should soft delete correctly")
  void should_softDelete_when_bookIsDeleted() {
    Category category = createCategory(Optional.empty());
    Book book = createBook(Optional.empty(), Optional.of(category));

    categoryRepository.save(category);
    bookRepository.save(book);

    bookService.inactiveBook(book.getBookId());

    Book deletedBook = bookRepository.findById(book.getBookId()).get();

    assertNotNull(deletedBook);
    assertNotNull(deletedBook.getInactivatedAt());
    assertNotNull(deletedBook.getUpdatedAt());
    assertNotEquals(book, deletedBook);
  }

  @Test
  @DisplayName(value = "When book is deleted and is not found, should throw exception")
  void should_throwException_when_bookIsDeletedAndIsNotFound() {
    UUID bookId = UUID.randomUUID();

    BookNotFoundException bookNotFoundException = assertThrows(
        BookNotFoundException.class,
        () -> bookService.inactiveBook(bookId)
    );

    String expectedExceptionMessage = "Book not found with id " + bookId;

    assertTrue(bookNotFoundException.getMessage().contains(expectedExceptionMessage));
  }
}
