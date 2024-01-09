package com.example.bookstoreapijava.main.category.repositories;

import com.example.bookstoreapijava.main.category.entities.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Modifying
  @Transactional
  @Query("UPDATE Category c SET c.categoryName = :categoryName WHERE c.categoryId = :categoryId")
  void updateCategoryById(@Param("categoryName") String categoryName, @Param("categoryId") Long categoryId);
}
