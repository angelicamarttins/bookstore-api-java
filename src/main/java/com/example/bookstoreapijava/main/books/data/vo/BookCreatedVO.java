package com.example.bookstoreapijava.main.books.data.vo;

import com.example.bookstoreapijava.main.books.data.dto.BookResponseDTO;

import java.net.URI;

public class BookCreatedVO {
  private BookResponseDTO bookResponseDTO;
  private URI uri;

  public BookCreatedVO(BookResponseDTO bookResponseDTO, URI uri) {
    this.bookResponseDTO = bookResponseDTO;
    this.uri=uri;
  }

  public BookResponseDTO getBookResponseDTO() {
    return bookResponseDTO;
  }

  public void setBookResponseDTO(BookResponseDTO bookResponseDTO) {
    this.bookResponseDTO = bookResponseDTO;
  }

  public URI getUri() {
    return uri;
  }

  public void setUri(URI uri) {
    this.uri = uri;
  }
}
