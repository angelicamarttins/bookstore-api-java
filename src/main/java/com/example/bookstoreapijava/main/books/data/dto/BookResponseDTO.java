package com.example.bookstoreapijava.main.books.data.dto;

import com.example.bookstoreapijava.main.books.entities.Book;
import com.example.bookstoreapijava.main.category.entities.Category;

public class BookResponseDTO {
  private Book book;
  private Category category;

  public BookResponseDTO(Book book, Category category) {
    this.book = book;
    this.category = category;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
