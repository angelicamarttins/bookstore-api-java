package com.example.bookstoreapijava.main.category.controller;

import com.example.bookstoreapijava.main.category.data.dto.CategoryResponseDTO;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
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

  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id) {
    CategoryResponseDTO response = categoryService.getCategory(id);

    return ResponseEntity.ok(response);
  }

  @PostMapping()
  public ResponseEntity<CategoryResponseDTO> insertCategory(@RequestBody Category category) throws URISyntaxException {
    CategoryCreatedVO savedCategory = categoryService.insertCategory(category);

    CategoryResponseDTO response = savedCategory.getCategory();
    URI uri = savedCategory.getUri();

    return ResponseEntity.created(uri).body(response);
  }

}
