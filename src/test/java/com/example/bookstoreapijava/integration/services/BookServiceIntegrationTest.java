package com.example.bookstoreapijava.integration.services;

import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.bookstoreapijava.providers.BookProvider.createBook;

public class BookServiceIntegrationTest {

  @Autowired
  BookRepository bookRepository;

  @AfterEach
  public void cleanUpDb() {
    bookRepository.deleteAll();
  }

  @Test
  @DisplayName(value = "When book is inserted, should returns correctly")
  public void should_returnCorrectly_when_bookIsInserted() {
    Book book = createBook();
  }

}
