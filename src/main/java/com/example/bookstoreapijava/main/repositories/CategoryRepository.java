package com.example.bookstoreapijava.main.repositories;

import com.example.bookstoreapijava.main.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

  Optional<Category> getBySanitizedCategoryName(String sanitizedCategoryName);

  @Modifying
  @Query("UPDATE Category SET inactivatedAt = NULL " +
      "WHERE categoryId = :categoryId AND inactivatedAt IS NOT NULL")
  void reactivateByCategoryId(UUID categoryId);

}
