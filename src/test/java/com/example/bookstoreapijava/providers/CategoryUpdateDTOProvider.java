package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.dto.CategoryUpdateDTORequest;

public class CategoryUpdateDTOProvider {

  public static CategoryUpdateDTORequest createCategoryUpdateDTO() {
    return new CategoryUpdateDTORequest("New categoryName");
  }

}
