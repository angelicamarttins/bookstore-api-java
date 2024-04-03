package com.example.bookstoreapijava.integration.category;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.category.services.CategoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.UUID;

public class CategoryServiceIntegrationTest extends PostgresTestContainersBase {

  @Autowired
  CategoryService categoryService;

  @Test
  @DisplayName(value = "Category")
  public void test() throws URISyntaxException {
    UUID categoryId = UUID.randomUUID();
    Category category =
        new Category(categoryId, "TestCategory");

    CategoryCreatedVO savedCategory = categoryService.insertCategory(category);

    assertNotNull(savedCategory);
    assertEquals(category, savedCategory);
  }

}
