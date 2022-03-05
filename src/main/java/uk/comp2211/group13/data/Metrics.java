package uk.comp2211.group13.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.enums.Metric;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

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
   * This is used to add data to the metric object
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
  public HashMap<Date, Float> request(Metric metric, String startDate, String endDate) throws ParseException {

    long increment = 1000*60*60*24;
    Date d1 = Utility.string2Date(startDate);
    Date d2 = Utility.string2Date(endDate);
    //specify whether we want to have a <Date, Float> or a <String, Float> HashMap
    HashMap<Date, Float> table = new HashMap<>();

    for (long i = d1.getTime(); i < d2.getTime()-increment; i += increment) {
      //request should return the log object for the dates from i to i+increment
      Logs log = data.request(startDate, endDate);
      Date idx = new Date(i);
      Utility.date2String(idx);
      switch (metric) {
        case Impressions -> table.put(idx, (float) impressions(log));
        case Clicks -> table.put(idx, (float) clicks(log));
        case Unique -> table.put(idx, (float) uniques(log));
        case BouncePage -> table.put(idx, (float) bouncePage(log));
        case BounceVisit -> table.put(idx, (float) bounceVisit(log));
        case Conversions -> table.put(idx, conversionRate(log));
        case TotalCost -> table.put(idx, totalCost(log));
        case CTR -> table.put(idx, clickRate(log));
        case CPA -> table.put(idx, costAcquisition(log));
        case CPC -> table.put(idx, costClick(log));
        case CPM -> table.put(idx, costThousand(log));
        case BounceRatePage -> table.put(idx, bounceRatePage(log));
        case BounceRateVisit -> table.put(idx, bounceRateVisit(log));
      }
    }

    return table;
  }

  // TODO: Add java docs
  public int impressions(Logs logs) {
    return logs.getImpressions();
  }

  public int clicks(Logs logs) {
    return logs.getClicks();
  }

  public int uniques(Logs logs) {
    return logs.getUniques();
  }

  public int bouncePage(Logs logs) {
    return logs.getBouncePage();
  }

  public int bounceVisit(Logs logs) {
    return logs.getBounceVisit();
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
  //:TODO:Change tests to reflect logs granularity.
  public float clickRate(Logs logs) {

    int clicks = logs.getClicks();
    int impressions = logs.getImpressions();

    return impressions / clicks;
  }

  /**
   * Getter for the bounce rate. Defined as visiting only one page.
   *
   * @return bounce rate
   */
  //:TODO:Change tests to reflect logs granularity.
  public float bounceRatePage(Logs logs) {

    int clicks = logs.getClicks();
    int bounce = logs.getBouncePage();

    return bounce / clicks;
  }

  /**
   * Getter for the bounce rate. Defined as staying on the website for less than a minute.
   *
   * @return bounce rate
   */
  public float bounceRateVisit(Logs logs) {
    int clicks = logs.getClicks();
    int bounce = logs.getBounceVisit();

    return bounce / clicks;
  }

  /**
   * Getter for the conversion rate
   *
   * @return conversion rate
   */
  //:TODO:Change tests to reflect logs granularity.
  public float conversionRate(Logs logs) {

    int conversions = logs.getConversions();
    int clicks = logs.getClicks();

    return conversions / clicks;
  }

  /**
   * Getter for the Cost-per-acquisition (CPA)
   * We define cost as the total cost for the entire campaign (Click-cost + impression-cost)
   *
   * @return cost-per-acquisition
   */
  //:TODO:Change tests to reflect logs granularity.
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
  //:TODO:Change tests to reflect logs granularity.
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
  //:TODO:Change tests to reflect logs granularity.
  public float costThousand(Logs logs) {

    float cost = logs.getImpressionCost();
    int impressions = logs.getImpressions();

    return cost / impressions * 1000;
  }

}