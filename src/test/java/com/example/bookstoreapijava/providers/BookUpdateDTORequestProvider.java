package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.category.entities.Category;

import java.util.Map;
import java.util.Optional;

public class BookUpdateDTORequestProvider {

  public static BookUpdateDTORequest createBookUpdateDTORequest(
      Map<String, Optional<String>> bookInfo,
      Optional<Category> category
  ) {
    return new BookUpdateDTORequest(
        bookInfo.get("title").orElse(null),
        bookInfo.get("author").orElse(null),
        bookInfo.get("isbn").orElse(null),
        category.orElse(null)
    );
  }

}
