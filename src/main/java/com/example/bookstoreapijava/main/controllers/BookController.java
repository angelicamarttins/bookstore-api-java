package com.example.bookstoreapijava.main.controllers;

import com.example.bookstoreapijava.main.data.dto.request.BookUpdateDtoRequest;
import com.example.bookstoreapijava.main.data.dto.response.PageResponse;
import com.example.bookstoreapijava.main.data.vo.BookCreatedVo;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.services.BookService;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/bookstore")
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

  private final CategoryRepository categoryRepository;

  @GetMapping
  public ResponseEntity<PageResponse<Book>> findAllBooks(
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    log.info("Finding all books");

    PageResponse<Book> response = bookService.findAllBooks(page, size);

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

    BookCreatedVo newBook = bookService.insertBook(book);

    Book response = newBook.book();
    URI uri = newBook.uri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{bookId}")
  public ResponseEntity<Book> updateBook(
    @PathVariable UUID bookId,
    @RequestBody @Valid BookUpdateDtoRequest updatedBook
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
