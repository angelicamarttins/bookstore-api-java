package com.example.bookstoreapijava.main.exceptions.dto;

public record ExceptionDtoResponse(Integer status, String error, String message) {
}
