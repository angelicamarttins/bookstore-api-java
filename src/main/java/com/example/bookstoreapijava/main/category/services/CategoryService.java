package com.example.bookstoreapijava.main.category.services;

import com.example.bookstoreapijava.main.category.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.exceptions.CategoryAlreadyExistsException;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> findAllCategories() {
    List<Category> categoryList = categoryRepository.findAll();

    return categoryList;
  }

  public Category getCategory(UUID categoryId) {
    Category category = categoryRepository.getReferenceById(categoryId);

    return category;
  }

  public CategoryCreatedVO insertCategory(Category category) throws URISyntaxException {
    String sanitizedCategory = Utils.sanitizeStringField(category.getCategoryName());

    categoryRepository
        .getByCategoryName(sanitizedCategory)
        .ifPresent(savedCategory -> {
          throw new CategoryAlreadyExistsException(sanitizedCategory);
        });
    //TODO: Isso pode dar um problema de sanitização, pois os acentos podem ser fundamentais
    // para distinguir categorias com mesmo nome sem acento, mas diferentes quando há acentos.
    // Pesquisar melhor sobre isso.

    category.setCategoryName(sanitizedCategory);

    Category newCategory = categoryRepository.save(category);
    URI uri =
        new URI("http://localhost:8080/category/" + newCategory.getCategoryId().toString());

    return new CategoryCreatedVO(newCategory, uri);
  }

  public Category updateCategory(CategoryUpdateDTO updatedCategory, UUID categoryId) {
    Category savedCategory = categoryRepository.getReferenceById(categoryId);

    savedCategory.setCategoryName(updatedCategory.categoryName());

    categoryRepository.save(savedCategory);

    return savedCategory;
  }

  public void deleteCategory(UUID categoryId) {
    Category deletedCategory = categoryRepository.getReferenceById(categoryId);

    deletedCategory.setInactivatedAt(LocalDateTime.now());

    categoryRepository.save(deletedCategory);
  }

}
