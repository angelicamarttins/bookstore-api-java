package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.dto.request.BookUpdateDtoRequest;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BookUpdateDtoRequestProvider {

  public static BookUpdateDtoRequest createBookUpdateDtoRequest(
    Map<String, Optional<String>> bookInfo,
    Optional<UUID> categoryId
  ) {
    return new BookUpdateDtoRequest(
      bookInfo.get("title").orElse(null),
      bookInfo.get("author").orElse(null),
      bookInfo.get("isbn").orElse(null),
      categoryId.orElse(null)
    );
  }

}
