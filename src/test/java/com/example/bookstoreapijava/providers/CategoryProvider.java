package com.example.bookstoreapijava.providers;

import static com.example.bookstoreapijava.utils.TestUtils.localDateTimeFormat;

import com.example.bookstoreapijava.main.entities.Category;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryProvider {

  public static Category createCategory(Optional<String> categoryName) {
    return new Category(
      UUID.randomUUID(),
      categoryName.orElse(UUID.randomUUID().toString().replace("-", "")),
      categoryName.orElse(UUID.randomUUID().toString().replace("-", "")).toUpperCase(),
      localDateTimeFormat(LocalDateTime.now()),
      null,
      null
    );
  }

  public static Category createInactiveCategory(Optional<String> categoryName) {
    return new Category(
      UUID.randomUUID(),
      categoryName.orElse(UUID.randomUUID().toString().replace("-", "")),
      categoryName.orElse(UUID.randomUUID().toString().replace("-", "")).toUpperCase(),
      localDateTimeFormat(LocalDateTime.now()),
      null,
      localDateTimeFormat(LocalDateTime.now())
    );
  }

  public static List<Category> createCategoryList(int howMany) {
    List<Category> categoryList = new ArrayList<>();

    for (int i = 0; i < howMany; i++) {
      categoryList.add(createCategory(Optional.empty()));
    }

    return categoryList;
  }

}
