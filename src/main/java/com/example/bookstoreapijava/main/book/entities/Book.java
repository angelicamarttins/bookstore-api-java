package com.example.bookstoreapijava.main.book.entities;

import com.example.bookstoreapijava.main.category.entities.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "book")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {

  @Column(name = "book_id")
  @Id
  @UuidGenerator
  private UUID bookId;

  @Column(name = "title")
  @NotNull
  @NotEmpty(message = "O título do livro não pode estar vazio")
  @Size(min = 1, max = 500, message = "O título do livro deve conter entre 1 e 500 caracteres")
  private String title;

  @Column(name = "author")
  @NotNull
  @NotEmpty(message = "O autor do livro não pode estar vazio")
  @Size(min = 1, max = 500, message = "O nome do autor deve conter entre 1 e 500 caracteres")
  private String author;

  @Column(unique = true, name = "isbn")
  @NotNull
  @NotEmpty
  @Size(max = 13)
  private String isbn;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "inactivated_at")
  private LocalDateTime inactivatedAt;

  //  @NotEmpty(message = "A categoria do livro não pode estar vazia")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  public Book() {
  }

  public Book(String title, String author, String isbn, Category category, LocalDateTime createdAt) {
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.category = category;
    this.createdAt = createdAt;
  }

  public UUID getBookId() {
    return bookId;
  }

  public void setBookId(UUID bookId) {
    this.bookId = bookId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getInactivatedAt() {
    return inactivatedAt;
  }

  public void setInactivatedAt(LocalDateTime inactivatedAt) {
    this.inactivatedAt = inactivatedAt;
  }

  @PrePersist
  private void onCreate() {
    this.setCreatedAt(LocalDateTime.now());
  }

  @PreUpdate
  private void onUpdate() {
    this.setUpdatedAt(LocalDateTime.now());
  }

  @Override
  public String toString() {
    return "Book{" +
        "bookId=" + bookId +
        ", title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", isbn='" + isbn + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", inactivatedAt=" + inactivatedAt +
        ", category=" + category +
        '}';
  }
}
