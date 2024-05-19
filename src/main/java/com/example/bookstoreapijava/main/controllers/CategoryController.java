package com.example.bookstoreapijava.main.controllers;

import com.example.bookstoreapijava.main.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/category")
@AllArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<Category>> findAllCategories() {
    log.info("Finding all categories");

    List<Category> response = categoryService.findAllCategories();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> getCategory(@PathVariable UUID categoryId) {
    log.info("Finding category. CategoryId: {}", categoryId);

    Category response = categoryService.findCategory(categoryId);

    return ResponseEntity.ok(response);
  }

  @PostMapping()
  public ResponseEntity<Category> insertCategory(@RequestBody @Valid Category category) throws URISyntaxException {
    log.info("Creating category. CategoryName: {}", category.getCategoryName());

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
    log.info("Updating category. CategoryId: {}", categoryId);

    Category updatedCategory = categoryService.updateCategory(categoryUpdateDTO, categoryId);

    return ResponseEntity.ok(updatedCategory);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> inactiveCategory(@PathVariable UUID categoryId) {
    log.info("Deleting category. CategoryId: {}", categoryId);

    categoryService.inactiveCategory(categoryId);

    return ResponseEntity.noContent().build();
  }

}
