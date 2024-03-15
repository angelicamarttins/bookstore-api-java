package com.example.bookstoreapijava.main.book.data.dto;

import com.example.bookstoreapijava.main.category.entities.Category;

import java.util.Optional;
import java.util.OptionalLong;

public record BookUpdateDTORequest(String title, String author, String isbn, Category category) {}
