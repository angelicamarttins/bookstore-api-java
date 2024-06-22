package com.example.bookstoreapijava.main.data.vo;

import com.example.bookstoreapijava.main.entities.Book;

import java.net.URI;

public record BookCreatedVO(Book book, URI uri) {
}
