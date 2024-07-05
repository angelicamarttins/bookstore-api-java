package com.example.bookstoreapijava.providers;

import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;
import static com.example.bookstoreapijava.utils.TestUtils.localDateTimeFormat;

import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.entities.Category;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BookProvider {

  public static Book createBook(
    String isbn,
    Category category,
    LocalDateTime updatedAt,
    LocalDateTime inactivatedAt
  ) {
    return new Book(
      UUID.randomUUID(),
      UUID.randomUUID().toString().replace("-", ""),
      UUID.randomUUID().toString().replace("-", ""),
      Objects.requireNonNullElse(isbn, "0123456789"),
      localDateTimeFormat(LocalDateTime.now()),
      localDateTimeFormat(updatedAt),
      localDateTimeFormat(inactivatedAt),
      Objects.requireNonNullElse(category, createCategory(null, null, null))
    );
  }

  public static List<Book> createBookList(Category category) {
    List<Book> bookList = new ArrayList<>();

    for (int i = 0; i <= 5; i++) {
      bookList.add(
        createBook(
          "0123456789" + i,
          Objects.requireNonNullElse(category, createCategory(null, null, null)),
          null,
          null
        )
      );
    }

    return bookList;
  }

//  public static Book createInactiveBook(
//    Optional<String> isbn,
//    Optional<Category> category,
//    Optional<LocalDateTime> inactivatedAt,
//    Optional<LocalDateTime> updatedAt
//  ) {
//    return new Book(
//      UUID.randomUUID(),
//      UUID.randomUUID().toString().replace("-", ""),
//      UUID.randomUUID().toString().replace("-", ""),
//      isbn.orElse("0123456789"),
//      localDateTimeFormat(LocalDateTime.now()),
//      updatedAt,
//      inactivatedAt,
//      Objects.requireNonNullElse(category, createCategory(Optional.empty()))
//    );
//  }

}
