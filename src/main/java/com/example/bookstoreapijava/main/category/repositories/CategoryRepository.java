package com.example.bookstoreapijava.main.category.repositories;

import com.example.bookstoreapijava.main.category.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
