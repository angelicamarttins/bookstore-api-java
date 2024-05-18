package com.example.bookstoreapijava.main.exceptions;

import java.util.UUID;

public class BookIsInactiveException extends RuntimeException {

  public BookIsInactiveException(UUID bookId) {
    super("Book is inactive. BookId: " + bookId);
  }

}
