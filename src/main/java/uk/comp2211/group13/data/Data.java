package uk.comp2211.group13.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.data.log.Click;
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.data.log.Server;
import uk.comp2211.group13.enums.Path;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class is used to ingest, store and reply to request about the data stores in the various logs.
 */
public class Data {

  private static final Logger logger = LogManager.getLogger(Data.class);

  /**
   * These store the logs we ingest
   */
  private Logs logs = new Logs();

  /**
   * This is used to ingest data into the data object from the various logs.
   * <p>
   * If this returns false, an error message will be logged and the stored logs are cleared
   * If this returns true, it has successfully loaded the logs
   *
   * @param paths this is a dictionary with the key as type of log and String as the path to the file.
   * @return boolean value for ingest success
   */
  public boolean ingest(HashMap<Path, String> paths) {
    logs = new Logs();

    if (paths.containsKey(Path.Impression) && paths.containsKey(Path.Click) && paths.containsKey(Path.Server)) {
      logger.error("Log ingest failed since there is a lack of paths");

      return false;
    }

    try {
      for (Map.Entry<Path, String> path : paths.entrySet()) {
        // File exists validation
        File file = new File(path.getValue());
        if (!file.exists() || file.isDirectory()) {
          throw new Exception("Invalid Log Path");
        }

        // Setup file reader
        Scanner reader = new Scanner(file);
        String[] line;

        while (reader.hasNextLine()) {
          line = reader.nextLine().split(",");

          // Process Log to new record and add to list
          switch (path.getKey()) {
            case Impression -> logs.impressionLogs.add(
                new Impression(
                    line[0],
                    line[1],
                    line[2],
                    line[3],
                    line[4],
                    line[5],
                    Float.parseFloat(line[6])
                )
            );
            case Click -> logs.clickLogs.add(
                new Click(
                    line[0],
                    line[1],
                    Float.parseFloat(line[2])
                )
            );
            case Server -> logs.serverLogs.add(
                new Server(
                    line[0],
                    line[1],
                    line[2],
                    Integer.parseInt(line[3]),
                    Objects.equals(line[4], "Yes")
                )
            );
          }
        }

        reader.close();
      }
    } catch (Exception e) {
      logger.error(String.format("Log ingest failed Reason: %s", e.getMessage()));
      logs = new Logs();

      return false;
    }
    return true;
  }

  /**
   * This is used to request all log data from the data object.
   *
   * @return returns requested data.
   */
  public Logs request() {
    return logs;
  }

  /**
   * Following methods are used to access raw metrics
   *
   * @return returns the requested metric
   */
  public int getClicks() {
    return logs.clickLogs.size();
  }

  public int getImpressions() {
    return logs.impressionLogs.size();
  }

  /**
   * Getter for bounces. Defined as staying on the website for less than a minute or visiting only one page
   *
   * @return Total number of bounces in logs
   */
  // TODO: Advise somebody about the exception handling same on the difDate
  public int getBounces() {
    int sum = 0;

    try {
      for (Server value : logs.serverLogs) {
        String start = value.entryDate();
        String finish = value.exitDate();

        long hours = difDate(start, finish);
        int pages = value.pages();
        if ((hours <= 1) || pages == 1) sum++;
      }
      return sum;
    } catch (Exception e) {
      logger.error(String.format("Bounce total request failed Reason: %s", e.getMessage()));
    }
    return sum; // TODO: Can we look into better error handling rather than return an incorrect value (ie, -1 for error or something)
  }

  /**
   * Helper function for getBounces() to find difference in minutes
   *
   * @param start start date
   * @param end   end date
   * @return difference in ??? TODO: Time spanning?
   */
  private long difDate(String start, String end) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    try {
      Date d1 = sdf.parse(start);
      Date d2 = sdf.parse(end);
      long difTime = d2.getTime() - d1.getTime();
      return (difTime / (1000 * 60));

    } catch (ParseException e) {
      e.printStackTrace();
    }

    return -1;
  }

  // TODO: Add java doc
  public int getConversions() {
    int sum = 0;

    for (Server value : logs.serverLogs) {
      if (value.conversion()) sum++;
    }

    return sum;
  }

  // TODO: Add java doc
  public int getUniques() {
    HashSet<String> ids = new HashSet<>();

    for (Click click : logs.clickLogs) {
      ids.add(click.id());
    }

    return ids.size();
  }

  // TODO: Add java doc
  public float getClickCost() {
    float sum = 0;

    for (Click click : logs.clickLogs) {
      sum += click.cost();
    }

    return sum;
  }

  // TODO: Add java doc
  public float getImpressionCost() {
    float sum = 0;

    for (Impression impression : logs.impressionLogs) {
      sum += impression.cost();
    }

    return sum;
  }


}