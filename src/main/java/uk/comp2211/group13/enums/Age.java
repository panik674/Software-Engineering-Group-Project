package uk.comp2211.group13.enums;

/**
 * This enum is used to identify the age of a user, in the impression log
 */
public enum Age {
  L25, // <25
  L34, // 25-34
  L44, // 35-44
  L54, // 45-54
  G54;  // >54

  /**
   * This is used to convert a string into an age.
   *
   * @param value string age
   * @return enum age
   */
  public static Age stringToAge(String value) {
    switch (value) {
      case "<25" -> {
        return L25;
      }
      case "25-34" -> {
        return L34;
      }
      case "35-44" -> {
        return L44;
      }
      case "45-54" -> {
        return L54;
      }
      case ">54" -> {
        return G54;
      }
      default -> {
        return null;
      }
    }
  }
}
