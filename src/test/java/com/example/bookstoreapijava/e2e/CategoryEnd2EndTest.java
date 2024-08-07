package com.example.bookstoreapijava.e2e;

import static com.example.bookstoreapijava.providers.CategoryCreationRequestProvider.createCategoryCreationRequest;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapijava.config.TestContainersBase;
import com.example.bookstoreapijava.main.data.dto.request.CategoryCreationRequest;
import com.example.bookstoreapijava.main.data.dto.request.CategoryUpdateDtoRequest;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDtoResponse;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class CategoryEnd2EndTest extends TestContainersBase {

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
  @DisplayName("When category is searched by id, returns correctly")
  void getCategoryByIdSuccessfully() {
    String categoryName = "Test Category Name";
    Category expectedCategory = createCategory(categoryName, null, null);

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
  @DisplayName("When category is searched and is not found, throws exception correctly")
  void getCategoryByIdNotFound() {
    UUID categoryId = UUID.randomUUID();

    ExceptionDtoResponse expectedExceptionDtoResponse =
      createExceptionDtoResponse(
        404,
        "CategoryNotFoundException",
        "Category not found. CategoryId: " + categoryId
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
  @DisplayName("When category list is searched and there is categories, returns correctly")
  void getCategoryListSuccessfully() {
    List<Category> expectedCategories = createCategoryList(5);

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
  @DisplayName("When category list is searched and there is no categories, returns correctly")
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
  @DisplayName("When category list is searched and there is active and inactive categories,"
    + " returns only active categories correctly")
  void getCategoryListSuccessfullyWithOnlyActiveCategories() {
    List<Category> expectedCategoryList = createCategoryList(5);
    Category inactiveCategory =
      createCategory(null, LocalDateTime.now(), LocalDateTime.now());
    expectedCategoryList.add(inactiveCategory);

    categoryRepository.saveAll(expectedCategoryList);

    List<Category> actualCategoryList = given()
      .baseUri(baseURI)
      .get("/category")
      .then()
      .statusCode(200)
      .extract()
      .jsonPath()
      .getList("content", Category.class);

    expectedCategoryList.remove(inactiveCategory);

    assertEquals(expectedCategoryList, actualCategoryList);
    assertEquals(expectedCategoryList.size(), actualCategoryList.size());
  }

  @Test
  @DisplayName("When category is inserted and does not exist, returns correctly")
  void postCategorySuccessfully() {
    CategoryCreationRequest expectedCategory =
      createCategoryCreationRequest("Category Name");

    ValidatableResponse response = given()
      .baseUri(baseURI)
      .contentType("application/json")
      .body(expectedCategory)
      .post("/category")
      .then()
      .statusCode(201);

    Category actualCategory = response
      .extract()
      .as(Category.class);

    response.header("Location", baseUrl + "/category/" + actualCategory.getCategoryId());
    assertEquals(expectedCategory.categoryName(), actualCategory.getCategoryName());
    assertNull(actualCategory.getUpdatedAt());
  }

  @Test
  @DisplayName("When category is inserted, already exist but is inactive, "
    + "reactivate category and returns correctly")
  void postReactivateCategorySuccessfully() {
    CategoryCreationRequest categoryCreationRequest =
      createCategoryCreationRequest("Any Category Name");
    Category expectedCategory = createCategory(
      categoryCreationRequest.categoryName(),
      LocalDateTime.now(),
      LocalDateTime.now()
    );

    categoryRepository.save(expectedCategory);

    Category actualCategory = given()
      .baseUri(baseURI)
      .contentType("application/json")
      .body(categoryCreationRequest)
      .post("/category")
      .then()
      .header("Location", baseUrl + "/category/" + expectedCategory.getCategoryId())
      .statusCode(201)
      .extract()
      .as(Category.class);

    assertNotEquals(expectedCategory, actualCategory);
    assertNull(actualCategory.getInactivatedAt());
    assertNotNull(actualCategory.getUpdatedAt());
  }

  @Test
  @DisplayName("When category is inserted, already exist and is active, throws exception correctly")
  void postCategoryAlreadyExists() {
    CategoryCreationRequest categoryCreationRequest = createCategoryCreationRequest(null);
    Category category =
      createCategory(categoryCreationRequest.categoryName(), null, null);

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      409,
      "CategoryAlreadyExistsException",
      "Category already exists. CategoryName: " + category.getCategoryName()
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
  @DisplayName("When category is updated, returns correctly")
  void updateCategorySuccessfully() {
    Category savedCategory = createCategory(null, null, null);
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
  @DisplayName("When category is updated and is not found, throws exception correctly")
  void updateCategoryNotFound() {
    UUID categoryId = UUID.randomUUID();
    CategoryUpdateDtoRequest categoryUpdateDtoRequest = createCategoryUpdateDto();

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      404,
      "CategoryNotFoundException",
      "Category not found. CategoryId: " + categoryId
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
  @DisplayName("When category is updated and is inactive, "
    + "reactivate category and returns correctly")
  void updateInactiveCategory() {
    Category expectedCategory =
      createCategory(null, LocalDateTime.now(), LocalDateTime.now());
    CategoryUpdateDtoRequest categoryUpdateDtoRequest = createCategoryUpdateDto();

    categoryRepository.save(expectedCategory);

    Category actualCategory = given()
      .baseUri(baseURI)
      .contentType("application/json")
      .body(categoryUpdateDtoRequest)
      .patch("/category/" + expectedCategory.getCategoryId())
      .then()
      .statusCode(200)
      .extract()
      .as(Category.class);

    assertNotEquals(expectedCategory, actualCategory);
    assertNull(actualCategory.getInactivatedAt());
    assertNotNull(actualCategory.getUpdatedAt());
  }

  @Test
  @DisplayName("When category is inactivated, returns correctly")
  void inactiveCategorySuccessfully() {
    Category savedCategory = createCategory(null, null, null);

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
  @DisplayName("When category is inactivated and is not found, throws exception correctly")
  void inactiveCategoryNotFound() {
    UUID categoryId = UUID.randomUUID();

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      404,
      "CategoryNotFoundException",
      "Category not found. CategoryId: " + categoryId
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

  @Test
  @DisplayName("When category is already inactive, throws exception correctly")
  void inactiveCategoryAlreadyInactive() {
    Category deletedCategory =
      createCategory(null, LocalDateTime.now(), LocalDateTime.now());

    categoryRepository.save(deletedCategory);

    ExceptionDtoResponse expectedExceptionDtoResponse = createExceptionDtoResponse(
      409,
      "CategoryIsInactiveException",
      "Category is inactive. CategoryId: " + deletedCategory.getCategoryId()
    );

    ExceptionDtoResponse actualExceptionDtoResponse = given()
      .baseUri(baseURI)
      .delete("/category/" + deletedCategory.getCategoryId())
      .then()
      .statusCode(409)
      .extract()
      .as(ExceptionDtoResponse.class);

    assertEquals(expectedExceptionDtoResponse, actualExceptionDtoResponse);
  }

}
