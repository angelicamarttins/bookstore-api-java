package com.example.bookstoreapijava.integration.category;

import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class CategoryServiceIntegrationTest {

  @Container
  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:alpine")
          .withDatabaseName("postgres")
          .withUsername("postgres")
          .withPassword("secret");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
  }

  @BeforeAll
  static void beforeAll() {
    postgresContainer.start();
  }

  @AfterAll
  static void afterAll() {
    postgresContainer.stop();
  }

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
