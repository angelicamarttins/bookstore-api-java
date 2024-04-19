package com.example.bookstoreapijava.integration.services;

import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.book.services.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URISyntaxException;

import static com.example.bookstoreapijava.providers.BookProvider.createBook;
import static com.example.bookstoreapijava.providers.BookCreatedVOProvider.createBookCreatedVO;
import static org.junit.jupiter.api.Assertions.*;

public class BookServiceIntegrationTest {

  @Autowired
  BookRepository bookRepository;

  @Autowired
  BookService bookService;

  @AfterEach
  public void cleanUpDb() {
    bookRepository.deleteAll();
  }

  @Test
  @DisplayName(value = "When book is inserted, should returns correctly")
  public void should_returnCorrectly_when_bookIsInserted() throws URISyntaxException {
    Book book = createBook();
    BookCreatedVO bookCreatedMock = createBookCreatedVO(book);

    BookCreatedVO bookCreated = bookService.insertBook(book);

    assertNotNull(bookCreated);
    assertEquals(bookCreatedMock, bookCreated);
  }

}
