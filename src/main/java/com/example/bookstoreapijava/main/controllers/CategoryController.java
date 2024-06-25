package com.example.bookstoreapijava.main.controllers;

import com.example.bookstoreapijava.main.data.dto.request.CategoryUpdateDTORequest;
import com.example.bookstoreapijava.main.data.dto.response.PageResponse;
import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.services.CategoryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/category")
@AllArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<PageResponse<Category>> findAllCategories(
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    log.info("Finding all categories");

    PageResponse<Category> response = categoryService.findAllCategories(page, size);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> findCategory(@PathVariable UUID categoryId) {
    log.info("Finding category. CategoryId: {}", categoryId);

    Category response = categoryService.findCategory(categoryId);

    return ResponseEntity.ok(response);
  }

  @PostMapping()
  public ResponseEntity<Category> insertCategory(@RequestBody @Valid Category category)
    throws URISyntaxException {
    log.info("Creating category. CategoryName: {}", category.getCategoryName());

    CategoryCreatedVO savedCategory = categoryService.insertCategory(category);

    Category response = savedCategory.category();
    URI uri = savedCategory.uri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{categoryId}")
  public ResponseEntity<Category> updateCategory(
    @PathVariable UUID categoryId,
    @RequestBody @Valid CategoryUpdateDTORequest categoryUpdateDTORequest
  ) {
    log.info("Updating category. CategoryId: {}", categoryId);

    Category updatedCategory = categoryService.updateCategory(categoryUpdateDTORequest, categoryId);

    return ResponseEntity.ok(updatedCategory);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> inactiveCategory(@PathVariable UUID categoryId) {
    log.info("Deleting category. CategoryId: {}", categoryId);

    categoryService.inactiveCategory(categoryId);

    return ResponseEntity.noContent().build();
  }

}
