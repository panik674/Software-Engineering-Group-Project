package uk.comp2211.group13.data;

import uk.comp2211.group13.data.log.Click;
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.data.log.Server;

import java.util.ArrayList;

public class Logs {
  public ArrayList<Impression> impressionLogs = new ArrayList<>();
  public ArrayList<Click> clickLogs = new ArrayList<>();
  public ArrayList<Server> serverLogs = new ArrayList<>();
  /* get the impression logs data
  * */
  public ArrayList<Impression> getImpressionLogs(){
      return impressionLogs;
  }
    /* get the click logs data
     * */
  public ArrayList<Click> getClickLogs() {
      return clickLogs;
  }
    /* get the server logs data
     * */
  public ArrayList<Server>getServerLogs(){
      return serverLogs;
  }
}
