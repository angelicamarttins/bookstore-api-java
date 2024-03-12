package com.example.bookstoreapijava.main.book.controller;

import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.services.BookstoreService;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @DeleteMapping("/{bookId}")
  public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
    bookstoreService.deleteBook(bookId);

    return ResponseEntity.noContent().build();
  }

  // Response: 204 No content
  // Faremos um soft delete com inactivatedAt. Caso o isbn feito em um POST for o mesmo de
  // um "deletado", apenas restauraremos esse registro, removendo o inactivatedAt. Isso é possível,
  // pois cada edição tem seu próprio isbn e, se o usuário está reinserindo este livro, significa
  // que ele quer retomar o registro dessa obra em específico. Apenas devemos tomar cuidado no caso
  // de um DELETE e um PATCH com o mesmo isbn.
}
