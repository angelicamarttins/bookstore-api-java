package com.example.bookstoreapijava.providers;

import static com.example.bookstoreapijava.utils.TestUtils.localDateTimeFormat;

import com.example.bookstoreapijava.main.entities.Category;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CategoryProvider {

  public static Category createCategory(
    String categoryName,
    LocalDateTime updatedAt,
    LocalDateTime inactivatedAt
  ) {
    String randomString = UUID.randomUUID().toString().replace("-", "");

    return new Category(
      UUID.randomUUID(),
      Objects.requireNonNullElse(categoryName, randomString),
      Objects.requireNonNullElse(categoryName, randomString),
      localDateTimeFormat(LocalDateTime.now()),
      localDateTimeFormat(updatedAt),
      localDateTimeFormat(inactivatedAt)
    );
  }

  public static List<Category> createCategoryList(int howMany) {
    List<Category> categoryList = new ArrayList<>();

    for (int i = 0; i < howMany; i++) {
      categoryList.add(createCategory(null, null, null));
    }

    return categoryList;
  }

}
