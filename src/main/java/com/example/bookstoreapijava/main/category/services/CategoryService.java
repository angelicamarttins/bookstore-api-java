package com.example.bookstoreapijava.main.category.services;

import com.example.bookstoreapijava.main.category.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> findAllCategories() {
    List<Category> categoryList = categoryRepository.findAll();

    return categoryList;
  }

  public Category getCategory(Long id) {
    Category category = categoryRepository.getReferenceById(id);

    return category;
  }

  public CategoryCreatedVO insertCategory(Category category) throws URISyntaxException {

    Category newCategory = categoryRepository.save(category);
    URI uri =
        new URI("http://localhost:8080/category/" + newCategory.getCategoryId().toString());

    return new CategoryCreatedVO(newCategory, uri);
  }

  public Category updateCategory(CategoryUpdateDTO updatedCategory, Long categoryId) {
    Category savedCategory = categoryRepository.getReferenceById(categoryId);

    savedCategory.setCategoryName(updatedCategory.getCategoryName());

    categoryRepository.save(savedCategory);

    return savedCategory;
  }

  public void deleteCategory(Long categoryId) {
    Category deletedCategory = categoryRepository.getReferenceById(categoryId);

    deletedCategory.setInactivatedAt(LocalDateTime.now());

    categoryRepository.save(deletedCategory);
  }

}
