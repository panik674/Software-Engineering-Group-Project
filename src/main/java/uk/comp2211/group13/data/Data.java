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
import java.io.FileNotFoundException;
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
  private Logs masterLog = new Logs();

  /**
   * These store the min and max date of the currently stored master log
   */
  private Date maxDate = null;
  private Date minDate = null;

  /**
   * This will return the master log's min date
   *
   * @return min date in master log
   */
  public Date getMinDate() {
    return minDate;
  }

  /**
   * This will return the master log's max date
   *
   * @return max date in master log
   */
  public Date getMaxDate() {
    return maxDate;
  }


  /**
   * These functions are is used to ingest data into the data object from the various logs.
   * It will validate the incoming files in this function and pass it to performIngest().
   * <p>
   * This requires all 3 paths Impression, Click and Server for it to not fail.
   * <p>
   * Status codes
   * 0 = success
   * 1 = log validation and estimation fail
   * 2 = Scanner file not found
   * 3 = click and server log mismatch (invalid data)
   * 4 = invalid date format (invalid data)
   *
   * @param inputPaths this is a list of Strings as the paths to the log files.
   * @return int status code
   */
  public int ingest(ArrayList<String> inputPaths) {
    HashMap<Path, File> paths;
    try {
      paths = ingestFiles(inputPaths);
    } catch (Exception e) {
      logger.error("Ingest files error, reason: " + e.getMessage());
      return 1;
    }

    return performIngest(paths);
  }

  public int ingest(String folder) {
    HashMap<Path, File> paths;
    try {
      paths = ingestFolder(folder);
    } catch (Exception e) {
      logger.error("Ingest folder error, reason: " + e.getMessage());
      return 1;
    }

    return performIngest(paths);
  }

  /**
   * This is used to pass ingest() data from a folder rather than individual files.
   * <p>
   * Status codes
   * 5 = This isn't a folder/exists
   *
   * @param folderPath path to folder containing files
   * @return status code
   */
  private HashMap<Path, File> ingestFolder(String folderPath) throws Exception {
    File folder = new File(folderPath);

    // Check path goes to folder and exists
    if (!folder.exists() || !folder.isDirectory()) {
      throw new Exception("Unable to ingest folder that isn't a folder or non-existent");
    }

    // Get log file paths
    HashMap<Path, File> paths = new HashMap<>();
    for (File file : Objects.requireNonNull(folder.listFiles())) {
      if (file.isFile()) {
        try {
          Path result = estimateLogType(file);

          if (result != Path.Invalid) {
            paths.put(result, file);
          }
        } catch (Exception ignored) {
        }
      }
    }

    // Checks all 3 log paths have been entered
    if (!paths.containsKey(Path.Impression)
        || !paths.containsKey(Path.Click)
        || !paths.containsKey(Path.Server)
        || paths.containsKey(Path.Invalid)) {
      throw new Exception("Log ingest failed there are invalid logs or missing logs.");
    }

    return paths;
  }

  /**
   * This is used to convert string paths into File and validate
   *
   * @param inputPaths string paths to convert into files
   * @return Log type and file key value pair
   */
  private HashMap<Path, File> ingestFiles(ArrayList<String> inputPaths) throws Exception {
    // Check number of input paths
    if (inputPaths.size() != 3) {
      throw new Exception("Log ingest failed since there is an invalid number of input paths.");
    }

    // Estimate path types
    HashMap<Path, File> paths = new HashMap<>();
    for (String inputPath : inputPaths) {
      File file = new File(inputPath);
      if (!file.exists() || file.isDirectory()) {
        throw new Exception("Cannot reach/open file");
      }

      paths.put(estimateLogType(file), file);
    }

    // Checks all 3 log paths have been entered
    if (!paths.containsKey(Path.Impression)
        || !paths.containsKey(Path.Click)
        || !paths.containsKey(Path.Server)
        || paths.containsKey(Path.Invalid)) {
      throw new Exception("Log ingest failed there are invalid logs or missing logs.");
    }

    return paths;
  }

  /**
   * This is a helper function where we estimate the log file type based off of header line
   *
   * @param file file to estimate log type of
   * @return Path type
   */
  private Path estimateLogType(File file) throws Exception {
    // Setup file reader
    Scanner reader = new Scanner(file);
    String line = reader.nextLine();
    reader.close();

    // Basic file validation
    switch (line) {
      case "Date,ID,Gender,Age,Income,Context,Impression Cost" -> {
        return Path.Impression;
      }
      case "Date,ID,Click Cost" -> {
        return Path.Click;
      }
      case "Entry Date,ID,Exit Date,Pages Viewed,Conversion" -> {
        return Path.Server;
      }

      default -> {
        return Path.Invalid;
      }
    }
  }

  /**
   * This function will ingest data into the master log.,
   *
   * @param paths paths to ingest
   * @return status code
   */
  private int performIngest(HashMap<Path, File> paths) {
    masterLog = new Logs();

    //Set up scanners
    try {
      Scanner impressionScanner = new Scanner(paths.get(Path.Impression));
      Scanner clickScanner = new Scanner(paths.get(Path.Click));
      Scanner serverScanner = new Scanner(paths.get(Path.Server));

      // Skip header line
      impressionScanner.nextLine();
      clickScanner.nextLine();
      serverScanner.nextLine();

      while (true) {
        boolean endFlag = false;
        String[] impressionLine;

        Impression impression;
        Click click = null;
        Server server = null;
        if (clickScanner.hasNextLine() && serverScanner.hasNextLine()) {
          click = string2Click(clickScanner.nextLine().split(","));
          server = string2Server(serverScanner.nextLine().split(","));

          if (!Objects.equals(click.id(), server.id())) { //Assuming equal numbers of server and log entries
            logger.error("Ingest error has occurred, click and server log lines are not id matched");
            return 3;
          }
        }
        if ((clickScanner.hasNextLine() && !serverScanner.hasNextLine())
            || (!clickScanner.hasNextLine() && serverScanner.hasNextLine())) {
          logger.error("Ingest error has occurred, click and server log lines are not count matched");
          return 3;
        }

        boolean logFlag = true;
        while (logFlag) {
          if (impressionScanner.hasNextLine()) {
            try {
              impressionLine = impressionScanner.nextLine().split(",");

              if (click != null && Objects.equals(click.id(), impressionLine[1])) {
                logFlag = false;
                impression = string2Impression(impressionLine, click, server);
              } else {
                impression = string2Impression(impressionLine, null, null);
              }

              masterLog.impressionLogs.add(impression);

              // Set max and min data for the master log
              if (minDate == null || maxDate == null) {
                minDate = impression.date();
                maxDate = impression.date();
              }
              if (minDate.after(impression.date())) minDate = impression.date();
              if (maxDate.before(impression.date())) maxDate = new Date(impression.date().getTime() + 1000L);

            } catch (ParseException e) {
              throw e;
            } catch (Exception e) {
              logger.error("Invalid impression filter data: " + e.getMessage());
            }

          } else {
            endFlag = true;
            break;
          }
        }

        // TODO: Remove???
        masterLog.clickLogs.add(click);
        masterLog.serverLogs.add(server);

        if (endFlag) break;
      }

      // Tidy up
      impressionScanner.close();
      clickScanner.close();
      serverScanner.close();

      masterLog.impressionLogs.removeAll(Collections.singleton(null));
      masterLog.clickLogs.removeAll(Collections.singleton(null));
      masterLog.serverLogs.removeAll(Collections.singleton(null));

    } catch (FileNotFoundException e) {
      logger.error("Scanner error, reason: " + e.getMessage());
      return 2;

    } catch (ParseException e) {
      logger.error("Date conversion error, reason: " + e.getMessage());
      return 4;
    }
    return 0;
  }


  /**
   * This is used to create an Impression
   *
   * @param line   line from csv
   * @param click  related click log
   * @param server related server log
   * @return return Impression
   * @throws ParseException date error
   */
  private static Impression string2Impression(String[] line, Click click, Server server) throws Exception {
    return new Impression(
        Utility.string2Date(line[0]), // date
        line[1], // id
        Utility.validateGender(line[2]), // gender
        Utility.validateAge(line[3]), // age
        Utility.validateIncome(line[4]), // income
        Utility.validateContext(line[5]), // context
        Float.parseFloat(line[6]), // cost
        click,
        server
    );
  }

  /**
   * This is used to create a Click
   *
   * @param line line from csv
   * @return return Click
   * @throws ParseException date error
   */
  private static Click string2Click(String[] line) throws ParseException {
    return new Click(
        Utility.string2Date(line[0]), // date
        line[1], // id
        Float.parseFloat(line[2]) // cost
    );
  }

  /**
   * This is used to create a Server
   *
   * @param line line from csv
   * @return return Server
   * @throws ParseException date error
   */
  private static Server string2Server(String[] line) throws ParseException {
    return new Server(
        Utility.string2Date(line[0]), // date
        line[1], // id
        line[2], // exit date
        Integer.parseInt(line[3]), // pages
        Objects.equals(line[4], "Yes") // conversation
    );
  }


  /**
   * This is used to return an unfiltered master log
   *
   * @return masterlog
   */
  public Logs request() {
    return masterLog;
  }

  /**
   * This is used to request filtered log data from the data object.
   *
   * @param filters this is a list of applied filters
   * @return returns requested data.
   */
  public Logs request(Date startDate, Date endDate, HashMap<Filter, String[]> filters) {
    Logs output = new Logs();

    // Check dates are ordered correctly
    if (endDate.before(startDate)) {
      logger.error("End date cannot occur before start date");
      return null;
    }

    impressionLoop: for (Impression impression : masterLog.impressionLogs) {
      // Check date
      if (!withinDate(startDate, endDate, impression.date())) continue;

      // Check filters match
      for (Map.Entry<Filter, String[]> filter : filters.entrySet()) {
        if (!switch (filter.getKey()) {
          case Gender -> Arrays.asList(filter.getValue()).contains(impression.gender());
          case Age -> Arrays.asList(filter.getValue()).contains(impression.age());
          case Income -> Arrays.asList(filter.getValue()).contains(impression.income());
          case Context -> Arrays.asList(filter.getValue()).contains(impression.context());
        }) continue impressionLoop;
      }

      output.impressionLogs.add(impression);
      if (impression.click() != null){ // TODO: Remove???
        output.clickLogs.add(impression.click());
        output.serverLogs.add(impression.server());
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
  private static boolean withinDate(Date start, Date end, Date target) {
    return (target.after(start) || target.equals(start)) && target.before(end);
  }
}