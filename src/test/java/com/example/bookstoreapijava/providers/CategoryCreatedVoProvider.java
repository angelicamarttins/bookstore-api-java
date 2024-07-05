package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.vo.CategoryCreatedVo;
import com.example.bookstoreapijava.main.entities.Category;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;

public class CategoryCreatedVoProvider {

  @Value("${app.baseUrl}")
  private static String baseUrl;

  public static CategoryCreatedVo createCategoryCreatedVo(Category category) {
    URI uri = null;

    try {
      uri = new URI(baseUrl + "/category/" + category.getCategoryId());
    } catch (URISyntaxException uriSyntaxException) {
      uriSyntaxException.getStackTrace();
    }

    return new CategoryCreatedVo(category, uri);
  }

}
