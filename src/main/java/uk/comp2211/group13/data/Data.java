package uk.comp2211.group13.data;

import java.util.HashMap;

/**
 * This class is used to ingest, store and reply to request about the data stores in the various logs.
 */
public class Data {
  // TODO: Decide data format
  // private FORMAT data;

  /**
   * This is used to ingest data into the data object from the various logs.
   *
   * @param paths this is a dictionary with the key as type of log and String as the path to the file.
   */
  public void ingest(HashMap<Path, String> paths) {

  }

  /**
   * This is used to validate that a path leads to a valid log file.
   *
   * @param path path to file
   * @return boolean if the file is valid or not
   */
  private Boolean validatePath(String path) {
    return true;
  }


  /**
   * This is used to validate that a log file has the correct number of rows and populated with valid data.
   *
   * @param type type of log
   * @param file path to file
   * @return boolean if the log file is valid or not
   */
  private Boolean validateLogFormat(Path type, String file){
    return true;
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