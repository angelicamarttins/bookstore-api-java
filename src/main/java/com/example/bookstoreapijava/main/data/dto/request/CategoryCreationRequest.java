package com.example.bookstoreapijava.main.data.dto.request;

import com.example.bookstoreapijava.main.entities.Category;

public record CategoryCreationRequest(String categoryName) {

  public static Category toDomain(String categoryName) {
    return new Category(categoryName);
  }

}
