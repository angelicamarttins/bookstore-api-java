package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.URISyntaxException;

public class BookCreatedVOProvider {

  @Value("${app.baseUrl}")
  private static String baseUrl;

  public static BookCreatedVO createBookCreatedVO(Book book) throws URISyntaxException {
    URI uri = new URI(baseUrl + "/bookstore/" + book.getBookId());

    return new BookCreatedVO(book, uri);
  }

}
