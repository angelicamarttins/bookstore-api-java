package com.example.bookstoreapijava.main.books.entities;

import com.example.bookstoreapijava.main.category.entities.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "book")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bookId;
  @NotEmpty(message = "O título do livro não pode estar vazio")
  @Size(min = 1, max = 500, message = "O título do livro deve conter entre 1 e 500 caracteres")
  private String title;

  @NotEmpty(message = "O autor do livro não pode estar vazio")
  @Size(min = 1, max = 500, message = "O nome do autor deve conter entre 1 e 500 caracteres")
  private String author;

//  @NotEmpty(message = "A categoria do livro não pode estar vazia")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  public Book() {

  }

  public Book(String title, String author, Category category) {
    this.title = title;
    this.author = author;
    this.category = category;
  }

  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Category getCategory() {
    return category;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public String toString() {
    return "Book information: \n"
        + "Name: " + title
        + "\n Category: " + category.getCategoryName();
  }
}
