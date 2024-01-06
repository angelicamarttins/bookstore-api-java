package com.example.bookstoreapijava.main.category.data.vo;

import com.example.bookstoreapijava.main.category.entities.Category;

import java.net.URI;

public class CategoryCreatedVO {
  private Category category;
  private URI uri;

  public CategoryCreatedVO(Category category, URI uri) {
    this.category = category;
    this.uri = uri;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public URI getUri() {
    return uri;
  }

  public void setUri(URI uri) {
    this.uri = uri;
  }
}
