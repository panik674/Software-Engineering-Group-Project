package uk.comp2211.group13.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.data.log.Click;
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.data.log.Server;
import uk.comp2211.group13.enums.Age;
import uk.comp2211.group13.enums.Context;
import uk.comp2211.group13.enums.Income;
import uk.comp2211.group13.enums.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class is used to ingest, store and reply to request about the data stores in the various logs.
 */
public class Data {

  private static final Logger logger = LogManager.getLogger(Data.class);

  /**
   * These store the logs we ingest
   */
  private ArrayList<Impression> impressionLogs ;
  private ArrayList<Click> clickLogs;
  private ArrayList<Server> serverLogs ;

  /**
   * This is used to ingest data into the data object from the various logs.
   *
   * @param paths this is a dictionary with the key as type of log and String as the path to the file.
   */
  public void ingest(HashMap<Path, String> paths) { // TODO: May be worth thinking about better validation or having 3 parameters 1 for each log
    for (Map.Entry<Path, String> path : paths.entrySet()) {  // For each path
      try {
        // Validation
        validatePath(path.getValue());  // Validate file exists
        validateLogFormat(path.getKey(), path.getValue()); // Validate file contains valid data

        // Clean slate log on each update to avoid conflicting or repeated data
        switch (path.getKey()) {
          case Impression -> impressionLogs = new ArrayList<>();
          case Click -> clickLogs = new ArrayList<>();
          case Server -> serverLogs = new ArrayList<>();
        }

        // Setup file reader
        Scanner reader = new Scanner(new File(path.getValue()));
        String[] line;

        while (reader.hasNextLine()) {
          line = reader.nextLine().split(",");

          // Process Log to new record and add to list
          switch (path.getKey()) {
            case Impression -> impressionLogs.add(
                new Impression(
                    line[0],
                    line[1],
                    line[2],
                    Age.stringToAge(line[3]),
                    Income.stringToIncome(line[4]),
                    Context.stringToContext(line[5]),
                    Float.parseFloat(line[6])
                )
            );
            case Click -> clickLogs.add(
                new Click(
                    line[0],
                    line[1],
                    Float.parseFloat(line[2])
                )
            );
            case Server -> serverLogs.add(
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
      } catch (Exception e){
        // TODO: invalid error catching
      }
    }
  }

  /**
   * This is used to validate that a path leads to a valid log file.
   *
   * @param path path to file
   */
  private void validatePath(String path) {

  }


  /**
   * This is used to validate that a log file has the correct number of rows and populated with valid data.
   *
   * @param type type of log
   * @param file path to file
   */
  private void validateLogFormat(Path type, String file) {

  }

  /**
   * This is used to request data from the data object.
   *
   * @return returns requested data.
   */
  public String request() { // TODO: Correct data format as return type
    return "Data";
  }

}