package com.example.bookstoreapijava.main.books.repositories;


import com.example.bookstoreapijava.main.books.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
