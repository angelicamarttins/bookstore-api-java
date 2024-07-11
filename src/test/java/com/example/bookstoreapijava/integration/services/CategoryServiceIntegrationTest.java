package com.example.bookstoreapijava.integration.services;

import static com.example.bookstoreapijava.providers.CategoryCreatedVoProvider.createCategoryCreatedVo;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategoryList;
import static com.example.bookstoreapijava.providers.CategoryUpdateDtoProvider.createCategoryUpdateDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapijava.config.TestContainersBase;
import com.example.bookstoreapijava.main.data.dto.request.CategoryUpdateDtoRequest;
import com.example.bookstoreapijava.main.data.dto.response.PageResponse;
import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVo;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.CategoryAlreadyExistsException;
import com.example.bookstoreapijava.main.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.services.CategoryService;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceIntegrationTest extends TestContainersBase {

  @Autowired
  CategoryService categoryService;

  @Autowired
  CategoryRepository categoryRepository;

  @AfterEach
  public void cleanUpDb() {
    categoryRepository.deleteAll();
  }

  @Test
  @DisplayName("When category is inserted, should return correctly")
  void categoryIsInsertedCorrectly() throws URISyntaxException {
    Category category = createCategory(null, null, null);
    CategoryCreatedVo categoryCreatedVo = createCategoryCreatedVo(category);

    CategoryCreatedVo savedCategoryCreatedVo = categoryService.insertCategory(category);

    assertNotNull(savedCategoryCreatedVo);
    assertEquals(categoryCreatedVo, savedCategoryCreatedVo);
    assertEquals(category, savedCategoryCreatedVo.category());
  }

  @Test
  @DisplayName("When a category already exists and try to insert again, "
    + "should throw exception correctly")
  void categoryAlreadyExistsAndIsInsertAgain() {
    Category firstCategory = createCategory("Test", null, null);
    Category secondCategory = createCategory("Test", null, null);

    categoryRepository.save(firstCategory);

    CategoryAlreadyExistsException categoryAlreadyExistsException = assertThrows(
      CategoryAlreadyExistsException.class,
      () -> categoryService.insertCategory(secondCategory)
    );

    String expectedExceptionMessage =
      "Category already exists. CategoryName: " + secondCategory.getCategoryName();

    assertTrue(categoryAlreadyExistsException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When a category list is searched, should return correctly")
  void categoryListIsSearched() {
    List<Category> categoryList = createCategoryList(5);

    categoryRepository.saveAll(categoryList);

    PageResponse<Category> savedCategoryList = categoryService.findAllCategories(0, 10);

    assertNotNull(savedCategoryList);
    assertEquals(categoryList, savedCategoryList.content());
  }

  @Test
  @DisplayName("When a category is searched, should return correctly")
  void categoryIsSearched() {
    Category category = createCategory(null, null, null);

    categoryRepository.save(category);

    Category savedCategory = categoryService.findCategory(category.getCategoryId());

    assertNotNull(savedCategory);
    assertEquals(category, savedCategory);
  }

  @Test
  @DisplayName("When category is searched and is not found, should throw exception correctly")
  void categoryIsSearchedAndIsNotFound() {
    UUID categoryId = UUID.randomUUID();

    CategoryNotFoundException categoryNotFoundException = assertThrows(
      CategoryNotFoundException.class,
      () -> categoryService.findCategory(categoryId)
    );

    String expectedExceptionMessage = "Category not found. CategoryId: " + categoryId;

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When a category is updated, should return correctly")
  void categoryIsUpdated() {
    Category category = createCategory(null, null, null);
    CategoryUpdateDtoRequest categoryUpdateDtoRequest = createCategoryUpdateDto();

    categoryRepository.save(category);

    Category savedCategory =
      categoryService.updateCategory(categoryUpdateDtoRequest, category.getCategoryId());

    assertNotNull(savedCategory);
    assertNotNull(savedCategory.getUpdatedAt());
    assertEquals(categoryUpdateDtoRequest.categoryName(), savedCategory.getCategoryName());
    assertNotEquals(category, savedCategory);
  }

  @Test
  @DisplayName("When category is updated and is not found, should throw exception correctly")
  void categoryIsUpdatedAndIsNotFound() {
    UUID categoryId = UUID.randomUUID();
    CategoryUpdateDtoRequest categoryUpdateDtoRequest = createCategoryUpdateDto();
    String expectedExceptionMessage = "Category not found. CategoryId: " + categoryId;

    CategoryNotFoundException categoryNotFoundException = assertThrows(
      CategoryNotFoundException.class,
      () -> categoryService.updateCategory(categoryUpdateDtoRequest, categoryId)
    );

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName("When a category is deleted, should soft delete correctly")
  void categoryIsInactivated() {
    Category category = createCategory(null, null, null);
    UUID categoryId = category.getCategoryId();

    categoryRepository.save(category);

    categoryService.inactiveCategory(categoryId);

    Category deletedCategory = categoryRepository.findById(categoryId).get();

    assertNotNull(deletedCategory);
    assertNotNull(deletedCategory.getUpdatedAt());
    assertNotNull(deletedCategory.getInactivatedAt());
    assertNotEquals(category, deletedCategory);
  }

  @Test
  @DisplayName("When category is deleted and is not found, should throw exception correctly")
  void categoryIsInactivatedAndIsNotFound() {
    UUID categoryId = UUID.randomUUID();
    String expectedExceptionMessage = "Category not found. CategoryId: " + categoryId;

    CategoryNotFoundException categoryNotFoundException = assertThrows(
      CategoryNotFoundException.class,
      () -> categoryService.inactiveCategory(categoryId)
    );

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

}
