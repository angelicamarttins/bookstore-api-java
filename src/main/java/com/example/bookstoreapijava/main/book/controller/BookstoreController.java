package com.example.bookstoreapijava.main.book.controller;

import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.services.BookstoreService;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

  @GetMapping("/{bookId}")
  public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
    Book response = bookstoreService.getBook(bookId);

    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<Book> insertBook(@RequestBody Book book) throws URISyntaxException {
    BookCreatedVO newBook = bookstoreService.insertBook(book);

    Book response = newBook.getBook();
    URI uri = newBook.getUri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{bookId}")
  public ResponseEntity<Book> updateBook(
      @PathVariable Long bookId,
      @RequestBody BookUpdateDTORequest updatedBook
  ) {
    Book response = bookstoreService.updateBook(bookId, updatedBook);

    return ResponseEntity.ok(response);
  }

}
