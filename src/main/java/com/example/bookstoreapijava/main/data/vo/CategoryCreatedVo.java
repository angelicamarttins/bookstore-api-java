package com.example.bookstoreapijava.main.data.vo;

import com.example.bookstoreapijava.main.entities.Category;
import java.net.URI;

public record CategoryCreatedVo(Category category, URI uri) {
}
