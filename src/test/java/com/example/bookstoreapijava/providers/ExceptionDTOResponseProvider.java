package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDTOResponse;

import java.util.Optional;

public class ExceptionDTOResponseProvider {

  public ExceptionDTOResponse createExceptionDTOResponse(Optional<Integer> status, Optional<String> error, Optional<String> message) {
    return new ExceptionDTOResponse(
        status.orElse(500),
        error.orElse("RuntimeException"),
        message.orElse(null));
  }

}
