package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.category.entities.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryProvider {

  public static Category createCategory() {
    return new Category(
        UUID.randomUUID(),
        UUID.randomUUID().toString().replace("-", ""),
        LocalDateTime.now(),
        null,
        null
    );
  }

  public static List<Category> createCategoryList() {
    List<Category> categoryList = new ArrayList<>();

    for (int i = 0; i <= 5; i++) {
      categoryList.add(createCategory());
    }

    return categoryList;
  }

}