package com.example.bookstoreapijava.main.category.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

  @Id
  @Column(name = "category_id")
  @UuidGenerator
  private UUID categoryId;
  @NotEmpty(message = "O nome da categoria não pode estar vazio")
  @Size(min = 1, max = 500, message = "O nome da categoria deve conter entre 1 e 500 caracteres")
  @Column(name = "category_name", unique = true)
  private String categoryName;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "inactivated_at")
  private LocalDateTime inactivatedAt;

  @JsonCreator
  public Category() {
  }

  public Category(
      @JsonProperty("categoryId") UUID categoryId,
      @JsonProperty("categoryName") String categoryName
  ) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
  }

  public UUID getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(UUID categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
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
  public void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  public void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public String toString() {
    return "Category{" +
        "categoryId=" + categoryId +
        ", categoryName='" + categoryName + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", inactivatedAt=" + inactivatedAt +
        '}';
  }
}
