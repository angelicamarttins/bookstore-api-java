package com.example.bookstoreapijava.unit.utils;

import static com.example.bookstoreapijava.main.utils.Utils.capitalizeString;
import static com.example.bookstoreapijava.main.utils.Utils.removeAccents;
import static com.example.bookstoreapijava.main.utils.Utils.sanitizeStringField;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  @DisplayName("When field has no accents and blank spaces, returns the same string correctly")
  public void doNothingWhenSanitize() {
    String field = "Filosofia";
    String expectedField = "FILOSOFIA";
    String actualField = sanitizeStringField(field);

    Assertions.assertEquals(expectedField, actualField);
  }

  @Test
  @DisplayName("When field has accents and blank spaces, returns sanitized correctly")
  public void sanitizeCorrectly() {
    String field = "   Filosofiá   ";
    String expectedField = "FILOSOFIA";

    String actualField = sanitizeStringField(field);

    Assertions.assertEquals(expectedField, actualField);
  }

  @Test
  @DisplayName("When field is lower case, returns capitalized correctly")
  public void capitalizeCorrectly() {
    String field = "filosofia";
    String expectedField = "FILOSOFIA";

    String actualField = capitalizeString(field);

    Assertions.assertEquals(expectedField, actualField);
  }

  @Test
  @DisplayName("When field has accents, returns remove string without accents correctly")
  public void removeAccentsCorrectly() {
    String field = "Fìlõsôfíä";
    String expectedField = "Filosofia";

    String actualField = removeAccents(field);

    Assertions.assertEquals(expectedField, actualField);
  }

}
