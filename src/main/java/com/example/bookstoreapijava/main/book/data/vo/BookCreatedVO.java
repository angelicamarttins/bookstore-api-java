package com.example.bookstoreapijava.main.book.data.vo;

import com.example.bookstoreapijava.main.book.entities.Book;

import java.net.URI;

public record BookCreatedVO(Book book, URI uri) {}
