package com.example.bookstoreapijava.main.category.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long categoryId;
  @NotEmpty(message = "O nome da categoria não pode estar vazio")
  @Size(min = 1, max = 500, message = "O nome da categoria deve conter entre 1 e 500 caracteres")
  @Column(name = "category_name")
  private String categoryName;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @JsonCreator
  public Category() {
  }

  public Category(
      @JsonProperty("categoryId") Long categoryId,
      @JsonProperty("categoryName") String categoryName
  ) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  @Override
  public String toString() {
    return "Book category: " + categoryName;
  }
}
