package com.example.bookstoreapijava.main.books.data.dto;

import com.example.bookstoreapijava.main.category.data.dto.CategoryResponseDTO;
import com.example.bookstoreapijava.main.category.entities.Category;

public class BookResponseDTO {
  private String title;
  private String author;
  private String isbn;
  private CategoryResponseDTO category;

  public BookResponseDTO(String title, String author, String isbn, CategoryResponseDTO category) {
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.category = category;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public CategoryResponseDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryResponseDTO category) {
    this.category = category;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }
}
