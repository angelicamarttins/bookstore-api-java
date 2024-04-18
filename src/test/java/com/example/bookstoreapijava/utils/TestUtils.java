package com.example.bookstoreapijava.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {
  public static LocalDateTime localDateTimeFormat(LocalDateTime dateTime) {
    DateTimeFormatter localDateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    String localDateTimeFormatted =
        dateTime.format(localDateTimeFormatter).replace(" ", "T");

    return LocalDateTime.parse(localDateTimeFormatted);
  }
}
