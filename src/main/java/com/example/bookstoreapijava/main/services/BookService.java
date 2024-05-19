package com.example.bookstoreapijava.main.services;

import com.example.bookstoreapijava.main.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.repositories.BookRepository;
import com.example.bookstoreapijava.main.validators.BookValidator;
import com.example.bookstoreapijava.main.validators.CategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

  @Value("${app.baseUrl}")
  private static String baseUrl;
  private final BookRepository bookRepository;
  private final BookValidator bookValidator;
  private final CategoryValidator categoryValidator;

  public List<Book> findAllBooks() {
    log.info("All books were found");

    return bookRepository.findAll();
  }

  public Book findBook(UUID bookId) {
    Book book = bookValidator.checkIfBookIsFound(bookId);

    log.info("Book found. BookId: {}", bookId);

    return book;
  }

  public BookCreatedVO insertBook(Book book) throws URISyntaxException {
    String bookIsbn = book.getIsbn();
    Optional<Book> maybeBook = bookRepository.findBookByIsbn(bookIsbn);

    if (maybeBook.isPresent()) {
      bookValidator.checkIfBookAlreadyExists(maybeBook.get(), bookIsbn);

      Book reactivatedBook = reactivateBook(maybeBook.get());

      return saveBook(reactivatedBook);
    }

    return createBook(book, bookIsbn);
  }

  public Book updateBook(UUID bookId, BookUpdateDTORequest updatedBook) throws URISyntaxException {
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

    return bookRepository.save(savedBook);
  }

  public void inactiveBook(UUID bookId) {
    Book inactivatedBook = bookValidator.checkIfBookIsFound(bookId);

    bookValidator.checkIfBookIsInactive(inactivatedBook);

    inactivatedBook.setInactivatedAt(LocalDateTime.now());

    bookRepository.save(inactivatedBook);

    log.info("Book successfully inactivated. BookId: {}", bookId);
  }

  private BookCreatedVO saveBook(Book book) throws URISyntaxException {
    Book savedBook = bookRepository.save(book);

    URI uri = new URI(baseUrl + "/bookstore/" + savedBook.getBookId().toString());

    log.info("Book saved successfully. BookIsbn: {}, BookId: {}",
        book.getIsbn(),
        savedBook.getBookId()
    );

    return new BookCreatedVO(savedBook, uri);
  }

  private Book reactivateBook(Book savedBook) {
    log.info("Book has been inactivated. Will now reactivate it. BookIsbn: {}, BookId: {}",
        savedBook.getIsbn(),
        savedBook.getBookId()
    );

    savedBook.setInactivatedAt(null);

    return savedBook;
  }

  private BookCreatedVO createBook(Book book, String bookIsbn) throws URISyntaxException {
    UUID categoryId = book.getCategory().getCategoryId();

    Category category = categoryValidator.checkIfCategoryIsFound(categoryId);

    categoryValidator.checkIfCategoryIsInactive(category);

    log.info(
        "Book has not been created yet and category were found. Will now save book. " +
            "BookIsbn: {}, CategoryId: {}",
        bookIsbn,
        categoryId
    );

    return saveBook(book);
  }

}