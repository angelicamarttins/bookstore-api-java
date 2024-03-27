package com.example.bookstoreapijava.main.exceptions.dto;

public record ExceptionDTOResponse(Integer status, String error, String message) {
}
