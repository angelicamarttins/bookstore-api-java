package com.example.bookstoreapijava.integration.services;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.exceptions.BookAlreadyExistsException;
import com.example.bookstoreapijava.main.book.exceptions.BookNotFoundException;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.book.services.BookService;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.bookstoreapijava.providers.BookProvider.createBook;
import static com.example.bookstoreapijava.providers.BookProvider.createBookList;
import static com.example.bookstoreapijava.providers.BookCreatedVOProvider.createBookCreatedVO;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.providers.BookUpdateDTORequestProvider.createBookUpdateDTORequest;
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
  @DisplayName(value = "When book is inserted, should returns correctly")
  void should_returnCorrectly_when_bookIsInserted() throws URISyntaxException {
    Book book = createBook(Optional.empty(), Optional.empty());
    BookCreatedVO bookCreatedMock = createBookCreatedVO(book);

    categoryRepository.save(book.getCategory());

    BookCreatedVO bookCreated = bookService.insertBook(book);

    assertNotNull(bookCreated);
    assertEquals(bookCreatedMock, bookCreated);
  }

  @Test
  @DisplayName(value = "When book is inserted and already exists, should throw exception correctly")
  void should_throwException_when_bookIsInsertedAndAlreadyExists() {
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
  @DisplayName(value = "When book list is searched, should return correctly")
  void should_returnEquals_when_bookListIsSearched() {
    Category category = createCategory(Optional.of("Book List Test"));
    List<Book> bookList = createBookList(Optional.of(category));

    categoryRepository.save(category);
    bookRepository.saveAll(bookList);

    List<Book> savedBookList = bookService.findAllBooks();

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
  void should_returnEquals_when_bookTitleIsUpdated() {
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
  void should_returnEquals_when_bookAuthorIsUpdated() {
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
  void should_returnEquals_when_bookIsbnIsUpdated() {
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
  void should_returnEquals_when_bookCategoryIsUpdated() {
    Book book = createBook(Optional.empty(), Optional.empty());

    Category newCategory = createCategory(Optional.of("New Category Name"));

    Map<String, Optional<String>> bookInfo = new HashMap<>() {{
      put("title", Optional.empty());
      put("author", Optional.empty());
      put("isbn", Optional.empty());
    }};

    BookUpdateDTORequest bookUpdateDTORequest =
        createBookUpdateDTORequest(bookInfo, Optional.of(newCategory));

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
        createBookUpdateDTORequest(bookInfo, Optional.of(newCategory));

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

    bookService.deleteBook(book.getBookId());

    Book deletedBook = bookRepository.findById(book.getBookId()).get();

    assertNotNull(deletedBook);
    assertNotNull(deletedBook.getInactivatedAt());
    assertNotNull(deletedBook.getUpdatedAt());
    assertNotEquals(book, deletedBook);
  }
}
