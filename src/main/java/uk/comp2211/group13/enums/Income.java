package uk.comp2211.group13.enums;

/**
 * This enum is used to identify a user's income, in the impression log
 */
public enum Income {
  Low,
  Medium,
  High;

  /**
   * This is used to convert a string into an income.
   *
   * @param value string income
   * @return enum income
   */
  public static Income stringToIncome(String value) {
    switch (value) {
      case "Low" -> {
        return Low;
      }
      case "Medium" -> {
        return Medium;
      }
      case "High" -> {
        return High;
      }
      default -> {
        return null;
      }
    }
  }
}
