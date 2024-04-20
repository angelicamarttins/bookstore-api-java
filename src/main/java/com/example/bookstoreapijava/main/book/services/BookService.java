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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {
  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Book> findAllBooks() {
    return bookRepository.findAll();
  }

  public Book findBook(UUID bookId) {

    return bookRepository
        .findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(bookId));
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

    book.setCategory(category);

    Book savedBook = bookRepository.save(book);

    URI uri = new URI("http://localhost:8080/bookstore/" + savedBook.getBookId().toString());

    return new BookCreatedVO(savedBook, uri);
  }

  public Book updateBook(UUID bookId, BookUpdateDTORequest updatedBook) {
    Book savedBook = bookRepository
        .findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(bookId));

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
      savedBook.setCategory(updatedBook.category());
      // TODO: Não permitir que os dados da categoria sejam alterados.
      //  Deixar apenas alterar o relacionamento entre categoria e livro
    }

    return bookRepository.save(savedBook);
  }

  public void deleteBook(UUID bookId) {
    Book deletedBook = bookRepository
        .findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(bookId));

    deletedBook.setInactivatedAt(LocalDateTime.now());

    bookRepository.save(deletedBook);

  }

}
