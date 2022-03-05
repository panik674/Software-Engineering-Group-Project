package uk.comp2211.group13.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.enums.Metric;

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
  public HashMap<Data, Float> request(Metric metric) {
    return new HashMap<>();
  }

  /**
   * This is a helper function to combine impression and click cost.
   *
   * @return Total cost of impressions and clicks
   */
  private float totalCost() {
    Logs logs = data.request();
    return logs.getImpressionCost() + logs.getClickCost();
  }

  /**
   * Getter for the Click-through-rate (CTR)
   *
   * @return click-through rate
   */
  public float clickRate() {
    Logs logs = data.request();

    int clicks = logs.getClicks();
    int impressions = logs.getImpressions();

    return impressions / clicks;
  }

  /**
   * Getter for the bounce rate. Defined as visiting only one page.
   *
   * @return bounce rate
   */
  public float bounceRatePage() {
    Logs logs = data.request();

    int clicks = logs.getClicks();
    int bounce = logs.getBouncePage();

    return bounce / clicks;
  }

  /**
   * Getter for the bounce rate. Defined as staying on the website for less than a minute.
   *
   * @return bounce rate
   */
  public float bounceRateVisit() {
    Logs logs = data.request();

    int clicks = logs.getClicks();
    int bounce = logs.getBounceVisit();

    return bounce / clicks;
  }

  /**
   * Getter for the conversion rate
   *
   * @return conversion rate
   */
  public float conversionRate() {
    Logs logs = data.request();

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

  public float costAcquisition() {
    Logs logs = data.request();

    int acquisitions = logs.getConversions();

    return totalCost() / acquisitions;
  }


  /**
   * Getter for Cost-Per-Click metric (CPC)
   * We define cost as the total cost for the clicks.
   *
   * @return Cost per click
   */
  public float costClick() {
    Logs logs = data.request();

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
  public float costThousand() {
    Logs logs = data.request();

    float cost = logs.getImpressionCost();
    int impressions = logs.getImpressions();

    return cost / impressions * 1000;
  }

  // TODO: Add java docs
  public int impressions() {
    return data.request().getImpressions();
  }

  public int clicks() {
    return data.request().getClicks();
  }

  public int uniques() {
    return data.request().getUniques();
  }

  public int bouncePage() {
    return data.request().getBouncePage();
  }

  public int bounceVisit() {
    return data.request().getBounceVisit();
  }

}