package com.example.bookstoreapijava.main.category.services;

import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

  public CategoryCreatedVO insertCategory(Category category) throws URISyntaxException {

    Category newCategory = categoryRepository.save(category);
    URI uri =
        new URI("http://localhost:8080/category/" + newCategory.getCategoryId().toString());

    CategoryCreatedVO categoryCreatedVO = new CategoryCreatedVO(newCategory, uri);

    return categoryCreatedVO;
  }
}
