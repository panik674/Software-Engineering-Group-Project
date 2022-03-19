import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class toms_tester {
  public static void main(String[] args) {
    Data data = new Data();
    Metrics metrics = new Metrics(data);

    // 2 week data
    ArrayList<String> paths2week = new ArrayList<>();
    paths2week.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
    paths2week.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    paths2week.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");

    // 2 month data
    ArrayList<String> paths2month = new ArrayList<>();
    paths2month.add("C:\\Users\\thoma\\Downloads\\2_month_campaign\\click_log.csv");
    paths2month.add("C:\\Users\\thoma\\Downloads\\2_month_campaign\\impression_log.csv");
    paths2month.add("C:\\Users\\thoma\\Downloads\\2_month_campaign\\server_log.csv");

    // filters
    HashMap<Filter, String[]> filter = new HashMap<>();
    filter.put(Filter.Gender, new String[]{"Male", "Female"});
    filter.put(Filter.Age, new String[]{"<25", "25-34", "35-44", "45-54", ">54"});
    filter.put(Filter.Income, new String[]{"Low", "Medium", "High"});
    filter.put(Filter.Context, new String[]{"News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel"});


    long t1 = System.nanoTime();
    //data.ingest(pathsTest);
    //data.ingest("C:\\Users\\thoma\\Downloads\\2_month_campaign");
    data.ingest("src/test/java/uk/comp2211/group13/testdata");

    long t2 = System.nanoTime();

    //Logs logs = data.request(
    //    data.getMinDate(),
    //    data.getMaxDate(),
    //    filter
    //);

    HashMap<Date, Float> test = metrics.request(
        Metric.Impressions,
        data.getMinDate(),
        data.getMaxDate(),
        filter,
        Granularity.Second
    );

    long t3 = System.nanoTime();
    long d1 = (t2 - t1);
    long d2 = (t3 - t2);

    Logs masterLog = data.request();

    System.out.println(masterLog.impressionLogs.size());
    System.out.println(masterLog.clickLogs.size());

    System.out.println(test);

    System.out.println("Time 1: " + d1/1000000000.00);
    System.out.println("Time 2: " + d2/1000000000.00);
  }
}
