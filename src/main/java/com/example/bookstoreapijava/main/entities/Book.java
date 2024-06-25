package com.example.bookstoreapijava.main.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Table(name = "book")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {

  @Column(name = "book_id")
  @Id
  private UUID bookId;

  @Column(name = "title")
  @NotNull(message = "O título do livro não pode ser nulo")
  @NotEmpty(message = "O título do livro não pode estar vazio")
  @Size(min = 1, max = 500, message = "O título do livro deve conter entre 1 e 500 caracteres")
  @NonNull
  private String title;

  @Column(name = "author")
  @NotNull(message = "O autor do livro não pode ser nulo")
  @NotEmpty(message = "O autor do livro não pode estar vazio")
  @Size(min = 1, max = 500, message = "O nome do autor deve conter entre 1 e 500 caracteres")
  @NonNull
  private String author;

  @Column(unique = true, name = "isbn")
  @NotNull(message = "O isbn do livro não pode ser nulo")
  @NotEmpty(message = "O isbn do livro não pode estar vazio")
  @Size(min = 10, max = 13, message = "Isbn must be between 10 and 13 characters")
  @NonNull
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

  @PrePersist
  private void onCreate() {
    if (this.bookId == null) {
      this.setBookId(UUID.randomUUID());
    }

    if (this.createdAt == null) {
      this.setCreatedAt(LocalDateTime.now());
    }
  }

  @PreUpdate
  private void onUpdate() {
    this.setUpdatedAt(LocalDateTime.now());
  }

}
