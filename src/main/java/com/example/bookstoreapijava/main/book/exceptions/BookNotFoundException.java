package com.example.bookstoreapijava.main.book.exceptions;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(UUID message) {
    super("Book not found with id " + message.toString());
  }

}
