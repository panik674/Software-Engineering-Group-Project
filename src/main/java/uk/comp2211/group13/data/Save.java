package uk.comp2211.group13.data;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import org.json.simple.JSONObject;

/**
 * This class is responsible for the saving and loading operations of the application
 */
public class Save {
    public static HashSet<Filter> filters;
    public static HashSet<Metric> metrics;
    public static Granularity granularity;
    public static Date startDate;
    public static Date endDate;


    /**
     * This function receives the customisable objects that a user may want to save, turns them into a JSON object and saves it into a respective file
     *
     * @param filters HashSet of all filters applied to the campaign
     * @param metrics HashSet of the metrics to which the filters are applied to
     * @param granularity Chosen granularity for the filters
     * @param startDate Start date
     * @param endDate End date
     */

    //TODO: Look into fetching the right directory for saving
    public static void saveFilters(HashSet<Filter> filters, HashSet<Metric> metrics, Granularity granularity, Date startDate, Date endDate) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filters", filters.toString());
        jsonObject.put("metrics", metrics.toString());
        jsonObject.put("granularity", granularity.toString());
        jsonObject.put("startDate", startDate.toString());
        jsonObject.put("endDate", endDate.toString());

        try {
            String workingDir = getDirectory();
            FileWriter file = new FileWriter(workingDir);
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public boolean checkFile() {
//        Path paths = Paths.get()
//    }

    // TODO: Handle cases where the file might not exist, without crashing.
    public static void loadFilters() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(getDirectory()));
            JSONObject jsonObject = (JSONObject) obj;
            filters = (HashSet<Filter>) jsonObject.get("filters");
            metrics = (HashSet<Metric>) jsonObject.get("metrics");
            granularity = (Granularity) jsonObject.get("Granularity");
            startDate = (Date) jsonObject.get("startDate");
            endDate = (Date) jsonObject.get("endDate");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getDirectory(){
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        return currentWorkingDir.normalize().toString()+"\\src\\main\\resources\\save.json";
    }

//    public HashSet<Filter> getFilters() {
//        return filters;
//    }
//
//    public HashSet<Metric> getMetrics() {
//        return metrics;
//    }
//
//    public Granularity getGranularity() {
//        return granularity;
//    }
//
//    public Date getStartDate(){
//        return startDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
}
