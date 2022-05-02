package uk.comp2211.group13.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONObject;
import uk.comp2211.group13.scenes.LoadingScene;
import uk.comp2211.group13.scenes.WelcomeScene;

/**
 * This class is responsible for the saving and loading operations of the application
 */
public class Save {
    private static final Logger logger = LogManager.getLogger(LoadingScene.class);
    public static HashMap<Filter, String[]> genderFilters;
    public static HashMap<Filter, String[]> ageFilters;
    public static HashMap<Filter, String[]> incomeFilters;
    public static HashMap<Filter, String[]> contextFilters;
    public static Date startDate;
    public static Date endDate;
    public static Granularity granularity;


    /**
     * This function is responsible for transforming Filter information to JSON objects and writing them to individual files.
     *
     * @param genderFilters - HashMap containing gender filters and their subsequent values.
     * @param ageFilters - HashMap containing age filters and their subsequent values.
     * @param incomeFilters - HashMap containing income filters and their subsequent values.
     * @param contextFilters - HashMap containing context filters and their subsequent values.
     * @param startDate - The start Date of the period.
     * @param endDate - The end Date of the period.
     * @param granularity - Desired Granularity.
     */
    public static void saveFilters(HashMap<Filter, String[]> genderFilters, HashMap<Filter, String[]> ageFilters, HashMap<Filter, String[]> incomeFilters, HashMap<Filter, String[]> contextFilters, Date startDate, Date endDate, Granularity granularity) {
        JSONObject genderJson = mapToJson(genderFilters);
        JSONObject ageJson = mapToJson(ageFilters);
        JSONObject incomeJson = mapToJson(incomeFilters);
        JSONObject contextJson = mapToJson(contextFilters);
        JSONObject dateJson = new JSONObject();
        dateJson.put("startDate", startDate.toString());
        dateJson.put("endDate", endDate.toString());
        JSONObject granularityJson = new JSONObject();
        granularityJson.put("granularity", granularity.toString());
        logger.info("saving...");
        try {
            String workingDir = getDirectory();
            FileWriter genderFile = new FileWriter(workingDir + "gender.json");
            FileWriter ageFile = new FileWriter(workingDir + "age.json");
            FileWriter incomeFile = new FileWriter(workingDir + "income.json");
            FileWriter contextFile = new FileWriter(workingDir + "context.json");
            FileWriter dateFile = new FileWriter(workingDir + "date.json");
            FileWriter granularityFile = new FileWriter(workingDir + "granularity.json");

            genderFile.write(genderJson.toJSONString());
            genderFile.close();

            ageFile.write(ageJson.toJSONString());
            ageFile.close();

            incomeFile.write(incomeJson.toJSONString());
            incomeFile.close();

            contextFile.write(contextJson.toJSONString());
            contextFile.close();

            dateFile.write(dateJson.toJSONString());
            dateFile.close();

            granularityFile.write(granularityJson.toJSONString());
            granularityFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     *  Function responsible for the reading of the saved filters and subsequent relay to the requesting function.
     *
     */
    public static void loadFilters() {
        String workingDir = getDirectory();

        JSONParser genderParser = new JSONParser();
        JSONParser ageParser = new JSONParser();
        JSONParser incomeParser = new JSONParser();
        JSONParser contextParser = new JSONParser();
        JSONParser dateParser = new JSONParser();
        JSONParser granularityParser = new JSONParser();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            Object gender = genderParser.parse(new FileReader(workingDir + "gender.json"));
            Object age = ageParser.parse(new FileReader(workingDir + "age.json"));
            Object income = incomeParser.parse(new FileReader(workingDir + "income.json"));
            Object context = contextParser.parse(new FileReader(workingDir + "context.json"));
            Object date = dateParser.parse(new FileReader(workingDir + "date.json"));
            Object granularityObj = granularityParser.parse(new FileReader(workingDir + "granularity.json"));

//
            genderFilters = jsonToMap((JSONObject) gender);
            ageFilters = jsonToMap((JSONObject) age);
            incomeFilters = jsonToMap((JSONObject) income);
            contextFilters = jsonToMap((JSONObject) context);
            JSONObject dateJson = (JSONObject) date;
            JSONObject granularityJson = (JSONObject) granularityObj;

            startDate = formatter.parse((String) dateJson.get("startDate"));
            endDate = formatter.parse((String) dateJson.get("endDate"));
            granularity = Granularity.valueOf(granularityJson.get("granularity").toString());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


    }


    /**
     * Helper function to get the recourses directory of the project in the running computer
     * @return
     */
    public static String getDirectory() {
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        return currentWorkingDir.normalize().toString() + "\\src\\main\\resources\\";
    }


    /**
     * Helper function to convert HashMap<Filter, String[]> to JsonObjects
     * @param filters HashMap to be converted
     * @return JSONObject encoded with the HashMap's components.
     */
    private static JSONObject mapToJson(HashMap<Filter, String[]> filters) {
        JSONObject json = new JSONObject();
        for (Filter filter : filters.keySet()) {
            json.put(filter, Arrays.asList(filters.get(filter)));
        }
        return json;
    }

    /**
     * Helper function to transform JSONObjects to HashMap<Filer, String[]>.
     * @param jsonObject The JSONObject to be transformed.
     * @return The HashMap parsed by the JSONObject.
     */
    private static HashMap<Filter, String[]> jsonToMap(JSONObject jsonObject) {
        HashMap<Filter, String[]> map = new HashMap<>();
        for (Object key : jsonObject.keySet()) {
            Filter s = Filter.valueOf(key.toString());
            List<String> list = (List<String>) jsonObject.get(key);
            String[] arr = new String[list.size()];
            list.toArray(arr);

            map.put(s, arr);
        }
        return map;
    }

}