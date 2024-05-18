package com.example.bookstoreapijava.main.exceptions;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException(UUID categoryId) {
    super("Category not found with id " + categoryId);
  }

}
