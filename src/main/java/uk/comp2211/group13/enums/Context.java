package uk.comp2211.group13.enums;

/**
 * This enum is used to identify an impression's context, in the impression log
 */
public enum Context {
  News,
  Shopping,
  SocialMedia,
  Blog,
  Hobbies,
  Travel;

  /**
   * This is used to convert a string into a context.
   *
   * @param value string context
   * @return enum context
   */
  public static Context stringToContext(String value) {
    switch (value) {
      case "News" -> {
        return News;
      }
      case "Shopping" -> {
        return Shopping;
      }
      case "Social Media" -> {
        return SocialMedia;
      }
      case "Blog" -> {
        return Blog;
      }
      case "Hobbies" -> {
        return Hobbies;
      }
      case "Travel" -> {
        return Travel;
      }
      default -> {
        return null;
      }
    }
  }
}
