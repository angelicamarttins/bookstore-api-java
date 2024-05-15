package com.example.bookstoreapijava.main.data.dto;

import java.util.UUID;

public record BookUpdateDTORequest(String title, String author, String isbn, UUID categoryId) {
}
