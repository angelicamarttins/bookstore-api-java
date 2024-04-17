package com.example.bookstoreapijava.main.category.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

  @Column(name = "category_id")
  @Id
  private UUID categoryId;

  @Column(name = "category_name", unique = true)
  @NonNull
  @NotNull(message = "O nome da categoria não pode ser nulo")
  @NotEmpty(message = "O nome da categoria não pode estar vazio")
  @Size(min = 1, max = 500, message = "O nome da categoria deve conter entre 1 e 500 caracteres")
  private String categoryName;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "inactivated_at")
  private LocalDateTime inactivatedAt;

  public Category(
      @JsonProperty("categoryId") UUID categoryId,
      @JsonProperty("categoryName") String categoryName
  ) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
  }

  @PrePersist
  public void onCreate() {
    if (this.categoryId == null) {
      this.categoryId = UUID.randomUUID();
    }

    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA" + createdAt);

    if (this.createdAt == null) {
      this.createdAt = LocalDateTime.now();
    }
  }

  @PreUpdate
  public void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

}
