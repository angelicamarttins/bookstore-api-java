package com.example.bookstoreapijava.main.book.exceptions;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(String message) {
    super("Book not found with id " + message);
  }

}
