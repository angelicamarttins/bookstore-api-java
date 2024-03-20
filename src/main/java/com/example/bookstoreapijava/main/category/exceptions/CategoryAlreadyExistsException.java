package com.example.bookstoreapijava.main.category.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {

  public CategoryAlreadyExistsException(String message) {
    super("Category already exists with name " + message);
  }

}
