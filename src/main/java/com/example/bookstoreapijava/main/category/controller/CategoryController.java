package com.example.bookstoreapijava.main.category.controller;

import com.example.bookstoreapijava.main.category.data.dto.CategoryResponseDTO;
import com.example.bookstoreapijava.main.category.data.dto.CategoryUpdateDTO;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.category.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<List<CategoryResponseDTO>> findAllCategories() {
    List<CategoryResponseDTO> response = categoryService.findAllCategories();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long categoryId) {
    CategoryResponseDTO response = categoryService.getCategory(categoryId);

    return ResponseEntity.ok(response);
  }

  @PostMapping()
  public ResponseEntity<CategoryResponseDTO> insertCategory(@RequestBody Category category) throws URISyntaxException {
    CategoryCreatedVO savedCategory = categoryService.insertCategory(category);

    CategoryResponseDTO response = savedCategory.getCategory();
    URI uri = savedCategory.getUri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{categoryId}")
  public ResponseEntity<CategoryResponseDTO> updateCategory(
      @PathVariable Long categoryId,
      @RequestBody CategoryUpdateDTO categoryUpdateDTO
  ) {
    Category updatedCategory = categoryService.updateCategory(categoryUpdateDTO.getCategoryName(), categoryId);

    CategoryResponseDTO response = new CategoryResponseDTO(categoryId, updatedCategory.getCategoryName());

    return ResponseEntity.ok(response);
  }

}
