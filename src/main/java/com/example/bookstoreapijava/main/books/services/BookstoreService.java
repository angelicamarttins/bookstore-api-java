package com.example.bookstoreapijava.main.books.services;

import com.example.bookstoreapijava.main.books.data.dto.BookResponseDTO;
import com.example.bookstoreapijava.main.books.entities.Book;
import com.example.bookstoreapijava.main.books.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public BookResponseDTO insertBook(Book book) {
    Book newBook = bookRepository.save(book);
    Category category = categoryRepository.getReferenceById(newBook.getCategory().getCategoryId());

    return new BookResponseDTO(newBook.getTitle(), newBook.getAuthor(), category);
  }
}
