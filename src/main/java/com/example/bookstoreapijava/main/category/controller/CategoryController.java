package com.example.bookstoreapijava.main.category.controller;

import com.example.bookstoreapijava.main.category.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.category.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
  public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) {
    Category response = categoryService.getCategory(categoryId);

    return ResponseEntity.ok(response);
  }

  @PostMapping()
  public ResponseEntity<Category> insertCategory(@RequestBody Category category) throws URISyntaxException {
    CategoryCreatedVO savedCategory = categoryService.insertCategory(category);

    Category response = savedCategory.getCategory();
    URI uri = savedCategory.getUri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{categoryId}")
  public ResponseEntity<Category> updateCategory(
      @PathVariable Long categoryId,
      @RequestBody CategoryUpdateDTO categoryUpdateDTO
  ) {
    Category updatedCategory = categoryService.updateCategory(categoryUpdateDTO.getCategoryName(), categoryId);

    return ResponseEntity.ok(updatedCategory);
  }

}
