package com.example.bookstoreapijava.main.category.exceptions;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException(UUID message) {
    super("Category not found with id " + message.toString());
  }

}
