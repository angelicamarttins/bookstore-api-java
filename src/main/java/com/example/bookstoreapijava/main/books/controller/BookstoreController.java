package com.example.bookstoreapijava.main.books.controller;

import com.example.bookstoreapijava.main.books.data.dto.BookResponseDTO;
import com.example.bookstoreapijava.main.books.entities.Book;
import com.example.bookstoreapijava.main.books.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/bookstore")
public class BookstoreController {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping
  public List<Book> findAllBooks() {
    return bookRepository.findAll();
  }

  @GetMapping("/{id}")
  public Book getBook(@PathVariable Long id) {
    return bookRepository.getReferenceById(id);
  }

  @PostMapping
  public Book insertBook(@RequestBody Book book) {
    return bookRepository.save(book);
  }

}
