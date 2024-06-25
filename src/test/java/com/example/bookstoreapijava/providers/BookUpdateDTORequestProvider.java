package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.dto.request.BookUpdateDTORequest;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BookUpdateDTORequestProvider {

  public static BookUpdateDTORequest createBookUpdateDTORequest(
    Map<String, Optional<String>> bookInfo,
    Optional<UUID> categoryId
  ) {
    return new BookUpdateDTORequest(
      bookInfo.get("title").orElse(null),
      bookInfo.get("author").orElse(null),
      bookInfo.get("isbn").orElse(null),
      categoryId.orElse(null)
    );
  }

}
