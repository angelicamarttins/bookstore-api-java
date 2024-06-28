package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.dto.request.CategoryCreationRequest;
import java.util.Optional;

public class CategoryCreationRequestProvider {

  public static CategoryCreationRequest createCategoryCreationRequest(
    Optional<String> categoryName
  ) {
    return new CategoryCreationRequest(categoryName.orElse("Category Creation Request"));
  }

}
