package com.example.bookstoreapijava.main.exceptions;

import java.util.UUID;

public class CategoryIsInactiveException extends RuntimeException {

  public CategoryIsInactiveException(UUID categoryId) {
    super("Category is inactive. CategoryId: " + categoryId);
  }

}
