package com.example.bookstoreapijava.main.book.repositories;


import com.example.bookstoreapijava.main.book.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

  Optional<Book> getBookByIsbn(String isbn);
}
