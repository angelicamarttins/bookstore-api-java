package com.example.bookstoreapijava.providers;

import com.example.bookstoreapijava.main.data.vo.BookCreatedVo;
import com.example.bookstoreapijava.main.entities.Book;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;

public class BookCreatedVoProvider {

  @Value("${app.baseUrl}")
  private static String baseUrl;

  public static BookCreatedVo createBookCreatedVo(Book book) {
    URI uri = null;

    try {
      uri = new URI(baseUrl + "/bookstore/" + book.getBookId());
    } catch (URISyntaxException uriSyntaxException) {
      uriSyntaxException.getStackTrace();
    }

    return new BookCreatedVo(book, uri);
  }

}
