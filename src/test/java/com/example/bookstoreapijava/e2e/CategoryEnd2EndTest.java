package com.example.bookstoreapijava.e2e;

import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategoryList;
import static com.example.bookstoreapijava.providers.CategoryUpdateDtoProvider.createCategoryUpdateDto;
import static com.example.bookstoreapijava.providers.ExceptionDtoResponseProvider.createExceptionDtoResponse;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.data.dto.request.CategoryUpdateDtoRequest;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDtoResponse;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import io.restassured.path.json.JsonPath;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class CategoryEnd2EndTest extends PostgresTestContainersBase {

  @Value("${app.baseUrl}")
  private static String baseUrl;

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

    Category actualCategory = given()
      .baseUri(baseURI)
      .get("/category/" + expectedCategory.getCategoryId())
      .then()
      .statusCode(200)
      .extract()
      .as(Category.class);

    assertEquals(expectedCategory, actualCategory);
  }

  @Test
  @DisplayName(value = "When category is searched and is not found, "
    + "should throws exception correctly")
  void getCategoryByIdNotFound() {
    UUID categoryId = UUID.randomUUID();

    ExceptionDtoResponse expectedExceptionDtoResponse =
      createExceptionDtoResponse(
        Optional.of(404),
        Optional.of("CategoryNotFoundException"),
        Optional.of("Category not found with id " + categoryId)
      );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .baseUri(baseURI)
      .get("/category/" + categoryId)
      .then()
      .statusCode(404)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName(value = "When category list is searched and there is categories, returns correctly")
  void getCategoryListSuccessfully() {
    List<Category> expectedCategories = createCategoryList();

    categoryRepository.saveAll(expectedCategories);

    JsonPath response = given()
      .params("page", 0)
      .params("size", 1)
      .baseUri(baseURI)
      .get("/category")
      .then()
      .statusCode(200)
      .extract()
      .response()
      .jsonPath();

    List<Category> actualCategoriesContent = response.getList("content", Category.class);
    Boolean hasNextPage = response.getBoolean("hasNextPage");

    assertEquals(expectedCategories.getFirst(), actualCategoriesContent.getFirst());
    assertTrue(hasNextPage);
  }

  @Test
  @DisplayName(value = "When category list is searched and there is no categories, "
    + "returns correctly")
  void getCategoryEmptyListSuccessfully() {
    JsonPath response = given()
      .baseUri(baseURI)
      .get("/category")
      .then()
      .statusCode(200)
      .extract()
      .jsonPath();

    List<Category> actualActiveCategoriesContent = response.getList("content", Category.class);
    Boolean hasNextPage = response.getBoolean("hasNextPage");

    assertTrue(actualActiveCategoriesContent.isEmpty());
    assertFalse(hasNextPage);
  }

  @Test
  @DisplayName(value = "When category is inserted, return correctly")
  void postCategorySuccessfully() {
    Category expectedCategory = createCategory(Optional.of("Category name"));
    //TODO: Ajustar para que nomes compostos, ambos os nomes permaneçam com letra maiúscula

    Category actualCategory = given()
      .baseUri(baseURI)
      .contentType("application/json")
      .body(expectedCategory)
      .post("/category")
      .then()
      .header("Location", baseUrl + "/category/" + expectedCategory.getCategoryId())
      .statusCode(201)
      .extract()
      .as(Category.class);

    assertEquals(expectedCategory, actualCategory);
  }

  @Test
  @DisplayName(value = "When category is inserted and already exists, throws exception correctly")
  void postCategoryAlreadyExists() {
    Category category = createCategory(Optional.of("Category name"));

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      Optional.of(409),
      Optional.of("CategoryAlreadyExistsException"),
      Optional.of("Category already exists with name " + category.getCategoryName())
    );

    categoryRepository.save(category);

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .baseUri(baseURI)
      .contentType("application/json")
      .body(category)
      .post("/category")
      .then()
      .statusCode(409)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName(value = "When category is updated, return correctly")
  void updateCategorySuccessfully() {
    Category savedCategory = createCategory(Optional.empty());
    CategoryUpdateDtoRequest categoryUpdateDtoRequest = createCategoryUpdateDto();

    categoryRepository.save(savedCategory);

    Category updatedCategory = given()
      .baseUri(baseURI)
      .contentType("application/json")
      .body(categoryUpdateDtoRequest)
      .patch("/category/" + savedCategory.getCategoryId())
      .then()
      .statusCode(200)
      .extract()
      .as(Category.class);

    assertNotNull(updatedCategory);
    assertNotEquals(savedCategory, updatedCategory);
    assertEquals(categoryUpdateDtoRequest.categoryName(), updatedCategory.getCategoryName());
    assertNotNull(updatedCategory.getUpdatedAt());
  }

  @Test
  @DisplayName(value = "When category is updated and is not found, throws exception correctly")
  void updateCategoryNotFound() {
    UUID categoryId = UUID.randomUUID();
    CategoryUpdateDtoRequest categoryUpdateDtoRequest = createCategoryUpdateDto();

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      Optional.of(404),
      Optional.of("CategoryNotFoundException"),
      Optional.of("Category not found with id " + categoryId)
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .baseUri(baseURI)
      .contentType("application/json")
      .body(categoryUpdateDtoRequest)
      .patch("/category/" + categoryId)
      .then()
      .statusCode(404)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

  @Test
  @DisplayName(value = "When category is deleted, returns correctly")
  void deleteCategorySuccessfully() {
    Category savedCategory = createCategory(Optional.empty());

    categoryRepository.save(savedCategory);

    given()
      .baseUri(baseURI)
      .delete("/category/" + savedCategory.getCategoryId())
      .then()
      .statusCode(204);

    Category deletedCategory = categoryRepository.findById(savedCategory.getCategoryId()).get();

    assertNotEquals(savedCategory, deletedCategory);
    assertNotNull(deletedCategory.getUpdatedAt());
    assertNotNull(deletedCategory.getInactivatedAt());
  }

  @Test
  @DisplayName(value = "When category is deleted and is not found, throws exception correctly")
  void deleteCategoryNotFound() {
    UUID categoryId = UUID.randomUUID();

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      Optional.of(404),
      Optional.of("CategoryNotFoundException"),
      Optional.of("Category not found with id " + categoryId)
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .baseUri(baseURI)
      .delete("/category/" + categoryId)
      .then()
      .statusCode(404)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

}