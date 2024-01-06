package com.example.bookstoreapijava.main.category.data.vo;

import com.example.bookstoreapijava.main.category.data.dto.CategoryResponseDTO;
import com.example.bookstoreapijava.main.category.entities.Category;

import java.net.URI;

public class CategoryCreatedVO {
  private CategoryResponseDTO category;
  private URI uri;

  public CategoryCreatedVO(CategoryResponseDTO category, URI uri) {
    this.category = category;
    this.uri = uri;
  }

  public CategoryResponseDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryResponseDTO category) {
    this.category = category;
  }

  public URI getUri() {
    return uri;
  }

  public void setUri(URI uri) {
    this.uri = uri;
  }
}
