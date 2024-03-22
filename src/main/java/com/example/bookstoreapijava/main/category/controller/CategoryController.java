package com.example.bookstoreapijava.main.category.controller;

import com.example.bookstoreapijava.main.category.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.category.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

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

  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> getCategory(@PathVariable UUID categoryId) {
    Category response = categoryService.getCategory(categoryId);

    return ResponseEntity.ok(response);
  }

  @PostMapping()
  public ResponseEntity<Category> insertCategory(@RequestBody @Valid Category category) throws URISyntaxException {
    CategoryCreatedVO savedCategory = categoryService.insertCategory(category);

    Category response = savedCategory.category();
    URI uri = savedCategory.uri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{categoryId}")
  public ResponseEntity<Category> updateCategory(
      @PathVariable UUID categoryId,
      @RequestBody @Valid CategoryUpdateDTO categoryUpdateDTO
  ) {
    Category updatedCategory = categoryService.updateCategory(categoryUpdateDTO, categoryId);

    return ResponseEntity.ok(updatedCategory);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
    categoryService.deleteCategory(categoryId);

    return ResponseEntity.noContent().build();
  }

}
