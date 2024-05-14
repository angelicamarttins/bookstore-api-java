package com.example.bookstoreapijava.main.category.services;

import com.example.bookstoreapijava.main.category.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.exceptions.CategoryAlreadyExistsException;
import com.example.bookstoreapijava.main.category.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  @Value("${app.baseUrl}")
  private static String baseUrl;

  private final CategoryRepository categoryRepository;

  public List<Category> findAllCategories() {
    log.info("All categories were found");

    return categoryRepository.findAll();
  }

  public Category findCategory(UUID categoryId) {
    Category category = searchAndCheckCategory(categoryId);

    log.info("Category found. CategoryId: {}", categoryId);

    return category;
  }

  public CategoryCreatedVO insertCategory(Category category) throws URISyntaxException {
    String sanitizedCategory = Utils.sanitizeStringField(category.getCategoryName());

    searchAndCheckCategoryAlreadyExists(sanitizedCategory);
    //TODO: Na v2, o isbn será buscada na API do Google e as categorias serão traduzidas pela API
    // do DeepL

    category.setCategoryName(sanitizedCategory);

    Category newCategory = categoryRepository.save(category);
    URI uri =
        new URI(baseUrl + "/category/" + newCategory.getCategoryId().toString());

    log.info("Category saved successfully. CategoryName: {}, CategoryId: {}",
        category.getCategoryName(),
        category.getCategoryId()
    );

    return new CategoryCreatedVO(newCategory, uri);
  }

  public Category updateCategory(CategoryUpdateDTO updateCategory, UUID categoryId) {
    Category savedCategory = searchAndCheckCategory(categoryId);
    log.info("All info sent is updated. Will now save category. CategoryId: {}", categoryId);

    savedCategory.setCategoryName(updateCategory.categoryName());

    return categoryRepository.save(savedCategory);
  }

  public void deleteCategory(UUID categoryId) {
    Category deletedCategory = searchAndCheckCategory(categoryId);

    deletedCategory.setInactivatedAt(LocalDateTime.now());

    categoryRepository.save(deletedCategory);

    log.info("Category deleted successfully. CategoryId: {}", categoryId);
  }

  private Category searchAndCheckCategory(UUID categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> {
          log.info("Category not found. Aborting... CategoryId: {}", categoryId);

          return new CategoryNotFoundException(categoryId);
        });
  }

  private void searchAndCheckCategoryAlreadyExists(String sanitizedCategory) {
    categoryRepository
        .getByCategoryName(sanitizedCategory)
        .ifPresent(savedCategory -> {
          log.info("Category already exists. CategoryId: {}", savedCategory.getCategoryId());

          throw new CategoryAlreadyExistsException(sanitizedCategory);
        });
  }

}
