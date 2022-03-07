package uk.comp2211.group13.data;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.data.log.Click;
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.data.log.Server;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This is used to calculate and retrieve the requested metrics.
 * Will expand to version with selected intervals.
 */

public class Metrics {

  private static final Logger logger = LogManager.getLogger(Metrics.class); // TODO: Check if logger is needed?

  /**
   * This stores the data class given by the AppWindow
   */
  private final Data data;

  //TODO: must look into which Data object we'll be referring to. I think it should be referenced from the one
  // determined by the UI

  /**
   * Class constructor. Initialises the Data variable, with the one specified form the user
   *
   * @param data data object
   */
  public Metrics(Data data) {
    this.data = data;
  }

  /**
   * This can be used to request metric data.
   *
   * @param metric metric to request
   * @return metric data
   */
  //TODO: Add increment granularity in Sprint 2.
  public HashMap<Date, Float> request(Metric metric, String startDate, String endDate, Granularity granularity) {
    HashMap<Date, Float> table = new HashMap<>();

    // Get timeLogs
    HashMap<Filter, String> filters = new HashMap<>();
    filters.put(Filter.StartDatetime, startDate);
    filters.put(Filter.EndDatetime, endDate);
    Logs masterLog = data.request(filters);

    if (granularity != Granularity.None) {
      HashMap<Date, Logs> timeLogs = getGranularity(masterLog, granularity);

      for (Map.Entry<Date, Logs> timeLog : timeLogs.entrySet()) {
        switch (metric) {
          case Impressions -> table.put(timeLog.getKey(), (float) impressions(timeLog.getValue()));
          case Clicks -> table.put(timeLog.getKey(), (float) clicks(timeLog.getValue()));
          case Unique -> table.put(timeLog.getKey(), (float) uniques(timeLog.getValue()));
          case BouncePage -> table.put(timeLog.getKey(), (float) bouncePage(timeLog.getValue()));
          case BounceVisit -> table.put(timeLog.getKey(), (float) bounceVisit(timeLog.getValue()));
          case Conversions -> table.put(timeLog.getKey(), conversionRate(timeLog.getValue()));
          case TotalCost -> table.put(timeLog.getKey(), totalCost(timeLog.getValue()));
          case ClickCost -> table.put(timeLog.getKey(), timeLog.getValue().getClickCost());
          case CTR -> table.put(timeLog.getKey(), clickRate(timeLog.getValue()));
          case CPA -> table.put(timeLog.getKey(), costAcquisition(timeLog.getValue()));
          case CPC -> table.put(timeLog.getKey(), costClick(timeLog.getValue()));
          case CPM -> table.put(timeLog.getKey(), costThousand(timeLog.getValue()));
          case BounceRatePage -> table.put(timeLog.getKey(), bounceRatePage(timeLog.getValue()));
          case BounceRateVisit -> table.put(timeLog.getKey(), bounceRateVisit(timeLog.getValue()));
        }
      }
    } else { // Ignore granularity
      try {
        Date d1 = Utility.string2Date(startDate);

        switch (metric) {
          case Impressions -> table.put(d1, (float) impressions(masterLog));
          case Clicks -> table.put(d1, (float) clicks(masterLog));
          case Unique -> table.put(d1, (float) uniques(masterLog));
          case BouncePage -> table.put(d1, (float) bouncePage(masterLog));
          case BounceVisit -> table.put(d1, (float) bounceVisit(masterLog));
          case Conversions -> table.put(d1, conversionRate(masterLog));
          case TotalCost -> table.put(d1, totalCost(masterLog));
          case ClickCost -> table.put(d1, masterLog.getClickCost());
          case CTR -> table.put(d1, clickRate(masterLog));
          case CPA -> table.put(d1, costAcquisition(masterLog));
          case CPC -> table.put(d1, costClick(masterLog));
          case CPM -> table.put(d1, costThousand(masterLog));
          case BounceRatePage -> table.put(d1, bounceRatePage(masterLog));
          case BounceRateVisit -> table.put(d1, bounceRateVisit(masterLog));
        }
      } catch (ParseException e) {
        logger.error("Invalid date used for request.");
        return table;
      }
    }

    return table;
  }

  /**
   * This function it used to split a Logs object into multiple chunks based on time granularity
   *
   * @param masterLog   log with all logs in
   * @param granularity size to create chunks of
   * @return chunked logs
   */
  private HashMap<Date, Logs> getGranularity(Logs masterLog, Granularity granularity) {
    HashMap<Date, Logs> timeLogs = new HashMap<>();

    // Granulate impression logs
    for (Impression impression : masterLog.impressionLogs) {
      Date key = DateUtils.truncate(impression.date(), granularity.getCalendar());

      if (!timeLogs.containsKey(key)) {
        timeLogs.put(key, new Logs());
      }

      timeLogs.get(key).impressionLogs.add(impression);
    }

    // Granulate click logs
    for (Click click : masterLog.clickLogs) {
      Date key = DateUtils.truncate(click.date(), granularity.getCalendar());

      if (!timeLogs.containsKey(key)) {
        timeLogs.put(key, new Logs());
      }

      timeLogs.get(key).clickLogs.add(click);
    }

    // Granulate server logs
    for (Server server : masterLog.serverLogs) {
      Date key = DateUtils.truncate(server.entryDate(), granularity.getCalendar());

      if (!timeLogs.containsKey(key)) {
        timeLogs.put(key, new Logs());
      }

      timeLogs.get(key).serverLogs.add(server);
    }

    return timeLogs;
  }

  /**
   * Pass through function for the number of impressions
   *
   * @param logs: Log object for which the accumulation happens
   * @return number of impressions
   */
  public int impressions(Logs logs) {
    return logs.getImpressions();
  }

  /**
   * Pass through function for the number of clicks
   *
   * @param logs: Log object for which the accumulation happens
   * @return number of clicks
   */
  public int clicks(Logs logs) {
    return logs.getClicks();
  }

  /**
   * Pass through function for the number of uniques
   *
   * @param logs: Log object for which the accumulation happens
   * @return number of uniques
   */
  public int uniques(Logs logs) {
    return logs.getUniques();
  }

  /**
   * Pass through function for the number of bounces - Page number definition
   *
   * @param logs: Log object for which the accumulation happens
   * @return number of bounces
   */
  public int bouncePage(Logs logs) {
    return logs.getBouncePage();
  }

  /**
   * Pass through function for the number of bounces - Visit duration definition
   *
   * @param logs: Log object for which the accumulation happens
   * @return number of bounces
   */
  public int bounceVisit(Logs logs) {
    return logs.getBounceVisit();
  }

  /**
   * Getter for the conversion rate
   *
   * @return conversion rate
   */
  public float conversionRate(Logs logs) {

    float conversions = logs.getConversions();
    int clicks = logs.getClicks();

    return conversions / clicks;
  }

  /**
   * This is a helper function to combine impression and click cost.
   *
   * @return Total cost of impressions and clicks
   */
  private float totalCost(Logs logs) {
    return logs.getImpressionCost() + logs.getClickCost();
  }

  /**
   * Getter for the Click-through-rate (CTR)
   *
   * @return click-through rate
   */
  public float clickRate(Logs logs) {

    float clicks = logs.getClicks();
    int impressions = logs.getImpressions();

    return impressions / clicks;
  }

  /**
   * Getter for the Cost-per-acquisition (CPA)
   * We define cost as the total cost for the entire campaign (Click-cost + impression-cost)
   *
   * @return cost-per-acquisition
   */
  public float costAcquisition(Logs logs) {

    int acquisitions = logs.getConversions();

    return totalCost(logs) / acquisitions;
  }


  /**
   * Getter for Cost-Per-Click metric (CPC)
   * We define cost as the total cost for the clicks.
   *
   * @return Cost per click
   */
  public float costClick(Logs logs) {

    float cost = logs.getClickCost();
    int clicks = logs.getClicks();

    return (cost / clicks);
  }

  /**
   * Getter for Cost-per-thousand-impressions (CPM)
   * We define cost as the total cost for the impressions.
   *
   * @return Cost per thousand impressions.
   */
  public float costThousand(Logs logs) {

    float cost = logs.getImpressionCost();
    int impressions = logs.getImpressions();

    return cost / impressions * 1000;
  }

  /**
   * Getter for the bounce rate. Defined as visiting only one page.
   *
   * @return bounce rate
   */
  public float bounceRatePage(Logs logs) {

    float clicks = logs.getClicks();
    int bounce = logs.getBouncePage();

    return bounce / clicks;
  }

  /**
   * Getter for the bounce rate. Defined as staying on the website for less than a minute.
   *
   * @return bounce rate
   */
  public float bounceRateVisit(Logs logs) {
    float clicks = logs.getClicks();
    int bounce = logs.getBounceVisit();

    return bounce / clicks;
  }

}