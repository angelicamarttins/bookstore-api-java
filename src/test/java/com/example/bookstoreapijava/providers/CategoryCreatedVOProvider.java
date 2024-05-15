package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVO;
import com.example.bookstoreapijava.main.entities.Category;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.URISyntaxException;

public class CategoryCreatedVOProvider {

  @Value("${app.baseUrl}")
  private static String baseUrl;

  public static CategoryCreatedVO createCategoryCreatedVO(Category category) throws URISyntaxException {
    URI uri =
        new URI(baseUrl + "/category/" + category.getCategoryId());

    return new CategoryCreatedVO(category, uri);
  }

}
