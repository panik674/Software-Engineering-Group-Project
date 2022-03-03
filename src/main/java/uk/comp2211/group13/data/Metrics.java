package uk.comp2211.group13.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is used to calculate and retrieve the requested metrics.
 * Will expand to version with selected intervals.
 */

public class Metrics {

  private static final Logger logger = LogManager.getLogger(Metrics.class); // TODO: Check if logger is needed?
  private Data data;

  //TODO: must look into which Data object we'll be referring to. I think it should be referenced from the one
  // determined by the UI

  // TODO: Add java doc
  public Metrics(Data data) {
    this.data = data;
  }

  /**
   * Getter for the Click-through-rate (CTR)
   *
   * @return click-through rate
   */
  public float clickRate() {
    int clicks = data.getClicks();
    int impressions = data.getImpressions();

    return impressions / clicks;
  }

  /**
   * Getter for the bounce rate. Refer to data for the definition of Bounce used.
   *
   * @return bounce rate
   */
  public float bounceRate() {
    int clicks = data.getClicks();
    int bounce = data.getBounces();

    return bounce / clicks;
  }

  /**
   * Getter for the conversion rate
   *
   * @return conversion rate
   */
  public float conversionRate() {
    int conversions = data.getConversions();
    int clicks = data.getClicks();

    return conversions / clicks;
  }

  /**
   * Getter for the Cost-per-acquisition (CPA)
   * We define cost as the total cost for the entire campaign (Click-cost + impression-cost)
   *
   * @return cost-per-acquisition
   */
  public float costAcquisition() {
    int acquisitions = data.getConversions();

    return totalCost / acquisitions;
  }

  /**
   * Getter for Cost-Per-Click metric (CPC)
   * We define cost as the total cost for the clicks.
   *
   * @return Cost per click
   */
  public float costClick() {
    float cost = data.getClickCost();
    int clicks = data.getClicks();

    return (cost / clicks);
  }

  /**
   * Getter for Cost-per-thousand-impressions (CPM)
   * We define cost as the total cost for the impressions.
   *
   * @return Cost per thousand impressions.
   */
  public float costThousand() {
    float cost = data.getImpressionCost();
    int impressions = data.getImpressions();

    return cost / impressions * 1000;
  }

  // TODO: Change to function or listener for on data update.
  //  If log data changes this won't update and data object may not be assigned on time of execution.
  private float totalCost = data.getImpressionCost() + data.getClickCost();

  // TODO: Add java docs
  public int impressions() {
    return data.getImpressions();
  }

  public int clicks() {
    return data.getClicks();
  }

  public int uniques() {
    return data.getUniques();
  }

  public int bounces() {
    return data.getBounces();
  }

  public float totalCost() {
    return totalCost;
  }


}