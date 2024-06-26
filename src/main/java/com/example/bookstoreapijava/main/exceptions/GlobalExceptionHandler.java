package com.example.bookstoreapijava.main.exceptions;

import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDtoResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ExceptionDtoResponse>> handleValidationExceptions(
    MethodArgumentNotValidException methodArgumentNotValidException
  ) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    List<ExceptionDtoResponse> invalidArguments = new ArrayList<>();

    methodArgumentNotValidException
      .getBindingResult()
      .getAllErrors()
      .forEach(error -> {
        ExceptionDtoResponse exceptionResponse = new ExceptionDtoResponse(
          badRequest.value(),
          MethodArgumentNotValidException.class.getSimpleName(),
          error.getDefaultMessage()
        );

        invalidArguments.add(exceptionResponse);
      });

    return ResponseEntity
      .status(badRequest)
      .body(invalidArguments);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ExceptionDtoResponse> handleRuntimeException(
    RuntimeException runtimeException
  ) {
    HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

    ExceptionDtoResponse exceptionResponse =
      new ExceptionDtoResponse(
        internalServerError.value(),
        RuntimeException.class.getSimpleName(),
        runtimeException.getMessage());

    return ResponseEntity
      .status(internalServerError)
      .body(exceptionResponse);
  }

  @ExceptionHandler(BookNotFoundException.class)
  public ResponseEntity<ExceptionDtoResponse> handleBookNotFoundException(
    BookNotFoundException bookNotFoundException
  ) {
    HttpStatus notFound = HttpStatus.NOT_FOUND;

    ExceptionDtoResponse exceptionResponse = new ExceptionDtoResponse(
      notFound.value(),
      BookNotFoundException.class.getSimpleName(),
      bookNotFoundException.getMessage()
    );

    return ResponseEntity
      .status(notFound)
      .body(exceptionResponse);
  }

  @ExceptionHandler(BookAlreadyExistsException.class)
  public ResponseEntity<ExceptionDtoResponse> handleBookAlreadyExistsException(
    BookAlreadyExistsException bookAlreadyExistsException
  ) {
    HttpStatus conflict = HttpStatus.CONFLICT;

    ExceptionDtoResponse exceptionResponse = new ExceptionDtoResponse(
      conflict.value(),
      BookAlreadyExistsException.class.getSimpleName(),
      bookAlreadyExistsException.getMessage()
    );

    return ResponseEntity
      .status(conflict)
      .body(exceptionResponse);
  }

  @ExceptionHandler(BookIsInactiveException.class)
  public ResponseEntity<ExceptionDtoResponse> handleBookIsInactiveException(
    BookIsInactiveException bookIsInactiveException
  ) {
    HttpStatus conflict = HttpStatus.CONFLICT;

    ExceptionDtoResponse exceptionResponse = new ExceptionDtoResponse(
      conflict.value(),
      BookIsInactiveException.class.getSimpleName(),
      bookIsInactiveException.getMessage()
    );

    return ResponseEntity
      .status(conflict)
      .body(exceptionResponse);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ExceptionDtoResponse> handleCategoryNotFoundException(
    CategoryNotFoundException categoryNotFoundException
  ) {
    HttpStatus notFound = HttpStatus.NOT_FOUND;

    ExceptionDtoResponse exceptionResponse = new ExceptionDtoResponse(
      notFound.value(),
      CategoryNotFoundException.class.getSimpleName(),
      categoryNotFoundException.getMessage()
    );

    return ResponseEntity
      .status(notFound)
      .body(exceptionResponse);
  }

  @ExceptionHandler(CategoryAlreadyExistsException.class)
  public ResponseEntity<ExceptionDtoResponse> handleCategoryAlreadyExistsException(
    CategoryAlreadyExistsException categoryAlreadyExistsException
  ) {
    HttpStatus conflict = HttpStatus.CONFLICT;

    ExceptionDtoResponse exceptionResponse = new ExceptionDtoResponse(
      conflict.value(),
      CategoryAlreadyExistsException.class.getSimpleName(),
      categoryAlreadyExistsException.getMessage()
    );

    return ResponseEntity
      .status(conflict)
      .body(exceptionResponse);
  }

  @ExceptionHandler(CategoryIsInactiveException.class)
  public ResponseEntity<ExceptionDtoResponse> handleCategoryIsInactiveException(
    CategoryIsInactiveException categoryIsInactiveException
  ) {
    HttpStatus conflict = HttpStatus.CONFLICT;

    ExceptionDtoResponse exceptionResponse = new ExceptionDtoResponse(
      conflict.value(),
      CategoryIsInactiveException.class.getSimpleName(),
      categoryIsInactiveException.getMessage()
    );

    return ResponseEntity
      .status(conflict)
      .body(exceptionResponse);
  }

}
