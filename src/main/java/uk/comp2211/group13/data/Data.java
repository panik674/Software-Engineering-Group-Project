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
   *
   * This requires all 3 paths Impression, Click and Server for it to not fail.
   *
   * If this returns false, an error message will be logged and the stored logs are cleared
   * If this returns true, it has successfully loaded the logs
   *
   * @param paths this is a dictionary with the key as type of log and String as the path to the file.
   * @return boolean value for ingest success
   */
  public boolean ingest(HashMap<Path, String> paths) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    logs = new Logs();

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
                    sdf.parse(line[0]),
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
                    sdf.parse(line[0]),
                    line[1],
                    Float.parseFloat(line[2])
                )
            );
            case Server -> logs.serverLogs.add(
                new Server(
                    sdf.parse(line[0]),
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
}