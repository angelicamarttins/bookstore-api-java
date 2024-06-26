package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDtoResponse;
import java.util.Optional;

public class ExceptionDtoResponseProvider {

  public static ExceptionDtoResponse createExceptionDtoResponse(
    Optional<Integer> status,
    Optional<String> error,
    Optional<String> message) {
    return new ExceptionDtoResponse(
      status.orElse(500),
      error.orElse("RuntimeException"),
      message.orElse(null));
  }

}
