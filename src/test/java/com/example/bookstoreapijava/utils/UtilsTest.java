package com.example.bookstoreapijava.utils;

import com.example.bookstoreapijava.main.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  @DisplayName(value = "Should return the same string when field has no accents and blank spaces")
  public void should_returnEquals_when_thereIsNoAccentAndBlankSpaces() {
    String field = "Filosofia";

    String actualField = Utils.sanitizeStringField(field);

    Assertions.assertEquals(field, actualField);
  }

  @Test
  @DisplayName(value = "Should return the sanitized string when field has accents and blank spaces")
  public void should_returnEquals_when_thereIsAccentAndBlankSpaces() {
    String field = "   Filosofiá   ";
    String expectedField = "Filosofia";

    String actualField = Utils.sanitizeStringField(field);

    Assertions.assertEquals(expectedField, actualField);
  }

}
