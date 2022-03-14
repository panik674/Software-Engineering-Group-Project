package uk.comp2211.group13.enums;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;

/**
 * This enum is used to state granularity
 */
public enum Granularity {
  None,
  Second,
  Minute,
  Hour,
  Day,
  Month,
  Year;

  /**
   * This is used to return the value used by DateUtil to truncate a date
   *
   * @return Calendar value
   */
  public int getCalendar() {
    return switch (this) {
      case None -> 0;
      case Second -> Calendar.SECOND;
      case Minute -> Calendar.MINUTE;
      case Hour -> Calendar.HOUR;
      case Day -> Calendar.DAY_OF_MONTH;
      case Month -> Calendar.MONTH;
      case Year -> Calendar.YEAR;
    };
  }
}
