package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.category.data.dto.CategoryUpdateDTO;

public class CategoryUpdateDTOProvider {

  public static CategoryUpdateDTO createCategoryUpdateDTO() {
    return new CategoryUpdateDTO("New categoryName");
  }

}
