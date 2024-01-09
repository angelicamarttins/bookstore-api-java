package com.example.bookstoreapijava.main.category.data.dto;

public class CategoryUpdateDTO {
  private String categoryName;

  public CategoryUpdateDTO() {
  }

  public CategoryUpdateDTO(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
}
