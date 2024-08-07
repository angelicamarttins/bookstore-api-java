package com.example.bookstoreapijava.main.controllers;

import static com.example.bookstoreapijava.main.data.dto.request.CategoryCreationRequest.toDomain;

import com.example.bookstoreapijava.main.data.dto.request.CategoryCreationRequest;
import com.example.bookstoreapijava.main.data.dto.request.CategoryUpdateDtoRequest;
import com.example.bookstoreapijava.main.data.dto.response.PageResponse;
import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVo;
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
  public ResponseEntity<Category> insertCategory(
    @RequestBody @Valid CategoryCreationRequest categoryCreationRequest
  ) throws URISyntaxException {
    log.info("Creating category. CategoryName: {}", categoryCreationRequest.categoryName());

    CategoryCreatedVo savedCategory =
      categoryService.insertCategory(toDomain(categoryCreationRequest.categoryName()));

    Category response = savedCategory.category();
    URI uri = savedCategory.uri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{categoryId}")
  public ResponseEntity<Category> updateCategory(
    @PathVariable UUID categoryId,
    @RequestBody @Valid CategoryUpdateDtoRequest categoryUpdateDtoRequest
  ) {
    log.info("Updating category. CategoryId: {}", categoryId);

    Category updatedCategory = categoryService.updateCategory(categoryUpdateDtoRequest, categoryId);

    return ResponseEntity.ok(updatedCategory);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> inactiveCategory(@PathVariable UUID categoryId) {
    log.info("Inactivating category. CategoryId: {}", categoryId);

    categoryService.inactiveCategory(categoryId);

    return ResponseEntity.noContent().build();
  }

}
