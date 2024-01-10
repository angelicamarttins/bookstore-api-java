package com.example.bookstoreapijava.main.books.data.dto;

import java.util.Optional;

public class BookUpdateDTORequest {
  private Optional<String> title;
  private Optional<String> author;
  private Optional<String> isbn;
  private Optional<Long> categoryId;

  public BookUpdateDTORequest() {
  }

  public BookUpdateDTORequest(Optional<String> title, Optional<String> author, Optional<String> isbn, Optional<Long> categoryId) {
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.categoryId = categoryId;
  }

  public Optional<String> getTitle() {
    return title;
  }

  public void setTitle(Optional<String> title) {
    this.title = title;
  }

  public Optional<String> getAuthor() {
    return author;
  }

  public void setAuthor(Optional<String> author) {
    this.author = author;
  }

  public Optional<String> getIsbn() {
    return isbn;
  }

  public void setIsbn(Optional<String> isbn) {
    this.isbn = isbn;
  }

  public Optional<Long> getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Optional<Long> categoryId) {
    this.categoryId = categoryId;
  }
}
