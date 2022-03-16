package uk.comp2211.group13.data;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.io.*;
import java.util.Date;
import java.util.HashSet;
import org.json.simple.JSONObject;

/**
 * This class is responsible for the saving and loading operations of the application
 */
public class Save {
    private HashSet<Filter> filters;
    private HashSet<Metric> metrics;
    private Granularity granularity;
    private Date startDate;
    private Date endDate;


    /**
     * This function receives the customisable objects that a user may want to save, turns them into a JSON object and saves it into a respective file
     * @param filters
     * @param metrics
     * @param granularity
     * @param startDate
     * @param endDate
     */

    //TODO: Look into fetching the right directory for saving
    public void saveFilters(HashSet<Filter> filters, HashSet<Metric> metrics, Granularity granularity, Date startDate, Date endDate) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filters", filters.toString());
        jsonObject.put("metrics", metrics.toString());
        jsonObject.put("granularity", granularity.toString());
        jsonObject.put("startDate", startDate.toString());
        jsonObject.put("endDate", endDate.toString());

        try {
            FileWriter file = new FileWriter("E:/save.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Handle cases where the file might not exist, without crashing.
    public void loadFilters(String filepath) {
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader(filepath));
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
}
