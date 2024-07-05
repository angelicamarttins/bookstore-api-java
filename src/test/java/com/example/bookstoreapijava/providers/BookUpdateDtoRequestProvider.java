package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.dto.request.BookUpdateDtoRequest;
import java.util.Map;
import java.util.UUID;

public class BookUpdateDtoRequestProvider {

  public static BookUpdateDtoRequest createBookUpdateDtoRequest(
    Map<String, String> bookInfo,
    UUID categoryId
  ) {
    return new BookUpdateDtoRequest(
      bookInfo.get("title"),
      bookInfo.get("author"),
      bookInfo.get("isbn"),
      categoryId
    );
  }

}
