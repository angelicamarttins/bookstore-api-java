package com.example.bookstoreapijava.integration.services;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.exceptions.BookAlreadyExistsException;
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

import static com.example.bookstoreapijava.providers.BookProvider.createBook;
import static com.example.bookstoreapijava.providers.BookCreatedVOProvider.createBookCreatedVO;
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
    Book book = createBook();
    BookCreatedVO bookCreatedMock = createBookCreatedVO(book);

    categoryRepository.save(book.getCategory());

    BookCreatedVO bookCreated = bookService.insertBook(book);

    assertNotNull(bookCreated);
    assertEquals(bookCreatedMock, bookCreated);
  }

  @Test
  @DisplayName(value = "When book is inserted and already exists, should throw exception correctly")
  void should_throwException_when_bookIsInsertedAndAlreadyExists() {
    Book book = createBook();

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
    Book book = createBook();

    CategoryNotFoundException categoryNotFoundException = assertThrows(
      CategoryNotFoundException.class,
        () -> bookService.insertBook(book)
    );

    String expectedExceptionMessage =
        "Category not found with id " + book.getCategory().getCategoryId();

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

}
