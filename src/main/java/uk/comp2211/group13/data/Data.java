package uk.comp2211.group13.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.data.log.Click;
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.data.log.Server;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Path;

import java.io.File;
import java.text.ParseException;
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
   * This requires all 3 paths Impression, Click and Server for it to not fail.
   * <p>
   * If this returns false, an error message will be logged and the stored logs are cleared
   * If this returns true, it has successfully loaded the logs
   *
   * @param paths this is a dictionary with the key as type of log and String as the path to the file.
   * @return boolean value for ingest success
   */
  public boolean ingest(HashMap<Path, String> paths) {
    logs = new Logs();
    ;

    // Checks all 3 log paths have been entered
    if (!paths.containsKey(Path.Impression) || !paths.containsKey(Path.Click) || !paths.containsKey(Path.Server)) {
      logger.error("Log ingest failed since there is a lack of paths");

      return false;
    }

    // Try to ingest each log
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

        // Basic file validation
        boolean validFlag;
        switch (path.getKey()) {
          case Impression -> validFlag = Objects.equals(reader.nextLine(), "Date,ID,Gender,Age,Income,Context,Impression Cost");
          case Click -> validFlag = Objects.equals(reader.nextLine(), "Date,ID,Click Cost");
          case Server -> validFlag = Objects.equals(reader.nextLine(), "Entry Date,ID,Exit Date,Pages Viewed,Conversion");
          default -> validFlag = false;
        }

        if (!validFlag) {
          throw new Exception("File failed basic validation");
        }

        // Ingest data
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
      ;

      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    long t1 = System.nanoTime();
    Data data = new Data();

    HashMap<Path, String> paths = new HashMap<Path, String>();
    paths.put(Path.Impression, "C:\\Users\\thoma\\Downloads\\2_week_campaign_2\\impression_log.csv");
    paths.put(Path.Click, "C:\\Users\\thoma\\Downloads\\2_week_campaign_2\\click_log.csv");
    paths.put(Path.Server, "C:\\Users\\thoma\\Downloads\\2_week_campaign_2\\server_log.csv");

    data.ingest(paths);
    long t2 = System.nanoTime();

    for (int i = 0; i < 336; i++) {
      System.out.println("Time:" + i);
      HashMap<Filter, String> filters = new HashMap<>();
      filters.put(Filter.StartDatetime, "2015-01-01 12:00:00");
      filters.put(Filter.EndDatetime, "2015-01-01 13:00:00");

      Logs logs = data.request(filters);
    }
    long t3 = System.nanoTime();

    long d1 = (t2 - t1);
    long d2 = (t3 - t2);

    System.out.println(d1);
    System.out.println(d2);
  }

  /**
   * This is used to request all log data from the data object.
   *
   * @param filters this is a list of applied filters
   * @return returns requested data.
   */
  public Logs request(HashMap<Filter, String> filters) {
    Logs output = new Logs();

    boolean enableDataRange = false;
    Date startDate = null;
    Date endDate = null;

    if (filters.size() == 0) {
      return logs;

    } else {
      try {
        if (filters.containsKey(Filter.StartDatetime) && filters.containsKey(Filter.EndDatetime)) {
          startDate = Utility.string2Date(filters.get(Filter.StartDatetime));
          endDate = Utility.string2Date(filters.get(Filter.EndDatetime));
          enableDataRange = true;
        }
      } catch (ParseException e) {
        logger.error("Unable to complete request due to invalid dates");
      }

      // Create impressions list
      for (Impression impression : logs.impressionLogs) {
        try { // Check date range is being filtered and check record against date range
          if (enableDataRange && !withinDate(startDate, endDate, Utility.string2Date(impression.date()))) continue;
        } catch (ParseException e) {
          logger.error(String.format("Skipping impression record %s due to invalid date", impression.id()));
          continue;
        }

        output.impressionLogs.add(impression);
      }

      // Create impressions list
      for (Click click : logs.clickLogs) {
        try { // Check date range is being filtered and check record against date range
          if (enableDataRange && !withinDate(startDate, endDate, Utility.string2Date(click.date()))) continue;
        } catch (ParseException e) {
          logger.error(String.format("Skipping click record %s due to invalid date", click.id()));
          continue;
        }

        output.clickLogs.add(click);
      }

      // Create impressions list
      for (Server server : logs.serverLogs) {
        try { // Check date range is being filtered and check record against date range
          if (enableDataRange && !withinDate(startDate, endDate, Utility.string2Date(server.entryDate()))) continue;
        } catch (ParseException e) {
          logger.error(String.format("Skipping server record %s due to invalid date", server.id()));
          continue;
        }

        output.serverLogs.add(server);
      }
    }


    return output;
  }

  /**
   * This is a helper function to determine if a date is within a range.
   *
   * @param start start of range
   * @param end end of range
   * @param target date to check
   * @return true if in range
   */
  private boolean withinDate(Date start, Date end, Date target) {
    return (target.after(start) || target.equals(start)) && target.before(end);
  }
}