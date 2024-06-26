package com.example.bookstoreapijava.main.data.dto.request;

import java.util.UUID;

public record BookUpdateDtoRequest(String title, String author, String isbn, UUID categoryId) {
}
