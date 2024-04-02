package com.example.bookstoreapijava.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainersBase {

  private static final PostgreSQLContainer POSTGRES_CONTAINER;

  static {
    POSTGRES_CONTAINER =
    new PostgreSQLContainer<>("postgres:alpine");

    POSTGRES_CONTAINER.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
  }
}
