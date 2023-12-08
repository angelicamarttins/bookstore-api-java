package com.example.bookstoreapijava.main.category.controller;

import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping
  public List<Category> findAllCategories() {
    return categoryRepository.findAll();
  }

  @GetMapping("/{id}")
  public Optional<Category> getCategory(@PathVariable Long id) {
    return categoryRepository.findById(id);
  }

  @PostMapping()
  public Category insertCategory(@RequestBody Category category) {
    return categoryRepository.save(category);
  }

}
