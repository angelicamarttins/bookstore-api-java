package com.example.bookstoreapijava.main.services;

import com.example.bookstoreapijava.main.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.entities.Book;
import com.example.bookstoreapijava.main.exceptions.BookAlreadyExistsException;
import com.example.bookstoreapijava.main.exceptions.BookNotFoundException;
import com.example.bookstoreapijava.main.repositories.BookRepository;
import com.example.bookstoreapijava.main.entities.Category;
import com.example.bookstoreapijava.main.exceptions.CategoryIsInactiveException;
import com.example.bookstoreapijava.main.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.repositories.CategoryRepository;
import com.example.bookstoreapijava.main.validators.BookValidator;
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

  private final CategoryRepository categoryRepository;

  private final BookValidator bookValidator;

  public List<Book> findAllBooks() {
    log.info("All books were found");

    return bookRepository.findAll();
  }

  public Book findBook(UUID bookId) {
    Book book = bookValidator.searchAndCheckBook(bookId);

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
    Book savedBook = bookValidator.searchAndCheckBook(bookId);


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

      Optional<Category> category = categoryRepository.findById(categoryId);

      checkCategory(category, categoryId);

      savedBook.setCategory(category.get());
    }

    log.info("All info sent is updated. Will now save book. BookId: {}", bookId);

    if (savedBook.getInactivatedAt() != null) {
      reactivateBook(savedBook);
    }

    return bookRepository.save(savedBook);
  }

  public void deleteBook(UUID bookId) {
    Book deletedBook = bookValidator.searchAndCheckBook(bookId);

    deletedBook.setInactivatedAt(LocalDateTime.now());

    bookRepository.save(deletedBook);

    log.info("Book deleted successfully. BookId: {}", bookId);
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

  private Book reactivateBook(Book savedBook) throws URISyntaxException {
    log.info("Book has been inactivated. Will now reactivate it. BookIsbn: {}, BookId: {}",
        savedBook.getIsbn(),
        savedBook.getBookId()
    );

    savedBook.setInactivatedAt(null);

    return savedBook;
  }

  private BookCreatedVO createBook(Book book, String bookIsbn) throws URISyntaxException {
    UUID categoryId = book.getCategory().getCategoryId();

    Optional<Category> maybeCategory = categoryRepository.findById(categoryId);

    checkCategory(maybeCategory, categoryId);

    log.info(
        "Book has not yet been created and category were found. Will now save book. " +
            "BookIsbn: {}, CategoryId: {}",
        bookIsbn,
        categoryId
    );

    return saveBook(book);
  }

  private void checkCategory(Optional<Category> maybeCategory, UUID categoryId) {
    if (maybeCategory.isEmpty()) {
      log.info("Category not found. Aborting... CategoryId: {}", categoryId);

      throw new CategoryNotFoundException(categoryId);
    }

    if (maybeCategory.get().getInactivatedAt() != null) {
      log.info("Category is inactive. CategoryId: {}", categoryId);

      throw new CategoryIsInactiveException(categoryId);
    }
  }

}