package com.example.bookstoreapijava.main.controllers;

import com.example.bookstoreapijava.main.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.services.BookService;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/bookstore")
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

  private final CategoryRepository categoryRepository;

  @GetMapping
  public ResponseEntity<List<Book>> findAllBooks() {
    log.info("Finding all books");

    List<Book> response = bookService.findAllBooks();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{bookId}")
  public ResponseEntity<Book> getBook(@PathVariable UUID bookId) {
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
  public ResponseEntity<Void> deleteBook(@PathVariable UUID bookId) {
    log.info("Deleting book. BookId: {}", bookId);

    bookService.deleteBook(bookId);

    return ResponseEntity.noContent().build();
  }

  /*
  TODO: Response: 204 No content
    Faremos um soft delete com inactivatedAt. Caso o isbn feito em um POST for o mesmo de
    um "deletado", apenas restauraremos esse registro, removendo o inactivatedAt. Isso é possível,
    pois cada edição tem seu próprio isbn e, se o usuário está reinserindo este livro, significa
    que ele quer retomar o registro dessa obra em específico. Apenas devemos tomar cuidado no caso
    de um DELETE e um PATCH com o mesmo isbn.
   */
}