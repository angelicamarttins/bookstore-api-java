package com.example.bookstoreapijava.E2E;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.data.dto.request.CategoryUpdateDTORequest;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDTOResponse;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategoryList;
import static com.example.bookstoreapijava.providers.CategoryUpdateDTOProvider.createCategoryUpdateDTO;
import static com.example.bookstoreapijava.providers.ExceptionDTOResponseProvider.createExceptionDTOResponse;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryE2ETest extends PostgresTestContainersBase {

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
  @DisplayName(value = "When category is searched and is not found, should throws exception correctly")
  void getCategoryByIdNotFound() {
    UUID categoryId = UUID.randomUUID();

    ExceptionDTOResponse expectedExceptionDTOResponse =
        createExceptionDTOResponse(
            Optional.of(404),
            Optional.of("CategoryNotFoundException"),
            Optional.of("Category not found with id " + categoryId)
        );

    ExceptionDTOResponse actualExceptionDTOResponse = given()
        .baseUri(baseURI)
        .get("/category/" + categoryId)
        .then()
        .statusCode(404)
        .extract()
        .as(ExceptionDTOResponse.class);

    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
  }

  @Test
  @DisplayName(value = "When category list is searched and there is categories, returns correctly")
  void getCategoryListSuccessfully() {
    List<Category> expectedCategories = createCategoryList();
    categoryRepository.saveAll(expectedCategories);

    List<Category> actualCategories = Arrays.asList(given()
        .baseUri(baseURI)
        .get("/category")
        .then()
        .statusCode(200)
        .extract()
        .as(Category[].class));

    assertEquals(expectedCategories, actualCategories);
    assertFalse(actualCategories.isEmpty());
  }

  @Test
  @DisplayName(value = "When category list is searched and there is not categories, returns correctly")
  void getCategoryEmptyListSuccessfully() {
    List<Category> actualCategories = Arrays.asList(given()
        .baseUri(baseURI)
        .get("/category")
        .then()
        .statusCode(200)
        .extract()
        .as(Category[].class));

    assertTrue(actualCategories.isEmpty());
  }

  @Test
  @DisplayName(value = "When category is inserted, return correctly")
  void postCategorySuccessfully() {
    Category expectedCategory = createCategory(Optional.of("Category name")); //TODO: Ajustar para que nomes compostos, ambos os nomes permaneçam com letra maiúscula

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

    categoryRepository.save(category);

    ExceptionDTOResponse expectedExceptionDTOResponse = createExceptionDTOResponse(
        Optional.of(409),
        Optional.of("CategoryAlreadyExistsException"),
        Optional.of("Category already exists with name " + category.getCategoryName())
    );

    ExceptionDTOResponse actualExceptionDTOResponse = given()
        .baseUri(baseURI)
        .contentType("application/json")
        .body(category)
        .post("/category")
        .then()
        .statusCode(409)
        .extract()
        .as(ExceptionDTOResponse.class);

    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
  }

  @Test
  @DisplayName(value = "When category is updated, return correctly")
  void updateCategorySuccessfully() {
    Category savedCategory = createCategory(Optional.empty());

    categoryRepository.save(savedCategory);

    CategoryUpdateDTORequest categoryUpdateDTORequest = createCategoryUpdateDTO();

    Category updatedCategory = given()
        .baseUri(baseURI)
        .contentType("application/json")
        .body(categoryUpdateDTORequest)
        .patch("/category/" + savedCategory.getCategoryId())
        .then()
        .statusCode(200)
        .extract()
        .as(Category.class);

    assertNotNull(updatedCategory);
    assertNotEquals(savedCategory, updatedCategory);
    assertEquals(categoryUpdateDTORequest.categoryName(), updatedCategory.getCategoryName());
    assertNotNull(updatedCategory.getUpdatedAt());
  }

  @Test
  @DisplayName(value = "When category is updated and is not found, throws exception correctly")
  void updateCategoryNotFound() {
    UUID categoryId = UUID.randomUUID();

    CategoryUpdateDTORequest categoryUpdateDTORequest = createCategoryUpdateDTO();

    ExceptionDTOResponse expectedExceptionDTOResponse = createExceptionDTOResponse(
        Optional.of(404),
        Optional.of("CategoryNotFoundException"),
        Optional.of("Category not found with id " + categoryId)
    );

    ExceptionDTOResponse actualExceptionDTOResponse = given()
        .baseUri(baseURI)
        .contentType("application/json")
        .body(categoryUpdateDTORequest)
        .patch("/category/" + categoryId)
        .then()
        .statusCode(404)
        .extract()
        .as(ExceptionDTOResponse.class);

    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
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

    ExceptionDTOResponse expectedExceptionDTOResponse = createExceptionDTOResponse(
        Optional.of(404),
        Optional.of("CategoryNotFoundException"),
        Optional.of("Category not found with id " + categoryId)
    );

    ExceptionDTOResponse actualExceptionDTOResponse = given()
        .baseUri(baseURI)
        .delete("/category/" + categoryId)
        .then()
        .statusCode(404)
        .extract()
        .as(ExceptionDTOResponse.class);

    assertEquals(expectedExceptionDTOResponse, actualExceptionDTOResponse);
  }

}
