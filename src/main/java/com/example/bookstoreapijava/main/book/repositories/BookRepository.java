package com.example.bookstoreapijava.main.book.repositories;


import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.category.entities.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

  Optional<Book> getBookByIsbn(String isbn);
}
