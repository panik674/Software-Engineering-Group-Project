import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.data.Metrics;

import java.util.ArrayList;

public class toms_tester {
  public static void main(String[] args) {
    Data data = new Data();
    Metrics metrics = new Metrics(data);

    ArrayList<String> pathsTest = new ArrayList<>();
    pathsTest.add("C:\\Users\\thoma\\Downloads\\2_month_campaign\\click_log.csv");
    pathsTest.add("C:\\Users\\thoma\\Downloads\\2_month_campaign\\impression_log.csv");
    pathsTest.add("C:\\Users\\thoma\\Downloads\\2_month_campaign\\server_log.csv");

    //pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
    //pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    //pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");

    long t1 = System.nanoTime();
    data.ingest(pathsTest);
    //data.ingest("C:\\Users\\thoma\\Downloads\\2_month_campaign");
    Logs logs = data.request();
    long t2 = System.nanoTime();
    long d1 = (t2 - t1);

    System.out.println(logs.impressionLogs.size());
    System.out.println(logs.clickLogs.size());

    System.out.println(d1);
  }
}
