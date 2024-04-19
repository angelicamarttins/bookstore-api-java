package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;

import java.net.URI;
import java.net.URISyntaxException;

public class BookCreatedVOProvider {

  public static BookCreatedVO createBookCreatedVO(Book book) throws URISyntaxException {
    URI uri = new URI("http://localhost:8080/bookstore/" + book.getBookId());

    return new BookCreatedVO(book, uri);
  }

}
