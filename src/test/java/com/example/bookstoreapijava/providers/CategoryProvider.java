package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.category.entities.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.bookstoreapijava.utils.TestUtils.localDateTimeFormat;

public class CategoryProvider {

  public static Category createCategory(Optional<String> categoryName) {
    return new Category(
        UUID.randomUUID(),
        categoryName.orElse(UUID.randomUUID().toString().replace("-", "")),
        localDateTimeFormat(LocalDateTime.now()),
        null,
        null
    );
  }

  public static List<Category> createCategoryList() {
    List<Category> categoryList = new ArrayList<>();

    for (int i = 0; i <= 5; i++) {
      categoryList.add(createCategory(Optional.empty()));
    }

    return categoryList;
  }

}