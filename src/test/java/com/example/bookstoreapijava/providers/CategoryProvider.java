package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.category.entities.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public class CategoryProvider {
  public static Category createCategory() {
    return new Category(
        UUID.randomUUID(),
        "TestCategory",
        LocalDateTime.now(),
        null,
        null
    );
  }
}