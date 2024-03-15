package com.example.bookstoreapijava.main.category.data.vo;

import com.example.bookstoreapijava.main.category.entities.Category;

import java.net.URI;

public record CategoryCreatedVO(Category category, URI uri) {}
