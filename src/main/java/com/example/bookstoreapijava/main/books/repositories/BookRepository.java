package com.example.bookstoreapijava.main.books.repositories;


import com.example.bookstoreapijava.main.books.entities.Book;
import com.example.bookstoreapijava.main.category.entities.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

  @Modifying
  @Transactional
  @Query(
      "UPDATE Book b " +
          "SET b.title = :title, b.author = :author,  b.isbn = :isbn, category = :category " +
          "WHERE b.bookId = :bookId"
  )
  public void updateBookById(
      Long bookId,
      Optional<String> title,
      Optional<String> author,
      Optional<String> isbn,
      Category category);
}
