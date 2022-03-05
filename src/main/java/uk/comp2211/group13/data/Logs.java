package uk.comp2211.group13.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.data.log.Click;
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.data.log.Server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Logs {
  private static final Logger logger = LogManager.getLogger(Logs.class);


  public ArrayList<Impression> impressionLogs = new ArrayList<>();
  public ArrayList<Click> clickLogs = new ArrayList<>();
  public ArrayList<Server> serverLogs = new ArrayList<>();

  /**
   * This is used to quickly get the number of clicks
   *
   * @return returns the number of clicks
   */
  public int getClicks() {
    return clickLogs.size();
  }

  /**
   * This is used to quickly get the number of impressions
   *
   * @return returns the number of impressions
   */
  public int getImpressions() {
    return impressionLogs.size();
  }

  /**
   * Getter for bounces. Defined as visiting only one page
   *
   * @return Total number of bounces in logs
   */
  public int getBouncePage() {
    int sum = 0;

    for (Server value : serverLogs) {
      if (value.pages() <= 1) sum++;
    }

    return sum;
  }

  /**
   * Getter for bounces. Defined as staying on the website for less than a minute
   *
   * @return Total number of bounces in logs
   */
  public int getBounceVisit() {
    int sum = 0;

    for (Server value : serverLogs) {
      try {
        long seconds = difDate(value.entryDate(), value.exitDate());
        if (seconds <= 15) sum++;

      } catch (Exception e) {
        logger.error(String.format("Bounce total request failed Reason: %s", e.getMessage()));
      }
    }
    return sum;
  }

  /**
   * Helper function for getBounceVisit() to find difference in seconds
   *
   * @param start start date
   * @param end   end date
   * @return difference in seconds
   */
  private long difDate(String start, String end) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Date d1 = sdf.parse(end);
    Date d2 = sdf.parse(end);
    long difTime = d2.getTime() - d1.getTime();

    return (difTime / (1000));
  }

  /**
   * This is used to count the number of the number of conversions.
   *
   * @return number of conversions
   */
  public int getConversions() {
    int sum = 0;

    for (Server value : serverLogs) {
      if (value.conversion()) sum++;
    }

    return sum;
  }

  /**
   * This is used to count the number of unique clicks in logs
   *
   * @return number of conversions
   */
  public int getUniques() {
    HashSet<String> ids = new HashSet<>();

    for (Click click : clickLogs) {
      ids.add(click.id());
    }

    return ids.size();
  }

  /**
   * This is used to sum the cost of clicks in logs
   *
   * @return total click cost
   */
  public float getClickCost() {
    float sum = 0;

    for (Click click : clickLogs) {
      sum += click.cost();
    }

    return sum;
  }

  /**
   * This is used to sum the cost of impressions in logs
   *
   * @return total impressions cost
   */
  public float getImpressionCost() {
    float sum = 0;

    for (Impression impression : impressionLogs) {
      sum += impression.cost();
    }

    return sum;
  }
}
