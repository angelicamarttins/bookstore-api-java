package com.example.bookstoreapijava.main.validators;

import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.CategoryAlreadyExistsException;
import com.example.bookstoreapijava.main.exceptions.CategoryIsInactiveException;
import com.example.bookstoreapijava.main.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class CategoryValidator {

  private final CategoryRepository categoryRepository;

  public Category checkIfCategoryIsFound(UUID categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> {
          log.info("Category not found. Aborting... CategoryId: {}", categoryId);

          return new CategoryNotFoundException(categoryId);
        });
  }

  public void checkIfCategoryAlreadyExists(Category category) {
    if (category.getInactivatedAt() == null) {
      log.info("Category already exists. CategoryId: {}", category.getCategoryId());

      throw new CategoryAlreadyExistsException(category.getCategoryName());
    }
  }

  public void checkIfCategoryIsActive(Category category) {
    UUID categoryId = category.getCategoryId();

    if (category.getInactivatedAt() != null) {
      log.info("Category is inactive. CategoryId: {}", categoryId);

      throw new CategoryIsInactiveException(categoryId);
    }
  }

}
