package com.example.bookstoreapijava.integration.services;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.book.services.BookService;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URISyntaxException;
import java.util.Optional;

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
  public void should_returnCorrectly_when_bookIsInserted() throws URISyntaxException {
    Book book = createBook(Optional.empty());
    BookCreatedVO bookCreatedMock = createBookCreatedVO(book);

    categoryRepository.save(book.getCategory());

    BookCreatedVO bookCreated = bookService.insertBook(book);

    assertNotNull(bookCreated);
    assertEquals(bookCreatedMock, bookCreated);
  }

  @Test
  @DisplayName(value = "When book is inserted and already exists, should throw exception correctly")
  public void should_throwException_when_bookIsInsertedAndAlreadyExists() {
    String isbn = "0123456788";
    Book book = createBook(Optional.of(isbn));
  }

}
