package com.example.bookstoreapijava.main.category.services;

import com.example.bookstoreapijava.main.category.data.dto.CategoryResponseDTO;
import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public List<CategoryResponseDTO> findAllCategories() {
    List<CategoryResponseDTO> categoryList =
        categoryRepository
            .findAll()
            .stream()
            .map(category ->
                new CategoryResponseDTO(category.getCategoryId(), category.getCategoryName()))
            .collect(Collectors.toList());

    return categoryList;
  }

  public CategoryResponseDTO getCategory(Long id) {
    Category category = categoryRepository.getReferenceById(id);
    CategoryResponseDTO categoryResponseDTO =
        new CategoryResponseDTO(category.getCategoryId(), category.getCategoryName());

    return categoryResponseDTO;
  }

  public CategoryCreatedVO insertCategory(Category category) throws URISyntaxException {

    Category newCategory = categoryRepository.save(category);
    CategoryResponseDTO categoryResponseDTO =
        new CategoryResponseDTO(category.getCategoryId(), category.getCategoryName());
    URI uri =
        new URI("http://localhost:8080/category/" + newCategory.getCategoryId().toString());

    return new CategoryCreatedVO(categoryResponseDTO, uri);
  }

  public Category updateCategory(String categoryName, Long categoryId) {
    categoryRepository.updateCategoryById(categoryName, categoryId);

    return categoryRepository.getReferenceById(categoryId);
  }
}
