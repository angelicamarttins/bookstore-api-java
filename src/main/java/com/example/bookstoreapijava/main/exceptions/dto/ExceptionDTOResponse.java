package com.example.bookstoreapijava.main.exceptions.dto;

import org.springframework.http.HttpStatus;

public record ExceptionDTOResponse(HttpStatus status, String error, String message) {
}
