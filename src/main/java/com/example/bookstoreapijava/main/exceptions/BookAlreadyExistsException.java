package com.example.bookstoreapijava.main.exceptions;

public class BookAlreadyExistsException extends RuntimeException {

  public BookAlreadyExistsException(String message) {
    super("Book already exists with isbn " + message);
  }

}
