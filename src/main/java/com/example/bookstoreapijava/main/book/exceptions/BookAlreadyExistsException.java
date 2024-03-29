package com.example.bookstoreapijava.main.book.exceptions;

public class BookAlreadyExistsException extends RuntimeException {

  public BookAlreadyExistsException(String message) {
    super("Book already exists with isbn " + message);
  }

}
