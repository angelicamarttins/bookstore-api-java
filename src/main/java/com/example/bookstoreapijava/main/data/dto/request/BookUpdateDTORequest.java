package com.example.bookstoreapijava.main.data.dto.request;

import java.util.UUID;

public record BookUpdateDTORequest(String title, String author, String isbn, UUID categoryId) {
}
