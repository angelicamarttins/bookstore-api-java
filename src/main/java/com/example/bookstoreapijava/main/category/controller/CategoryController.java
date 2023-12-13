package com.example.bookstoreapijava.main.category.controller;

import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.category.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<Category>> findAllCategories() {
    List<Category> response = categoryService.findAllCategories();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategory(@PathVariable Long id) {
    Category response = categoryService.getCategory(id);

    return ResponseEntity.ok(response);
  }

  @PostMapping()
  public <T> ResponseEntity<Category> insertCategory(@RequestBody Category category) throws URISyntaxException {
    Map<String, T> savedCategory = categoryService.insertCategory(category);

    Category response = (Category) savedCategory.get("category");
    URI uri = (URI) savedCategory.get("uri");

    return ResponseEntity.created(uri).body(response);
  }

}
