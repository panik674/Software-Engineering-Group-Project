package uk.comp2211.group13;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static Date string2Date (String date) throws ParseException {
    return sdf.parse(date);
  }

  public static String date2String (Date date) {
    return sdf.format(date);
  }
}
