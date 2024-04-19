package com.example.bookstoreapijava.main.book.data.dto;

import com.example.bookstoreapijava.main.category.entities.Category;

public record BookUpdateDTORequest(String title, String author, String isbn, Category category) {}
