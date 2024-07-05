package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDtoResponse;

public class ExceptionDtoResponseProvider {

  public static ExceptionDtoResponse createExceptionDtoResponse(
    Integer status,
    String error,
    String message) {
    return new ExceptionDtoResponse(
      status,
      error,
      message
    );
  }

}
