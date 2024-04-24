package com.example.bookstoreapijava.E2E;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;

public class CategoryE2ETest extends PostgresTestContainersBase {

  @Autowired
  BookRepository bookRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @AfterAll
  void cleanUpDb() {
    bookRepository.deleteAll(); // TODO: Remover quando não mais inserir dados no banco automaticamente
    categoryRepository.deleteAll();
  }

  @Test
  @DisplayName(value = "When category is searched by id, returns correctly")
  void getCategoryByIdSuccessfully() {
    String categoryName = "Test Category Name";
    Category category = createCategory(Optional.of(categoryName));
    categoryRepository.save(category);

    Response response = given()
        .baseUri("http://localhost:" + port)
        .get("/category/" + category.getCategoryId());

    response.then().statusCode(200);

    Category categoryResponse = response.as(Category.class);

    assertEquals(category, categoryResponse);

  }

}
