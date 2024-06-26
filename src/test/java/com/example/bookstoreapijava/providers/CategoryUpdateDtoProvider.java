package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.dto.request.CategoryUpdateDtoRequest;

public class CategoryUpdateDtoProvider {

  public static CategoryUpdateDtoRequest createCategoryUpdateDto() {
    return new CategoryUpdateDtoRequest("New categoryName");
  }

}
