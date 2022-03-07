package uk.comp2211.group13.enums;

import java.util.Calendar;

/**
 * This enum is used to state granularity
 */
public enum Granularity {
  None,
  Second,
  Minute,
  Hour,
  Day;

  /**
   * This is used to return the number of milliseconds in a given Granularity.
   *
   * @return milliseconds
   */
  public int getValue() {
    int out;
    switch (this) {
      case None -> out = 0;
      case Second -> out = 1000;
      case Minute -> out = 1000 * 60;
      case Hour -> out = 1000 * 60 * 60;
      case Day -> out = 1000 * 60 * 60 * 24;
      default -> out = -1;
    }

    return out;
  }

  /**
   * This is used to return the value used by DateUtil to truncate a date
   *
   * @return Calendar value
   */
  public int getCalendar() {
    int out;
    switch (this) {
      case None -> out = 0;
      case Second -> out = Calendar.SECOND;
      case Minute -> out = Calendar.MINUTE;
      case Hour -> out = Calendar.HOUR;
      case Day -> out = Calendar.DAY_OF_MONTH;
      default -> out = -1;
    }

    return out;
  }
}
