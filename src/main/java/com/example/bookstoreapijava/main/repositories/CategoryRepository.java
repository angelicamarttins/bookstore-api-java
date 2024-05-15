package com.example.bookstoreapijava.main.repositories;

import com.example.bookstoreapijava.main.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

  Optional<Category> getByCategoryName(String name);

}
