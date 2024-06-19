package com.example.bookstoreapijava.main.data.dto.response;


import java.util.List;

public record PageResponse<T>(List<T> content, Boolean hasNextPage) {
}
