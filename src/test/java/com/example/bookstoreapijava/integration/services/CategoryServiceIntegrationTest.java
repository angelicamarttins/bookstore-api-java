package com.example.bookstoreapijava.integration.services;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.CategoryAlreadyExistsException;
import com.example.bookstoreapijava.main.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.repositories.BookRepository;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.services.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.bookstoreapijava.providers.CategoryCreatedVOProvider.createCategoryCreatedVO;
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
    categoryRepository.deleteAll();
  }

  @Test
  @DisplayName(value = "When category is inserted, should return correctly")
  void should_returnEquals_when_categoryIsInsertedCorrectly() throws URISyntaxException {
    Category category = createCategory(Optional.empty());
    CategoryCreatedVO categoryCreatedVO = createCategoryCreatedVO(category);

    CategoryCreatedVO savedCategoryCreatedVO = categoryService.insertCategory(category);

    assertNotNull(savedCategoryCreatedVO);
    assertEquals(categoryCreatedVO, savedCategoryCreatedVO);
    assertEquals(category, savedCategoryCreatedVO.category());
  }

  @Test
  @DisplayName(value = "When a category already exists and try to insert again, should throw exception correctly")
  void should_throwException_when_categoryAlreadyExistsAndIsInsertAgain() {
    Category firstCategory = createCategory(Optional.of("Test"));
    Category secondCategory = createCategory(Optional.of("Test"));

    categoryRepository.save(firstCategory);

    CategoryAlreadyExistsException categoryAlreadyExistsException = assertThrows(
        CategoryAlreadyExistsException.class,
        () -> categoryService.insertCategory(secondCategory)
    );

    String expectedExceptionMessage =
        "Category already exists with name " + secondCategory.getCategoryName();

    assertTrue(categoryAlreadyExistsException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName(value = "When a category list is searched, should return correctly")
  void should_returnEquals_when_categoryListIsSearched() {
    List<Category> categoryList = createCategoryList();

    categoryRepository.saveAll(categoryList);

    Page<Category> savedCategoryList = categoryService.findAllCategories(0, 10);

    assertNotNull(savedCategoryList);
    assertEquals(categoryList, savedCategoryList);
  }

  @Test
  @DisplayName(value = "When a category is searched, should return correctly")
  void should_returnEquals_when_categoryIsSearched() {
    Category category = createCategory(Optional.empty());

    categoryRepository.save(category);

    Category savedCategory = categoryService.findCategory(category.getCategoryId());

    assertNotNull(savedCategory);
    assertEquals(category, savedCategory);
  }

  @Test
  @DisplayName(value = "When category is searched and is not found, should throw exception correctly")
  void should_throwException_when_isSearchedAndCategoryIsNotFound() {
    UUID categoryId = UUID.randomUUID();

    CategoryNotFoundException categoryNotFoundException = assertThrows(
        CategoryNotFoundException.class,
        () -> categoryService.findCategory(categoryId)
    );

    String expectedExceptionMessage = "Category not found with id " + categoryId;

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName(value = "When a category is updated, should return correctly")
  void should_returnEquals_when_categoryIsUpdated() {
    Category category = createCategory(Optional.empty());
    CategoryUpdateDTO categoryUpdateDTO = createCategoryUpdateDTO();

    categoryRepository.save(category);

    Category savedCategory = categoryService.updateCategory(categoryUpdateDTO, category.getCategoryId());

    assertNotNull(savedCategory);
    assertNotNull(savedCategory.getUpdatedAt());
    assertEquals(categoryUpdateDTO.categoryName(), savedCategory.getCategoryName());
    assertNotEquals(category, savedCategory);
  }

  @Test
  @DisplayName(value = "When category is updated and is not found, should throw exception correctly")
  void should_throwException_when_isUpdatedAndCategoryIsNotFound() {
    UUID categoryId = UUID.randomUUID();
    CategoryUpdateDTO categoryUpdateDTO = createCategoryUpdateDTO();

    CategoryNotFoundException categoryNotFoundException = assertThrows(
        CategoryNotFoundException.class,
        () -> categoryService.updateCategory(categoryUpdateDTO, categoryId)
    );

    String expectedExceptionMessage = "Category not found with id " + categoryId;

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

  @Test
  @DisplayName(value = "When a category is deleted, should soft delete correctly")
  void should_deleteCorrectly_when_categoryIsSoftDeleted() {
    Category category = createCategory(Optional.empty());
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
  @DisplayName(value = "When category is deleted and is not found, should throw exception correctly")
  void should_throwException_when_isDeletedAndCategoryIsNotFound() {
    UUID categoryId = UUID.randomUUID();

    CategoryNotFoundException categoryNotFoundException = assertThrows(
        CategoryNotFoundException.class,
        () -> categoryService.inactiveCategory(categoryId)
    );

    String expectedExceptionMessage = "Category not found with id " + categoryId;

    assertTrue(categoryNotFoundException.getMessage().contains(expectedExceptionMessage));
  }

}
