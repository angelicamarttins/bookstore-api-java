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

  @Test
  @DisplayName(value = "Should return the capitalized when field is lower case")
  public void should_returnEquals_when_isLowerCase() {
    String field = "filosofia";
    String expectedField = "Filosofia";

    String actualField = Utils.capitalizeString(field);

    Assertions.assertEquals(expectedField, actualField);
  }

  @Test
  @DisplayName(value = "Should return the capitalized when field is upper case")
  public void should_returnEquals_when_isUppercase() {
    String field = "FILOSOFIA";
    String expectedField = "Filosofia";

    String actualField = Utils.capitalizeString(field);

    Assertions.assertEquals(expectedField, actualField);
  }

  @Test
  @DisplayName(value = "Should remove accents when field has accents")
  public void should_returnEquals_whenHasAccents() {
    String field = "Fìlõsôfíä";
    String expectedField = "Filosofia";

    String actualField = Utils.removeAccents(field);

    Assertions.assertEquals(expectedField, actualField);
  }

}
