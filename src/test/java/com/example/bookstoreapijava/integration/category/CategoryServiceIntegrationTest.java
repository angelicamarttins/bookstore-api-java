package com.example.bookstoreapijava.integration.category;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

public class CategoryServiceIntegrationTest extends PostgresTestContainersBase {

  @Autowired
  CategoryRepository categoryRepository;

  @Test
  @DisplayName(value = "Category")
  public void test() {
    UUID categoryId = UUID.randomUUID();
    Category category =
        new Category(categoryId, "test", LocalDateTime.now(), null, null);

    categoryRepository.save(category);
    Category savedCategory = categoryRepository.findById(category.getCategoryId()).orElse(null);

    Assertions.assertEquals(category, savedCategory);
  }

}
