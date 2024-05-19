package com.example.bookstoreapijava.main.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class Utils {

  public static String sanitizeStringField(String field) {
    String accentsRemoved = removeAccents(field);

    String capitalizedField = capitalizeString(accentsRemoved.trim());

    return capitalizedField;
  }

  public static String removeAccents(String input) {
    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    return pattern.matcher(normalized).replaceAll("");
  }

  public static String capitalizeString(String input) {
    return input.toUpperCase();
  }

}
