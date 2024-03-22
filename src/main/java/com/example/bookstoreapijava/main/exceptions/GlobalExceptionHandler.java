package com.example.bookstoreapijava.main.exceptions;

import com.example.bookstoreapijava.main.book.exceptions.BookAlreadyExistsException;
import com.example.bookstoreapijava.main.book.exceptions.BookNotFoundException;
import com.example.bookstoreapijava.main.category.exceptions.CategoryAlreadyExistsException;
import com.example.bookstoreapijava.main.category.exceptions.CategoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<>();

    exception.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();

      errors.put(fieldName, errorMessage);
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(BookNotFoundException.class)
  public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException bookNotFoundException) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookNotFoundException.getMessage());
  }

  @ExceptionHandler(BookAlreadyExistsException.class)
  public ResponseEntity<Object> handleBookAlreadyExistsException(BookAlreadyExistsException bookAlreadyExistsException) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(bookAlreadyExistsException.getMessage());
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException categoryNotFoundException) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(categoryNotFoundException.getMessage());
  }

  @ExceptionHandler(CategoryAlreadyExistsException.class)
  public ResponseEntity<Object> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException categoryAlreadyExistsException) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(categoryAlreadyExistsException.getMessage());
  }

}
