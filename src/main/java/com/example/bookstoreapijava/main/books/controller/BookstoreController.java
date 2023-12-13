package com.example.bookstoreapijava.main.books.controller;

import com.example.bookstoreapijava.main.books.data.dto.BookResponseDTO;
import com.example.bookstoreapijava.main.books.entities.Book;
import com.example.bookstoreapijava.main.books.services.BookstoreService;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/bookstore")
public class BookstoreController {
  @Autowired
  private BookstoreService bookstoreService = new BookstoreService();

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping
  public ResponseEntity<List<Book>> findAllBooks() {
    List<Book> response = bookstoreService.findAllBooks();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Book> getBook(@PathVariable Long id) {
    Book response = bookstoreService.getBook(id);

    return ResponseEntity.ok(response);
  }

  @PostMapping
  public <T> ResponseEntity<BookResponseDTO> insertBook(@RequestBody Book book) throws URISyntaxException {
    Map<String, T> newBook = bookstoreService.insertBook(book);
    URI uri = (URI) newBook.get("uri");
    BookResponseDTO response = (BookResponseDTO) newBook.get("bookResponseDTO");

    return ResponseEntity.created(uri).body(response);
  }

}
