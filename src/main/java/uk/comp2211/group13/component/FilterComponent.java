package uk.comp2211.group13.component;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.CheckBox;
import java.time.Instant;
import java.util.Date;

public class FilterComponent extends StackPane {
    protected VBox vbox = new VBox();
    private Accordion filterAccord = new Accordion();
    private RadioButton hour = new RadioButton("Hour");
    private RadioButton day = new RadioButton("Day");
    private RadioButton month = new RadioButton("Month");
    private RadioButton year = new RadioButton("Year");
    private ToggleGroup granToggle = new ToggleGroup();
    private CheckBox male = new CheckBox("Male");
    private CheckBox female = new CheckBox("Female");
    private CheckBox ageRange1 = new CheckBox("<25");
    private CheckBox ageRange2 = new CheckBox("25-34");
    private CheckBox ageRange3 = new CheckBox("35-44");
    private CheckBox ageRange4 = new CheckBox("45-54");
    private CheckBox ageRange5 = new CheckBox(">54");
    private CheckBox low = new CheckBox("Low");
    private CheckBox medium = new CheckBox("Medium");
    private CheckBox high = new CheckBox("High");
    private CheckBox news = new CheckBox("News");
    private CheckBox shopping = new CheckBox("Shopping");
    private CheckBox sMedia = new CheckBox("Social Media");
    private CheckBox blog = new CheckBox("Blog");
    private CheckBox hobby = new CheckBox("Hobby");
    private CheckBox travel = new CheckBox("Travel");
    private String filtCompType;
    private Button updateButton;
    private String[] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounce Pages", "Number of Bounce Visits", "Rate of Conversions", "Total Costs", "CTR", "CPA", "CPC", "CPM", "Bounce Visit Rate", "Bounce Page Rate"};
    private ComboBox<String> metricBox = new ComboBox<>(FXCollections.observableArrayList(metrics));
    private DatePicker startdp = new DatePicker();
    private DatePicker enddp = new DatePicker();
    private Spinner<Integer> visitSpnr = new Spinner<Integer>(1,10,5);
    private Spinner<Integer> pageSpnr = new Spinner<Integer>(1,10,5);
    private String[] GenderList = {};
    private String[] AgeList = {};
    private String[] IncomeList = {};
    private String[] ContextList = {};

    /**
     * Builds the filter component
     *
     * @param filtCompType - Determines what kind of filter component will be built, graph, histogram or overview
     */
    public FilterComponent(String filtCompType){
        this.filtCompType=filtCompType;
        updateButton = new Button("Update " + filtCompType);
        build();
    }

    /**
     * Builds all the sections of the filter component
     */
    public void build(){
        //Putting all the granularity radio buttons in the same toggle group
        hour.setToggleGroup(granToggle);
        day.setToggleGroup(granToggle);
        month.setToggleGroup(granToggle);
        year.setToggleGroup(granToggle);
        //Building accordions for the time granularity and filters
        accordBuild("Time Granularity",new VBox(hour,day,month,year));
        accordBuild("Gender",new VBox(male,female));
        accordBuild("Age", new VBox(ageRange1,ageRange2,ageRange3,ageRange4,ageRange5));
        accordBuild("Income", new VBox(low,medium,high));
        accordBuild("Context", new VBox(news,shopping,sMedia,blog,hobby,travel));
        //Setting vbox spacing
        vbox.setSpacing(20);
        //Adding title label to component
        vbox.getChildren().add(new HBox(regionBuild(),new Label("Filters and Settings"),regionBuild()));
        //Calling the dropdowns to add the necessary widgets based on the type of filter component
        dropdowns(filtCompType);
        //Adding the accordion and the update button to the vbox
        vbox.getChildren().add(filterAccord);
        vbox.getChildren().add(new HBox(regionBuild(), updateButton, regionBuild()));
        //Adding the vbox to the stackpane
        getChildren().add(vbox);



    }

    /**
     * Builds a titled pane and adds it to the filter accordion
     *
     * @param filterLabel - Label for the titled pane
     * @param filterVBox - VBox to hold the titled pane
     */
    public void accordBuild(String filterLabel, VBox filterVBox){
        TitledPane filterPane = new TitledPane(filterLabel,filterVBox);
        filterAccord.getPanes().add(filterPane);
    }

    /**
     *Method to Build and grow regions to provide spacing for the filter HBox
     *
     * @return - The grown region
     */
    public Region regionBuild(){
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    /**
     *Builds the necessary widgets based on the type of filter component
     *
     * @param compType - The component type string which is used to determine which widgets are to be built
     */
    public void dropdowns(String compType) {
        switch (compType){
            case "Graph":
                //Setting default metricbox value
                metricBox.setValue(metrics[0]);
                //Adding questionmark and the metric box to an hbox and then adding that to the vbox
                vbox.getChildren().add(new HBox(questionMarkLabel("Select a Metric","Some Tip"), regionBuild(), metricBox));
                //Calling the dateFilters and bounceFilters methods to build the start and end datepickers and the bounce spinners
                dateFilters();
                bounceFilters();
                break;

            case "Histogram":
                //Calling the dateFilters method to build the start and end datepickers
                dateFilters();
                break;

            case "Overview" :
                //Calling the bounceFilters methods to build the bounce spinners
                bounceFilters();
        }
    }


    /**
     * Builds the spinner for the visit bounce time and the page bounce limit
     *
     * @return - The spinner
     */
    public Spinner<Integer> bounceSpinner(){
        Spinner<Integer> spnr = new Spinner<Integer>(1,10,5);
        return spnr;
    }

    /**
     * Adds the start and end datepickers to the vbox
     */
    public void dateFilters(){
        vbox.getChildren().add(new HBox(questionMarkLabel("Start Date","Some Tip"), regionBuild(), startdp));
        vbox.getChildren().add(new HBox(questionMarkLabel("End Date","Some Tip"), regionBuild(), enddp));
    }

    /**
     * Adds the visit bounce time and the page bounce limit to the vbox
     */
    public void bounceFilters(){
        vbox.getChildren().add(new HBox(questionMarkLabel("Define Visit Bounce Time","Some Tip"), regionBuild(), bounceSpinner()));
        vbox.getChildren().add(new HBox(questionMarkLabel("Define Page Bounce Limit    ","Some Tip"), regionBuild(), bounceSpinner()));
    }

    /**
     * Creates a label with text and a questionmark image with a tooltip
     *
     * @param labelText - Text for the label
     * @param tip - Text for the tip
     * @return - The label
     */
    public Label questionMarkLabel(String labelText, String tip){
        //Creating an image object with the questionmark icon image
        Image qmark = new Image("question.png");
        //Creating an imageview and setting qmark as its image
        ImageView imageView = new ImageView();
        imageView.setImage(qmark);
        //Creating a label comprised of the parsed text and the imageview
        Label qLabel = new Label(labelText,imageView);
        //Setting a tooltip to the qlabel
        qLabel.setTooltip(new Tooltip(tip));
        //Returning the qLabel
        return qLabel;
    }

    /**
     * Getter method for the metricbox's value
     *
     * @return - The metricbox's value
     */
    public String getMetric(){
        return metricBox.getValue();
    }

    /**
     * Getter method for the start datepicker's value
     *
     * @return - The start datepicker's value
     */
    public Date getStartDate(){
        return Date.from(Instant.from(startdp.getValue()));
    }

    /**
     * Getter method for the end datepicker's value
     *
     * @return - The end datepicker's value
     */
    public Date endStartDate(){
        return Date.from(Instant.from(enddp.getValue()));
    }

    /**
     * Getter method for the visit spinner
     *
     * @return - The visit spinner
     */
    public Integer getBounceVisit(){
        return visitSpnr.getValue();
    }

    /**
     * Getter method for the bounce spinner
     *
     * @return - The bounce spinner
     */
    public Integer getBouncePage(){
        return pageSpnr.getValue();
    }

    /**
     * Getter method for the selected time granularity radio button text
     *
     * @return - The text from the selected radio button
     */
    public String getGranularity(){
        return ((RadioButton) granToggle.getSelectedToggle()).getText();
    }

    /**
     * Getter method for the gender list
     *
     * @return - The gender list
     */
    public String[] getGendersList(){
        return GenderList;
    }

    /**
     * Getter method for the age list
     *
     * @return - The age list
     */
    public String[] getAgesList(){
        return AgeList;
    }

    /**
     * Getter method for the income list
     *
     * @return - The income list
     */
    public String[] getIncomeList(){
        return IncomeList;
    }

    /**
     * Getter method for the context list
     *
     * @return - The context list
     */
    public String[] getContextList(){
        return ContextList;
    }


}
