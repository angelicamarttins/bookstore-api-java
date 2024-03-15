package com.example.bookstoreapijava.main.book.services;

import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookstoreService {
  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Book> findAllBooks() {
    return bookRepository.findAll();
  }

  public Book getBook(Long id) {
    return bookRepository.getReferenceById(id);
  }

  public BookCreatedVO insertBook(Book book) throws URISyntaxException {
    Category category = categoryRepository.getReferenceById(book.getCategory().getCategoryId());
    book.setCategory(category);

    Book savedBook = bookRepository.save(book);


    URI uri = new URI("http://localhost:8080/bookstore/" + savedBook.getBookId().toString());

    return new BookCreatedVO(book, uri);
  }

  public Book updateBook(Long id, BookUpdateDTORequest updatedBook) {
    Book savedBook = bookRepository.getReferenceById(id);

    if(updatedBook.author() != null) {
      savedBook.setAuthor(updatedBook.author());
    }

    if(updatedBook.title() != null) {
      savedBook.setTitle(updatedBook.title());
    }

    if(updatedBook.isbn() != null) {
      savedBook.setIsbn(updatedBook.isbn());
    }

    if(updatedBook.category() != null) {
      savedBook.setCategory(updatedBook.category()); // TODO: Não permitir que os dados da categoria sejam alterados. Deixar apenas alterar o relacionamento entre categoria e livro
    }

    return bookRepository.save(savedBook);
  }

  public void deleteBook(Long bookId) {
    Book deletedBook = bookRepository.getReferenceById(bookId);

    deletedBook.setInactivatedAt(LocalDateTime.now());

    bookRepository.save(deletedBook);

  }

}
