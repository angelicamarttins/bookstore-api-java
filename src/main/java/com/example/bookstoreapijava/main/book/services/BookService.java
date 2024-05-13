package com.example.bookstoreapijava.main.book.services;

import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.exceptions.BookAlreadyExistsException;
import com.example.bookstoreapijava.main.book.exceptions.BookNotFoundException;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.exceptions.CategoryIsInactiveException;
import com.example.bookstoreapijava.main.category.exceptions.CategoryNotFoundException;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BookService {

  @Value("${app.baseUrl}")
  private static String baseUrl;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Book> findAllBooks() {
    log.info("All books were found");

    return bookRepository.findAll();
  }

  public Book findBook(UUID bookId) {
    Book book = bookRepository
        .findById(bookId)
        .orElseThrow(() -> {
          log.info("Book not found. Aborting... BookId: {}", bookId);

          return new BookNotFoundException(bookId);
        });

    log.info("Book found. BookId: {}", bookId);

    return book;
  }

  public BookCreatedVO insertBook(Book book) throws URISyntaxException {
    String bookIsbn = book.getIsbn();
    Optional<Book> maybeBook = bookRepository.findBookByIsbn(bookIsbn);

    if (maybeBook.isPresent()) {
      checkIfBookAlreadyExists(maybeBook.get(), bookIsbn);

      Book reactivatedBook = reactivateBook(maybeBook.get());

      return saveBook(reactivatedBook);
    }

    return createBook(book, bookIsbn);
  }

  public Book updateBook(UUID bookId, BookUpdateDTORequest updatedBook) throws URISyntaxException {
    Book savedBook = bookRepository
        .findById(bookId)
        .orElseThrow(() -> {
          log.info("Book not found. BookId: {}", bookId);

          return new BookNotFoundException(bookId);
        });


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
    Book deletedBook = bookRepository
        .findById(bookId)
        .orElseThrow(() -> {
          log.info("Book not found. Aborting... BookId: {}", bookId);

          return new BookNotFoundException(bookId);
        });

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

  private void checkIfBookAlreadyExists(Book savedBook, String bookIsbn) {
    if (savedBook.getInactivatedAt() == null) {
      log.info("Book already exists. Aborting... BookIsbn: {}, BookId: {}",
          savedBook.getIsbn(),
          savedBook.getBookId());

      throw new BookAlreadyExistsException(bookIsbn);
    }
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