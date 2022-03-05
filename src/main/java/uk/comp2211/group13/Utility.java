package uk.comp2211.group13;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
  public static boolean validateDate(String date){
    try {
      sdf.parse(date);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }
}
