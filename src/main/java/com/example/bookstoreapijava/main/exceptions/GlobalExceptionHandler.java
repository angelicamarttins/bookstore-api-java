package com.example.bookstoreapijava.main.exceptions;

import com.example.bookstoreapijava.main.exceptions.dto.ExceptionDTOResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ExceptionDTOResponse>> handleValidationExceptions(
      MethodArgumentNotValidException methodArgumentNotValidException
  ) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    List<ExceptionDTOResponse> invalidArguments = new ArrayList<>();

    methodArgumentNotValidException
        .getBindingResult()
        .getAllErrors()
        .forEach(error -> {
          ExceptionDTOResponse exceptionResponse = new ExceptionDTOResponse(
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
  public ResponseEntity<ExceptionDTOResponse> handleRuntimeException(
      RuntimeException runtimeException
  ) {
    HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

    ExceptionDTOResponse exceptionResponse = new ExceptionDTOResponse(
        internalServerError.value(),
        RuntimeException.class.getSimpleName(),
        runtimeException.getMessage()
    );

    return ResponseEntity
        .status(internalServerError)
        .body(exceptionResponse);
  }

  @ExceptionHandler(BookNotFoundException.class)
  public ResponseEntity<ExceptionDTOResponse> handleBookNotFoundException(
      BookNotFoundException bookNotFoundException
  ) {
    HttpStatus notFound = HttpStatus.NOT_FOUND;

    ExceptionDTOResponse exceptionResponse = new ExceptionDTOResponse(
        notFound.value(),
        BookNotFoundException.class.getSimpleName(),
        bookNotFoundException.getMessage()
    );

    return ResponseEntity
        .status(notFound)
        .body(exceptionResponse);
  }

  @ExceptionHandler(BookAlreadyExistsException.class)
  public ResponseEntity<ExceptionDTOResponse> handleBookAlreadyExistsException(
      BookAlreadyExistsException bookAlreadyExistsException
  ) {
    HttpStatus conflict = HttpStatus.CONFLICT;

    ExceptionDTOResponse exceptionResponse = new ExceptionDTOResponse(
        conflict.value(),
        BookAlreadyExistsException.class.getSimpleName(),
        bookAlreadyExistsException.getMessage()
    );

    return ResponseEntity
        .status(conflict)
        .body(exceptionResponse);
  }

  @ExceptionHandler(BookIsInactiveException.class)
  public ResponseEntity<ExceptionDTOResponse> handleBookIsInactiveException(
      BookIsInactiveException bookIsInactiveException
  ) {
    HttpStatus conflict = HttpStatus.CONFLICT;

    ExceptionDTOResponse exceptionResponse = new ExceptionDTOResponse(
        conflict.value(),
        BookIsInactiveException.class.getSimpleName(),
        bookIsInactiveException.getMessage()
    );

    return ResponseEntity
        .status(conflict)
        .body(exceptionResponse);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ExceptionDTOResponse> handleCategoryNotFoundException(
      CategoryNotFoundException categoryNotFoundException
  ) {
    HttpStatus notFound = HttpStatus.NOT_FOUND;

    ExceptionDTOResponse exceptionResponse = new ExceptionDTOResponse(
        notFound.value(),
        CategoryNotFoundException.class.getSimpleName(),
        categoryNotFoundException.getMessage()
    );

    return ResponseEntity
        .status(notFound)
        .body(exceptionResponse);
  }

  @ExceptionHandler(CategoryAlreadyExistsException.class)
  public ResponseEntity<ExceptionDTOResponse> handleCategoryAlreadyExistsException(
      CategoryAlreadyExistsException categoryAlreadyExistsException
  ) {
    HttpStatus conflict = HttpStatus.CONFLICT;

    ExceptionDTOResponse exceptionResponse = new ExceptionDTOResponse(
        conflict.value(),
        CategoryAlreadyExistsException.class.getSimpleName(),
        categoryAlreadyExistsException.getMessage()
    );

    return ResponseEntity
        .status(conflict)
        .body(exceptionResponse);
  }

  @ExceptionHandler(CategoryIsInactiveException.class)
  public ResponseEntity<ExceptionDTOResponse> handleCategoryIsInactiveException(
      CategoryIsInactiveException categoryIsInactiveException
  ) {
    HttpStatus conflict = HttpStatus.CONFLICT;

    ExceptionDTOResponse exceptionResponse = new ExceptionDTOResponse(
        conflict.value(),
        CategoryIsInactiveException.class.getSimpleName(),
        categoryIsInactiveException.getMessage()
    );

    return ResponseEntity
        .status(conflict)
        .body(exceptionResponse);
  }

}
