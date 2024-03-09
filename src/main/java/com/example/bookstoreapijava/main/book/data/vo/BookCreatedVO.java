package com.example.bookstoreapijava.main.book.data.vo;

import com.example.bookstoreapijava.main.book.entities.Book;

import java.net.URI;

public class BookCreatedVO {
  private Book book;
  private URI uri;

  public BookCreatedVO(Book book, URI uri) {
    this.book = book;
    this.uri=uri;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public URI getUri() {
    return uri;
  }

  public void setUri(URI uri) {
    this.uri = uri;
  }
}
