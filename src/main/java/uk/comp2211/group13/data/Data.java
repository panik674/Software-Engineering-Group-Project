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
                    Utility.string2Date(line[0]),
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
                    Utility.string2Date(line[0]),
                    line[1],
                    Float.parseFloat(line[2])
                )
            );
            case Server -> logs.serverLogs.add(
                new Server(
                    Utility.string2Date(line[0]),
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

  /**
   * This is used to request filtered log data from the data object.
   *
   * @param filters this is a list of applied filters
   * @return returns requested data.
   */
  public Logs request(HashMap<Filter, String> filters) {
    Logs output = new Logs();

    // Init date range values
    boolean enableDataRange = false; // Used to quickly check if date range is being used
    Date startDate = null;
    Date endDate = null;

    if (filters.size() == 0) {
      // No filters return all
      return logs;

    } else {
      try { // Set up date range
        if (filters.containsKey(Filter.StartDatetime) && filters.containsKey(Filter.EndDatetime)) {
          startDate = Utility.string2Date(filters.get(Filter.StartDatetime));
          endDate = Utility.string2Date(filters.get(Filter.EndDatetime));
          enableDataRange = true;
        }
      } catch (ParseException e) {
        logger.error("Unable to complete request due to invalid dates");
      }

      // Filter impressions list
      for (Impression impression : logs.impressionLogs) {
        if (enableDataRange && !withinDate(startDate, endDate, impression.date())) continue;

        output.impressionLogs.add(impression);
      }

      // Filter impressions list
      for (Click click : logs.clickLogs) {
        if (enableDataRange && !withinDate(startDate, endDate, click.date())) continue;

        output.clickLogs.add(click);
      }

      // Filter impressions list
      for (Server server : logs.serverLogs) {
        if (enableDataRange && !withinDate(startDate, endDate, server.entryDate())) continue;

        output.serverLogs.add(server);
      }
    }


    return output;
  }

  /**
   * This is a helper function to determine if a date is within a range.
   *
   * @param start  start of range
   * @param end    end of range
   * @param target date to check
   * @return true if in range
   */
  private boolean withinDate(Date start, Date end, Date target) {
    return (target.after(start) || target.equals(start)) && target.before(end);
  }
}