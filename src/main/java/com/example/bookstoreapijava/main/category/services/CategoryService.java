package com.example.bookstoreapijava.main.category.services;

import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> findAllCategories() {
    return categoryRepository.findAll();
  }

  public Category getCategory(Long id) {
    return categoryRepository.getReferenceById(id);
  }

  public <T> Map<String, T>  insertCategory(Category category) throws URISyntaxException {
    Map<String, T> response = new HashMap<>();

    Category newCategory = categoryRepository.save(category);
    URI uri =
        new URI("http://localhost:8080/category/" + newCategory.getCategoryId().toString());

    response.put("category", (T) newCategory);
    response.put("uri", (T) uri);

    return response;
  }
}
