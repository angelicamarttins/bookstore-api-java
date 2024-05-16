package com.example.bookstoreapijava.main.validators;

import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.CategoryAlreadyExistsException;
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

  public Category searchAndCheckCategory(UUID categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> {
          log.info("Category not found. Aborting... CategoryId: {}", categoryId);

          return new CategoryNotFoundException(categoryId);
        });
  }

  public void searchAndCheckCategoryAlreadyExists(String sanitizedCategory) {
    categoryRepository
        .getByCategoryName(sanitizedCategory)
        .ifPresent(savedCategory -> {
          log.info("Category already exists. CategoryId: {}", savedCategory.getCategoryId());

          throw new CategoryAlreadyExistsException(sanitizedCategory);
        });
  }

}
