package uk.comp2211.group13;

import uk.comp2211.group13.enums.Granularity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * This is a class containing utility functions, such as date parsing and validation functions.
 */
public class Utility {
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * Converts a string into a Date
   *
   * @param date string date
   * @return Date
   * @throws ParseException Invalid string date
   */
  public static Date string2Date(String date) throws ParseException {
    return sdf.parse(date);
  }

  /**
   * Converts a Date into a string
   *
   * @param date Date
   * @return string date
   */
  public static String date2String(Date date) {
    return sdf.format(date);
  }

  /**
   * This is used to validate if a string is in the correct format to be parsed.
   *
   * @param date string date to check
   * @return true if valid
   */
  public static boolean validateDate(String date) {
    try {
      sdf.parse(date);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }


  /**
   * These are sued to hold the string values for filters and granularity for the UI to easily use.
   */
  public static final String[] getGenders = new String[]{"Male", "Female"};
  public static final String[] getAges = new String[]{"<25", "25-34", "35-44", "45-54", ">54"};
  public static final String[] getIncomes = new String[]{"Low", "Medium", "High"};
  public static final String[] getContexts = new String[]{"News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel"};
  public static final String[] getGranularityString = new String[]{"None", "Second", "Minute", "Hour", "Day", "Month", "Year"};

  /**
   * This is used to return a valid gender or null.
   *
   * @param gender string to check
   * @return gender or null
   */
  public static String validateGender(String gender) throws Exception {
    return switch (gender) {
      case "Male", "Female" -> gender;
      default -> throw new Exception("Invalid data");
    };
  }

  /**
   * This is used to return a valid age or null.
   *
   * @param age string to check
   * @return age or null
   */
  public static String validateAge(String age) throws Exception {
    return switch (age) {
      case "<25", "25-34", "35-44", "45-54", ">54" -> age;
      default -> throw new Exception("Invalid data");
    };
  }

  /**
   * This is used to return a valid income or null.
   *
   * @param income string to check
   * @return income or null
   */
  public static String validateIncome(String income) throws Exception {
    return switch (income) {
      case "Low", "Medium", "High" -> income;
      default -> throw new Exception("Invalid data");
    };
  }

  /**
   * This is used to return a valid context or null.
   *
   * @param context string to check
   * @return context or null
   */
  public static String validateContext(String context) throws Exception {
    return switch (context) {
      case "News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel" -> context;
      default -> throw new Exception("Invalid data");
    };
  }

  /**
   * This is used to convert a string of granularity to a enum value for backend use
   *
   * @param granularity string ui granularity
   * @return Granularity enum
   */
  public static Granularity getGranularity(String granularity) {
    return switch (granularity){
      case "Second" -> Granularity.Second;
      case "Minute" -> Granularity.Minute;
      case "Hour" -> Granularity.Hour;
      case "Day" -> Granularity.Day;
      case "Month" -> Granularity.Month;
      case "Year" -> Granularity.Year;
      default -> Granularity.None;
    };
  }
}
