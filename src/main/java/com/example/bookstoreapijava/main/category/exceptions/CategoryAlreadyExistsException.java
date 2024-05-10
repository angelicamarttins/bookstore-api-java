package com.example.bookstoreapijava.main.category.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {

  public CategoryAlreadyExistsException(String categoryName) {
    super("Category already exists with name " + categoryName);
  }

}
