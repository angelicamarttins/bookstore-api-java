package com.example.bookstoreapijava.E2E;

import com.example.bookstoreapijava.config.PostgresTestContainersBase;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.example.bookstoreapijava.providers.BookProvider.createBook;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class BookE2ETest extends PostgresTestContainersBase {

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
    bookRepository.deleteAll();
  }

  @Test
  @DisplayName(value = "When book is searched by id, returns correctly")
  void getBookByIdSuccessfully() {
    Book expectedBook = createBook(Optional.empty(), Optional.empty());

    categoryRepository.save(expectedBook.getCategory());
    bookRepository.save(expectedBook);

    Book actualBook = given()
        .baseUri(baseURI)
        .get("/bookstore/" + expectedBook.getBookId())
        .then()
        .statusCode(200)
        .extract()
        .as(Book.class);

    assertEquals(expectedBook, actualBook);
  }

}
