package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.category.entities.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.bookstoreapijava.utils.TestUtils.localDateTimeFormat;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;

public class BookProvider {

  public static Book createBook(Optional<String> isbn, Optional<Category> category) {
    return new Book(
        UUID.randomUUID(),
        UUID.randomUUID().toString().replace("-", ""),
        UUID.randomUUID().toString().replace("-", ""),
        isbn.orElse("0123456789"),
        localDateTimeFormat(LocalDateTime.now()),
        null,
        null,
        category.orElse(createCategory(Optional.empty()))
    );
  }

  public static List<Book> createBookList(Optional<Category> category) {
    List<Book> bookList = new ArrayList<>();

    for (int i = 0; i <= 5; i++) {
      bookList.add(createBook(Optional.of("0123456789" + i) ,category));
    }

    return bookList;
  }

}
