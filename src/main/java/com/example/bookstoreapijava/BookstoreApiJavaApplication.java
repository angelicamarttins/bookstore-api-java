package com.example.bookstoreapijava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;


@EnableKafka
@SpringBootApplication
public class BookstoreApiJavaApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookstoreApiJavaApplication.class, args);
  }

}
