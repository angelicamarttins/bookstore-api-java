package com.example.bookstoreapijava.main.controllers;

import com.example.bookstoreapijava.main.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.services.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/bookstore")
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

  private final CategoryRepository categoryRepository;

  @GetMapping
  public ResponseEntity<Page<Book>> findAllBooks(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    log.info("Finding all books");

    Page<Book> response = bookService.findAllBooks(page, size) ;

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{bookId}")
  public ResponseEntity<Book> findBook(@PathVariable UUID bookId) {
    log.info("Finding book. BookId: {}", bookId);

    Book response = bookService.findBook(bookId);

    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<Book> insertBook(@RequestBody @Valid Book book) throws URISyntaxException {
    log.info("Creating book. BookIsbn: {}", book.getIsbn());

    BookCreatedVO newBook = bookService.insertBook(book);

    Book response = newBook.book();
    URI uri = newBook.uri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{bookId}")
  public ResponseEntity<Book> updateBook(
      @PathVariable UUID bookId,
      @RequestBody @Valid BookUpdateDTORequest updatedBook
  ) throws URISyntaxException {
    log.info("Updating book. BookId: {}", bookId);

    Book response = bookService.updateBook(bookId, updatedBook);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{bookId}")
  public ResponseEntity<Void> inactiveBook(@PathVariable UUID bookId) {
    log.info("Inactivating book. BookId: {}", bookId);

    bookService.inactiveBook(bookId);

    return ResponseEntity.noContent().build();
  }

}
