package com.example.bookstoreapijava.integration.category;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.category.services.CategoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategoryList;
import static com.example.bookstoreapijava.providers.CategoryUpdateDTOProvider.createCategoryUpdateDTO;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceIntegrationTest extends PostgresTestContainersBase {

  @Autowired
  CategoryService categoryService;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  BookRepository bookRepository;

  @AfterEach
  public void cleanUpDb() {
    bookRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  @DisplayName(value = "When category is inserted, should return correctly")
  public void should_returnEquals_when_categoryIsInsertedCorrectly() throws URISyntaxException {
    Category category = createCategory();

    Category savedCategory = categoryService.insertCategory(category).category();

    assertNotNull(savedCategory);
    assertEquals(category, savedCategory);
  }

  @Test
  @DisplayName(value = "When a category list is searched, should return correctly")
  public void should_returnEquals_when_categoryListIsSearched() {
    List<Category> categoryList = createCategoryList();

    categoryRepository.saveAll(categoryList);

    List<Category> savedCategoryList = categoryService.findAllCategories();

    assertNotNull(savedCategoryList);
    assertEquals(categoryList, savedCategoryList);
  }

  @Test
  @DisplayName(value = "When a category is searched, should return correctly")
  public void should_returnEquals_when_categoryIsSearched() {
    Category category = createCategory();

    categoryRepository.save(category);

    Category savedCategory = categoryService.findCategory(category.getCategoryId());

    assertNotNull(savedCategory);
    assertEquals(category, savedCategory);
  }

  @Test
  @DisplayName(value = "When a category is updated, should return correctly")
  public void should_returnEquals_when_categoryIsUpdated() {
    Category category = createCategory();
    CategoryUpdateDTO categoryUpdateDTO = createCategoryUpdateDTO();

    categoryRepository.save(category);

    Category savedCategory = categoryService.updateCategory(categoryUpdateDTO, category.getCategoryId());

    assertNotNull(savedCategory);
    assertNotEquals(category, savedCategory);
    assertEquals(categoryUpdateDTO.categoryName(), savedCategory.getCategoryName());
  }

  @Test
  @DisplayName(value = "When a category is deleted, should soft delete correctly")
  public void should_deleteCorrectly_when_categoryIsSoftDeleted() {
    Category category = createCategory();
    UUID categoryId = category.getCategoryId();

    categoryRepository.save(category);

    categoryService.deleteCategory(categoryId);

    Optional<Category> deletedCategory = categoryRepository.findById(categoryId);

    LocalDateTime updatedAtDeletedCategory = deletedCategory.get().getUpdatedAt();
    LocalDateTime inactivatedAtDeletedCategory = deletedCategory.get().getInactivatedAt();

    assertNotNull(deletedCategory);
    assertNotNull(updatedAtDeletedCategory);
    assertNotNull(inactivatedAtDeletedCategory);
    assertNotEquals(category, deletedCategory);
  }

}