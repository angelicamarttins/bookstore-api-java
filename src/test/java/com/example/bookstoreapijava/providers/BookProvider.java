package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.book.entities.Book;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.bookstoreapijava.utils.TestUtils.localDateTimeFormat;
import static com.example.bookstoreapijava.providers.CategoryProvider.createCategory;

public class BookProvider {

  public static Book createBook() {
    return new Book(
        UUID.randomUUID(),
        UUID.randomUUID().toString().replace("-", ""),
        UUID.randomUUID().toString().replace("-", ""),
        UUID.randomUUID().toString().replace("-", ""),
        localDateTimeFormat(LocalDateTime.now()),
        null,
        null,
        createCategory(Optional.empty())
    );
  }

}
