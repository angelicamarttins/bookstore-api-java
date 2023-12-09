package com.example.bookstoreapijava.main.books.data.dto;

import com.example.bookstoreapijava.main.category.entities.Category;

public class BookResponseDTO {
  private String title;
  private String author;
  private Category category;

  public BookResponseDTO(String title, String author, Category category) {
    this.title = title;
    this.author = author;
    this.category = category;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }
}
