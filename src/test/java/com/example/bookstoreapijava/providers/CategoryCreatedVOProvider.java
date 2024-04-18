package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.category.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.category.entities.Category;

import java.net.URI;
import java.net.URISyntaxException;

public class CategoryCreatedVOProvider {

  public static CategoryCreatedVO createCategoryCreatedVO(Category category) throws URISyntaxException {
    URI uri =
        new URI("http://localhost:8080/category/" + category.getCategoryId());

    return new CategoryCreatedVO(category, uri);
  }

}
