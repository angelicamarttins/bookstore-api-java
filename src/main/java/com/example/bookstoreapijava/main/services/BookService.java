package com.example.bookstoreapijava.main.services;

import com.example.bookstoreapijava.main.data.dto.request.BookUpdateDtoRequest;
import com.example.bookstoreapijava.main.data.dto.response.PageResponse;
import com.example.bookstoreapijava.main.data.vo.BookCreatedVo;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.factories.PageResponseFactory;
import com.example.bookstoreapijava.main.repositories.BookRepository;
import com.example.bookstoreapijava.main.validators.BookValidator;
import com.example.bookstoreapijava.main.validators.CategoryValidator;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

  @Value("${app.baseUrl}")
  private static String baseUrl;
  private final BookRepository bookRepository;
  private final BookValidator bookValidator;
  private final CategoryValidator categoryValidator;

  public PageResponse<Book> findAllBooks(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Book> activeBooks = bookRepository.findAllActiveBooks(pageable);

    log.info("All books were found");

    return PageResponseFactory.toPageResponse(activeBooks);
  }

  public Book findBook(UUID bookId) {
    Book book = bookValidator.checkIfBookIsFound(bookId);

    log.info("Book found. BookId: {}", bookId);

    return book;
  }

  public BookCreatedVo insertBook(Book book) throws URISyntaxException {
    String bookIsbn = book.getIsbn();
    Optional<Book> maybeBook = bookRepository.findBookByIsbn(bookIsbn);

    if (maybeBook.isPresent()) {
      bookValidator.checkIfBookAlreadyExists(maybeBook.get(), bookIsbn);

      Book reactivatedBook = reactivateBook(maybeBook.get());

      return saveBook(reactivatedBook);
    }

    return createBook(book, bookIsbn);
  }

  public Book updateBook(UUID bookId, BookUpdateDtoRequest updatedBook) throws URISyntaxException {
    Book savedBook = bookValidator.checkIfBookIsFound(bookId);

    if (updatedBook.author() != null) {
      savedBook.setAuthor(updatedBook.author());
    }

    if (updatedBook.title() != null) {
      savedBook.setTitle(updatedBook.title());
    }

    if (updatedBook.isbn() != null) {
      savedBook.setIsbn(updatedBook.isbn());
    }

    if (updatedBook.categoryId() != null) {
      UUID categoryId = updatedBook.categoryId();

      Category category = categoryValidator.checkIfCategoryIsFound(categoryId);

      categoryValidator.checkIfCategoryIsInactive(category);

      savedBook.setCategory(category);
    }

    log.info("All info sent is updated. Will now save book. BookId: {}", bookId);

    if (savedBook.getInactivatedAt() != null) {
      return bookRepository.save(reactivateBook(savedBook));
    }

    savedBook.setUpdatedAt(LocalDateTime.now());

    return bookRepository.save(savedBook);
  }

  public void inactiveBook(UUID bookId) {
    Book inactivatedBook = bookValidator.checkIfBookIsFound(bookId);

    bookValidator.checkIfBookIsInactive(inactivatedBook);

    inactivatedBook.setInactivatedAt(LocalDateTime.now());

    bookRepository.save(inactivatedBook);

    log.info("Book successfully inactivated. BookId: {}", bookId);
  }

  private BookCreatedVo saveBook(Book book) throws URISyntaxException {
    Book savedBook = bookRepository.save(book);

    URI uri = new URI(baseUrl + "/bookstore/" + savedBook.getBookId().toString());

    log.info("Book saved successfully. BookIsbn: {}, BookId: {}",
      book.getIsbn(),
      savedBook.getBookId()
    );

    return new BookCreatedVo(savedBook, uri);
  }

  private Book reactivateBook(Book savedBook) {
    log.info("Book has been inactivated. Will now reactivate it. BookIsbn: {}, BookId: {}",
      savedBook.getIsbn(),
      savedBook.getBookId()
    );

    savedBook.setInactivatedAt(null);
    savedBook.setUpdatedAt(LocalDateTime.now());

    return savedBook;
  }

  private BookCreatedVo createBook(Book book, String bookIsbn) throws URISyntaxException {
    UUID categoryId = book.getCategory().getCategoryId();

    Category category = categoryValidator.checkIfCategoryIsFound(categoryId);

    categoryValidator.checkIfCategoryIsInactive(category);

    log.info(
      "Book has not been created yet and category were found. Will now save book. "
        + "BookIsbn: {}, CategoryId: {}",
      bookIsbn,
      categoryId
    );

    return saveBook(book);
  }

}
