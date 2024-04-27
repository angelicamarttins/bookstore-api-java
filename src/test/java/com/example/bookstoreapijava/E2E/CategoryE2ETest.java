package com.example.bookstoreapijava.E2E;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDTOResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategoryList;
import static com.example.bookstoreapijava.providers.ExceptionDTOResponseProvider.createExceptionDTOResponse;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryE2ETest extends PostgresTestContainersBase {

  @Autowired
  BookRepository bookRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @BeforeAll
  void setup() {
    baseURI = "http://localhost:" + port;
  }

  @AfterEach
  void cleanUpDb() {
    categoryRepository.deleteAll();
  }

  @Test
  @DisplayName(value = "When category is searched by id, returns correctly")
  void getCategoryByIdSuccessfully() {
    String categoryName = "Test Category Name";
    Category expectedCategory = createCategory(Optional.of(categoryName));
    categoryRepository.save(expectedCategory);

    Response response = given()
        .baseUri(baseURI)
        .get("/category/" + expectedCategory.getCategoryId());

    Category actualCategory = response.as(Category.class);

    response.then().statusCode(200);
    assertEquals(expectedCategory, actualCategory);
  }

  @Test
  @DisplayName(value = "When category is searched and is not found, should throw exception correctly")
  void getCategoryByIdNotFound() {
    UUID categoryId = UUID.randomUUID();

    ExceptionDTOResponse expectedExceptionDTOResponse =
        createExceptionDTOResponse(
            Optional.of(404),
            Optional.of("CategoryNotFoundException"),
            Optional.of("Category not found with id " + categoryId)
        );

    Response response = given()
        .baseUri(baseURI)
        .get("/category/" + categoryId);

    ExceptionDTOResponse actualExceptionDTOResponse = response.as(ExceptionDTOResponse.class);

    response.then().statusCode(404);
    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
  }

  @Test
  @DisplayName(value = "When category list is searched, returns correctly")
  void getCategoryListSuccessfully() {
    List<Category> expectedCategories = createCategoryList();
    categoryRepository.saveAll(expectedCategories);

    Response response = given()
        .baseUri(baseURI)
        .get("/category");

    List<Category> actualCategories = Arrays.asList(response.then().extract().as(Category[].class));

    response.then().statusCode(200);
    assertEquals(expectedCategories, actualCategories);
  }

}