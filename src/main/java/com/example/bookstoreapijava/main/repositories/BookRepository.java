package com.example.bookstoreapijava.main.repositories;


import com.example.bookstoreapijava.main.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

  Optional<Book> findBookByIsbn(String isbn);

  @Query("SELECT b FROM Book b WHERE b.inactivatedAt IS NULL")
  Page<Book> findAllActiveBooks(Pageable pageable);

}
