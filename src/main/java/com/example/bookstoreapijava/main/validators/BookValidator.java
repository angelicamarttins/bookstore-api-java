package com.example.bookstoreapijava.main.validators;

import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.exceptions.BookAlreadyExistsException;
import com.example.bookstoreapijava.main.exceptions.BookIsInactiveException;
import com.example.bookstoreapijava.main.exceptions.BookNotFoundException;
import com.example.bookstoreapijava.main.repositories.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class BookValidator {

  private final BookRepository bookRepository;

  public void checkIfBookAlreadyExists(Book savedBook, String bookIsbn) {
    if (savedBook.getInactivatedAt() == null) {
      log.info("Book already exists. Aborting... BookIsbn: {}, BookId: {}",
          savedBook.getIsbn(),
          savedBook.getBookId());

      throw new BookAlreadyExistsException(bookIsbn);
    }
  }

  public Book checkIfBookIsFound(UUID bookId) {
    return bookRepository
        .findById(bookId)
        .orElseThrow(() -> {
          log.info("Book not found. Aborting... BookId: {}", bookId);

          return new BookNotFoundException(bookId);
        });
  }

  public void checkIfBookIsActive(Book book) {
    UUID bookId = book.getBookId();

    if (book.getInactivatedAt() != null) {
      log.info("Book is inactive. BookId: {}", bookId);

      throw new BookIsInactiveException(bookId);
    }
  }

}
