package com.example.bookstoreapijava.config;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostgresTestContainersBase {

  @Container
  protected static final PostgreSQLContainer POSTGRES_CONTAINER;

  static {
    POSTGRES_CONTAINER =
        new PostgreSQLContainer<>("postgres:alpine");

    POSTGRES_CONTAINER.start();
  }

  @LocalServerPort
  protected int port;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
  }
}
