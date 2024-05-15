package com.example.bookstoreapijava.main.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {

  public CategoryAlreadyExistsException(String categoryName) {
    super("Category already exists with name " + categoryName);
  }

}
