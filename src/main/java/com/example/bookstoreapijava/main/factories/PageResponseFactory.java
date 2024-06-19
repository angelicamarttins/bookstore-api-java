package com.example.bookstoreapijava.main.factories;

import com.example.bookstoreapijava.main.data.dto.response.PageResponse;
import org.springframework.data.domain.Page;

public class PageResponseFactory {

  public static <T> PageResponse<T> toPageResponse(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.hasNext()
    );
  }

}
