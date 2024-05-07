package com.example.bookstoreapijava.main.book.data.dto;

import java.util.UUID;

public record BookUpdateDTORequest(String title, String author, String isbn, UUID categoryId) {
}
