package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.dto.request.CategoryCreationRequest;
import java.util.Objects;

public class CategoryCreationRequestProvider {

  public static CategoryCreationRequest createCategoryCreationRequest(
    String categoryName
  ) {
    return new CategoryCreationRequest(
      Objects.requireNonNullElse(categoryName, "Category Creation Request")
    );
  }

}
