package com.example.bookstoreapijava.main.repositories;

import com.example.bookstoreapijava.main.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

  Optional<Category> getBySanitizedCategoryName(String sanitizedCategoryName);

  @Query("SELECT c FROM Category c WHERE c.inactivatedAt IS NULL")
  Page<Category> findAllActiveCategories(Pageable pageable);

  @Modifying
  @Query("UPDATE Category SET inactivatedAt = NULL " +
      "WHERE categoryId = :categoryId AND inactivatedAt IS NOT NULL")
  void reactivateByCategoryId(UUID categoryId);

}
