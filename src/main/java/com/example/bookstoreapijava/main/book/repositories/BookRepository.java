package com.example.bookstoreapijava.main.book.repositories;


import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.category.entities.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

  @Modifying
  @Transactional
  @Query(
      "UPDATE Book b " +
          "SET b.title = :title, b.author = :author,  b.isbn = :isbn, category = :category " +
          "WHERE b.bookId = :bookId"
  )
  void updateBookById(
      @Param("title") String title,
      @Param("author") String author,
      @Param("isbn") String isbn,
      @Param("category") Category category,
      @Param("bookId") Long bookId
  );
}
