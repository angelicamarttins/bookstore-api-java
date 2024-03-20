package com.example.bookstoreapijava.main.category.exceptions;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException(String message) {
    super("Category not found with id " + message);
  }

}
