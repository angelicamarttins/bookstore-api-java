package com.example.bookstoreapijava.main.book.data.dto;

import com.example.bookstoreapijava.main.category.entities.Category;

import java.util.Optional;
import java.util.OptionalLong;

public class BookUpdateDTORequest {

  private String title;
  private String author;
  private String isbn;
  private Category category;

  public BookUpdateDTORequest() {
  }

  public BookUpdateDTORequest(String title, String author, String isbn, Category category) {
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

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
