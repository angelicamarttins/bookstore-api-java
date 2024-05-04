package com.example.bookstoreapijava.main.book.services;

import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.exceptions.BookAlreadyExistsException;
import com.example.bookstoreapijava.main.book.exceptions.BookNotFoundException;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
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
    UUID categoryId = book.getCategory().getCategoryId();

    bookRepository
        .findBookByIsbn(bookIsbn)
        .ifPresent(savedBook -> {
          throw new BookAlreadyExistsException(bookIsbn);
        });

    Category category = categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new CategoryNotFoundException(categoryId));

    log.info(
        "Book does not exists and category were found. Will now save book. CategoryId: {}",
        categoryId
    );

    book.setCategory(category);

    Book savedBook = bookRepository.save(book);

    URI uri = new URI(baseUrl + "/bookstore/" + savedBook.getBookId().toString());

    log.info("Book saved successfully. BookId: {}", savedBook.getBookId());

    return new BookCreatedVO(savedBook, uri);
  }

  public Book updateBook(UUID bookId, BookUpdateDTORequest updatedBook) {
    Book savedBook = bookRepository
        .findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(bookId));

    log.info("Book exists. Will now update info. BookId: {}", bookId);

    if (updatedBook.author() != null) {
      savedBook.setAuthor(updatedBook.author());
    }

    if (updatedBook.title() != null) {
      savedBook.setTitle(updatedBook.title());
    }

    if (updatedBook.isbn() != null) {
      savedBook.setIsbn(updatedBook.isbn());
    }

    if (updatedBook.category() != null) {
      UUID categoryId = updatedBook.category().getCategoryId();

      categoryRepository
          .findById(categoryId)
          .orElseThrow(() -> {
            log.info(
                "Category not found. Aborting... BookId: {}, CategoryId: {}",
                bookId,
                categoryId
            );

            return new CategoryNotFoundException(categoryId);
          });

      savedBook.setCategory(updatedBook.category());
      // TODO: Não permitir que os dados da categoria sejam alterados.
      //  Deixar apenas alterar o relacionamento entre categoria e livro
      //  Pedir para enviar apenas o ID é uma boa para solucionar essa possibilidade!
    }

    log.info("All info sent is updated. Will now save book. BookId: {}", bookId);

    return bookRepository.save(savedBook);
  }

  public void deleteBook(UUID bookId) {
    Book deletedBook = bookRepository
        .findById(bookId)
        .orElseThrow(() -> {
          log.info("Book not found. Aborting... BookId: {}", bookId);

          return new BookNotFoundException(bookId);
        });

    log.info("Book were found. Will now delete it. BookId: {}", bookId);

    deletedBook.setInactivatedAt(LocalDateTime.now());

    bookRepository.save(deletedBook);

    log.info("Book deleted successfully. BookId: {}", bookId);
  }

}
