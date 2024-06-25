package com.example.bookstoreapijava.main.services;

import com.example.bookstoreapijava.main.data.dto.request.CategoryUpdateDTORequest;
import com.example.bookstoreapijava.main.data.dto.response.PageResponse;
import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.factories.PageResponseFactory;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.utils.Utils;
import com.example.bookstoreapijava.main.validators.CategoryValidator;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  @Value("${app.baseUrl}")
  private static String baseUrl;
  private final CategoryRepository categoryRepository;
  private final CategoryValidator categoryValidator;

  public PageResponse<Category> findAllCategories(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Category> activeCategories = categoryRepository.findAllActiveCategories(pageRequest);

    log.info("All categories were found");

    return PageResponseFactory.toPageResponse(activeCategories);
  }

  public Category findCategory(UUID categoryId) {
    Category category = categoryValidator.checkIfCategoryIsFound(categoryId);

    log.info("Category found. CategoryId: {}", categoryId);

    return category;
  }

  @Transactional
  public CategoryCreatedVO insertCategory(Category category) throws URISyntaxException {
    String sanitizedCategoryName = Utils.sanitizeStringField(category.getCategoryName());
    Optional<Category> maybeCategory =
      categoryRepository.getBySanitizedCategoryName(sanitizedCategoryName);

    if (maybeCategory.isPresent()) {
      categoryValidator.checkIfCategoryAlreadyExists(maybeCategory.get());

      Category reactivatedCategory = reactivateCategory(maybeCategory.get());

      return createCategory(reactivatedCategory, false);
    }

    //TODO: Na v2, o isbn será buscada na API do Google e as categorias serão traduzidas pela API
    // do DeepL
    return createCategory(category, true);
  }

  @Transactional
  public Category updateCategory(CategoryUpdateDTORequest updateCategory, UUID categoryId) {
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

  private CategoryCreatedVO createCategory(Category category, boolean shouldSaveCategory)
    throws URISyntaxException {
    Category newCategory = category;

    if (shouldSaveCategory) {
      newCategory = saveCategory(newCategory);
    }

    URI uri =
      new URI(baseUrl + "/category/" + newCategory.getCategoryId().toString());

    return new CategoryCreatedVO(newCategory, uri);
  }

  private Category saveCategory(Category category) {
    Category newCategory = categoryRepository.save(updateSanitizedCategoryName(category));

    log.info("Category saved successfully. CategoryName: {}, CategoryId: {}",
      category.getCategoryName(),
      category.getCategoryId()
    );

    return newCategory;
  }

  private Category updateSanitizedCategoryName(Category category) {
    String sanitizedCategoryName = Utils.sanitizeStringField(category.getCategoryName());

    category.setSanitizedCategoryName(sanitizedCategoryName);

    return category;
  }


  private Category reactivateCategory(Category savedCategory) {
    log.info(
      "Category has been inactivated. Will now reactivate it. CategoryName: {}, CategoryId: {}",
      savedCategory.getCategoryName(),
      savedCategory.getCategoryId()
    );

    Category sanitizedCategory = updateSanitizedCategoryName(savedCategory);

    sanitizedCategory.setInactivatedAt(null);

    categoryRepository.reactivateByCategoryId(sanitizedCategory.getCategoryId());

    return sanitizedCategory;
  }

}
