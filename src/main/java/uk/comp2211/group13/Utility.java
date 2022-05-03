package uk.comp2211.group13;

import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This is a class containing utility functions, such as date parsing and validation functions.
 */
public class Utility {
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final SimpleDateFormat sdf2 = new SimpleDateFormat("E dd/MM/yy HH:ss");

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
   * Converts a Date into a fancy string
   *
   * @param date Date
   * @return string date
   */
  public static String date2StringGraphing(Date date) {
    return sdf2.format(date);
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

  public static Metric getMetric(String metric) {
    return switch (metric){
      case "Number of Clicks" -> Metric.Clicks;
      case "Number of Impressions" -> Metric.Impressions;
      case "Number of Uniques" -> Metric.Unique;
      case "Number of Bounce Pages" -> Metric.BouncePage;
      case "Number of Bounce Visits" -> Metric.BounceVisit;
      case "Rate of Conversions" -> Metric.Conversions;
      case "Total Costs" -> Metric.TotalCost;
      case "CTR" -> Metric.CTR;
      case "CPA" -> Metric.CPA;
      case "CPC" -> Metric.CPC;
      case "CPM" -> Metric.CPM;
      case "Bounce Visit Rate" -> Metric.BounceRateVisit;
      case "Bounce Page Rate" -> Metric.BounceRatePage;
      default -> Metric.Clicks;
    };
  }

  /**
   * Method to check filter type
   *
   * @param filt - Filter to be type-checked
   * @return - Corresponding filter type
   */
  public Filter filterType(String filt){
    switch (filt){

      case "Male", "Female" : return Filter.Gender;
      case "<25", "25-34", "35-44", "45-54", ">54" : return Filter.Age;
      case "Low", "Medium", "High" : return Filter.Income;
      case "News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel" : return Filter.Context;
    }
    return null;
  }

}
