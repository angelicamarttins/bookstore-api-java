package com.example.bookstoreapijava.main.exceptions;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(UUID message) {
    super("Book not found. BookId: " + message);
  }

}
