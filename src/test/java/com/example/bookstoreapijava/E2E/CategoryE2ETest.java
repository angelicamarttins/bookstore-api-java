package com.example.bookstoreapijava.E2E;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Optional;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;

public class CategoryE2ETest extends PostgresTestContainersBase {

  @Autowired
  CategoryRepository categoryRepository;

  @Test
  @DisplayName(value = "Testando E2E")
  void recoreco() {
    String categoryName = "Test Category Name";
    Category category = createCategory(Optional.of(categoryName));
    categoryRepository.save(category);

    given()
        .baseUri("http://localhost:" + port)
        .get("/category/" + category.getCategoryId())
        .then().statusCode(200)
        .body("categoryName", is(categoryName));
  }

}
