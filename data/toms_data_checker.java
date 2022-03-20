import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class toms_data_checker {
  public static void main(String[] args) throws FileNotFoundException {
    File file = new File("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    Scanner reader = new Scanner(file);

    reader.nextLine();

    float total = 0;
    while (reader.hasNextLine()) {
      String[] line = reader.nextLine().split(",");

      total += Float.parseFloat(line[6]);
    }

    System.out.println(total);


    Data data = new Data();
    ArrayList<String> pathsTest = new ArrayList<>();
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
    data.ingest(pathsTest);

    HashMap<Filter, String[]> filter = new HashMap<>();
    Logs logs = data.request(data.getMinDate(), data.getMaxDate(), filter);

    System.out.println(logs.getImpressionCost());
  }
}
