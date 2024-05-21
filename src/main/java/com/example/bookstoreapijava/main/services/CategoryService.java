package com.example.bookstoreapijava.main.services;

import com.example.bookstoreapijava.main.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.utils.Utils;
import com.example.bookstoreapijava.main.validators.CategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  @Value("${app.baseUrl}")
  private static String baseUrl;
  private final CategoryRepository categoryRepository;
  private final CategoryValidator categoryValidator;

  public List<Category> findAllCategories() {
    log.info("All categories were found");

    return categoryRepository.findAll();
  }

  public Category findCategory(UUID categoryId) {
    Category category = categoryValidator.checkIfCategoryIsFound(categoryId);

    log.info("Category found. CategoryId: {}", categoryId);

    return category;
  }

  public CategoryCreatedVO insertCategory(Category category) throws URISyntaxException {
    String sanitizedCategoryName = Utils.sanitizeStringField(category.getCategoryName());

    Optional<Category> maybeCategory = categoryRepository.getByCategoryName(sanitizedCategoryName);

    if (maybeCategory.isPresent()) {
      categoryValidator.checkIfCategoryAlreadyExists(maybeCategory.get());

      Category reactivatedCategory = reactivateCategory(maybeCategory.get());

      return createCategory(reactivatedCategory, false);
    }

    //TODO: Na v2, o isbn será buscada na API do Google e as categorias serão traduzidas pela API
    // do DeepL
    return createCategory(category, true);
  }

  public Category updateCategory(CategoryUpdateDTO updateCategory, UUID categoryId) {
    Category savedCategory = categoryValidator.checkIfCategoryIsFound(categoryId);

    if (savedCategory.getInactivatedAt() != null) {
      return reactivateCategory(savedCategory);
    }

    savedCategory.setCategoryName(updateCategory.categoryName());

    log.info("All info sent is updated. Will now save category. CategoryId: {}", categoryId);

    return saveCategory(savedCategory);
  }

  public void inactiveCategory(UUID categoryId) {
    Category inactivatedCategory = categoryValidator.checkIfCategoryIsFound(categoryId);

    categoryValidator.checkIfCategoryIsInactive(inactivatedCategory);

    inactivatedCategory.setInactivatedAt(LocalDateTime.now());

    categoryRepository.save(inactivatedCategory);

    log.info("Category deleted successfully. CategoryId: {}", categoryId);
  }

  private CategoryCreatedVO createCategory(Category category, boolean shouldSaveCategory) throws URISyntaxException {
    Category newCategory = category;

    if (shouldSaveCategory) {
      newCategory = categoryRepository.save(category);
    }

    URI uri =
        new URI(baseUrl + "/category/" + newCategory.getCategoryId().toString());

    return new CategoryCreatedVO(newCategory, uri);
  }

  private Category saveCategory(Category category) {
    String sanitizedCategoryName = Utils.sanitizeStringField(category.getCategoryName());

    category.setSanitizedCategoryName(sanitizedCategoryName);

    Category newCategory = categoryRepository.save(category);

    log.info("Category saved successfully. CategoryName: {}, CategoryId: {}",
        category.getCategoryName(),
        category.getCategoryId()
    );

    return newCategory;
  }

  private Category reactivateCategory(Category savedCategory) {
    log.info("Category has been inactivated. Will now reactivate it. CategoryName: {}, CategoryId: {}",
        savedCategory.getCategoryName(),
        savedCategory.getCategoryId()
    );

    String sanitizedCategoryName = Utils.sanitizeStringField(savedCategory.getCategoryName());

    savedCategory.setSanitizedCategoryName(sanitizedCategoryName);
    savedCategory.setInactivatedAt(null);

    categoryRepository.reactivateByCategoryId(savedCategory.getCategoryId().toString());

    return savedCategory;
  }

}
