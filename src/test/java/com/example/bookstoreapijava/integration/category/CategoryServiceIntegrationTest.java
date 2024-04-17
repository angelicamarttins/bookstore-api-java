package com.example.bookstoreapijava.integration.category;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.services.CategoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;

import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;

public class CategoryServiceIntegrationTest extends PostgresTestContainersBase {

  @Autowired
  CategoryService categoryService;

  @Test
  @DisplayName(value = "Should return correctly, when category is inserted")
  public void should_returnEquals_when_categoryIsInsertedCorrectly() throws URISyntaxException {
    Category category = createCategory();

    Category savedCategory = categoryService.insertCategory(category).category();

    assertNotNull(savedCategory);
    assertEquals(category, savedCategory);
  }

}
