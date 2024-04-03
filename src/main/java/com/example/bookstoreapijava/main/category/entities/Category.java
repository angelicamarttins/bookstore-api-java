package com.example.bookstoreapijava.main.category.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

  @Column(name = "category_id")
  @Id
  private UUID categoryId;

  @Column(name = "category_name", unique = true)
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

  public Category(UUID categoryId, String categoryName, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime inactivatedAt) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.inactivatedAt = inactivatedAt;
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
    if (this.categoryId == null) {
      this.categoryId = UUID.randomUUID();
    }

    if (this.createdAt == null) {
      this.createdAt = LocalDateTime.now();
    }
  }

  @PreUpdate
  public void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Category category)) return false;
    return Objects.equals(getCategoryId(), category.getCategoryId()) &&
        Objects.equals(getCategoryName(), category.getCategoryName()) &&
        Objects.equals(getCreatedAt(), category.getCreatedAt()) &&
        Objects.equals(getUpdatedAt(), category.getUpdatedAt()) &&
        Objects.equals(getInactivatedAt(), category.getInactivatedAt());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getCategoryId(),
        getCategoryName(),
        getCreatedAt(),
        getUpdatedAt(),
        getInactivatedAt());
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
