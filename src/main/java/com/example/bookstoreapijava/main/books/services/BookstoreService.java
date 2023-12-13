package com.example.bookstoreapijava.main.books.services;

import com.example.bookstoreapijava.main.books.data.dto.BookResponseDTO;
import com.example.bookstoreapijava.main.books.entities.Book;
import com.example.bookstoreapijava.main.books.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public <T> Map<String, T> insertBook(Book book) throws URISyntaxException {
    Book savedBook = bookRepository.save(book);
    Category category = categoryRepository.getReferenceById(savedBook.getCategory().getCategoryId());
    URI uri = new URI("http://localhost:8080/bookstore/" + savedBook.getBookId().toString());

    HashMap<String, T> response = new HashMap<>();
    response.put(
        "bookResponseDTO",
        (T) new BookResponseDTO(savedBook.getTitle(), savedBook.getAuthor(), category
        ));
    response.put("uri", (T) uri);

    return response;
  }

}
