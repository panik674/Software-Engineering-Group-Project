package uk.comp2211.group13.component;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.CheckBox;
import uk.comp2211.group13.enums.Granularity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FilterComponent extends StackPane {
    protected VBox vbox = new VBox();
    private HBox filterhbox = new HBox();
    private HBox saveloadhbox = new HBox();
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
    private CheckBox socialMedia = new CheckBox("Social Media");
    private CheckBox blog = new CheckBox("Blog");
    private CheckBox hobby = new CheckBox("Hobby");
    private CheckBox travel = new CheckBox("Travel");
    private String filtCompType;
    private Button updateButton;
    private Button resetButton;
    private Button saveButton;
    private Button loadButton;
    private String[] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounce Pages", "Number of Bounce Visits", "Rate of Conversions", "Total Costs", "CTR", "CPA", "CPC", "CPM", "Bounce Visit Rate", "Bounce Page Rate"};
    private ComboBox<String> metricBox = new ComboBox<>(FXCollections.observableArrayList(metrics));
    private DatePicker startdp = new DatePicker();
    private DatePicker enddp = new DatePicker();
    private Spinner<Integer> visitSpnr;
    private Spinner<Integer> pageSpnr;
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
        male.fire();
        female.fire();
        ageRange1.fire();
        ageRange2.fire();
        ageRange3.fire();
        ageRange4.fire();
        ageRange5.fire();
        low.fire();
        medium.fire();
        high.fire();
        news.fire();
        shopping.fire();
        socialMedia.fire();
        blog.fire();
        hobby.fire();
        travel.fire();
        updateButton = new Button("Update " + filtCompType);
        resetButton = new Button("Reset Filters");
        saveButton = new Button("Save Filters");
        loadButton = new Button("Load Filters");
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
        day.fire();
        //Building accordions for the time granularity and filters
        if (filtCompType != "Overview") {
            accordBuild("Time Granularity", new VBox(hour, day, month, year));
        }
        accordBuild("Gender",new VBox(male,female));
        accordBuild("Age", new VBox(ageRange1,ageRange2,ageRange3,ageRange4,ageRange5));
        accordBuild("Income", new VBox(low,medium,high));
        accordBuild("Context", new VBox(news,shopping,socialMedia,blog,hobby,travel));
        //Setting vbox spacing
        vbox.setSpacing(20);
        //Adding title label to component
        vbox.getChildren().add(new HBox(regionBuild(),new Label("Filters and Settings"),regionBuild()));
        //Calling the dropdowns to add the necessary widgets based on the type of filter component
        dropdowns(filtCompType);
        //Adding the accordion and the update button to the vbox
        vbox.getChildren().add(filterAccord);
        updateButton.setTooltip(new Tooltip("This button will apply the filters to the displayed data."));
        resetButton.setTooltip(new Tooltip("This button resets the filter settings to default."));
        saveButton.setTooltip(new Tooltip("This button saves the current filter settings."));
        loadButton.setTooltip(new Tooltip("This button loads the saved filter settings,"));
        filterhbox.getChildren().add(regionBuild());
        filterhbox.getChildren().add(updateButton);
        filterhbox.getChildren().add(resetButton);
        filterhbox.getChildren().add(regionBuild());
        vbox.getChildren().add(filterhbox);
        saveloadhbox.getChildren().add(regionBuild());
        saveloadhbox.getChildren().add(saveButton);
        saveloadhbox.getChildren().add(loadButton);
        saveloadhbox.getChildren().add(regionBuild());
        vbox.getChildren().add(saveloadhbox);
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
                vbox.getChildren().add(new HBox(questionMarkLabel("Select a Metric","Select the metric to be displayed."), regionBuild(), metricBox));
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
                break;
        }
    }


    /**
     * Builds the spinner for the visit bounce time and the page bounce limit
     *
     * @return - The spinner
     */
    public Spinner<Integer> bounceSpinner(int minimumValue, int maximumValue, int initialValue){
        Spinner<Integer> spnr = new Spinner<Integer>(minimumValue,maximumValue,initialValue);
        return spnr;
    }

    /**
     * Adds the start and end datepickers to the vbox
     */
    public void dateFilters(){
        vbox.getChildren().add(new HBox(questionMarkLabel("Start Date","Select start date of the time period to be displayed."), regionBuild(), startdp));
        vbox.getChildren().add(new HBox(questionMarkLabel("End Date","Select end date of the time period to be displayed."), regionBuild(), enddp));
    }

    /**
     * Adds the visit bounce time and the page bounce limit to the vbox
     */
    public void bounceFilters(){
        visitSpnr = bounceSpinner(0,10000,15);
        pageSpnr = bounceSpinner(0,1000,1);
        vbox.getChildren().add(new HBox(questionMarkLabel("Define Visit Bounce Time (s)","Visit bound defined by time (in seconds) user visits website, values less or equal than this setting are defined as a bounce. (default: 15)"), regionBuild(), visitSpnr));
        vbox.getChildren().add(new HBox(questionMarkLabel("Define Page Bounce Limit    ","Visit bound defined by pages a user visits on a website, values less or equal than this setting are defined as a bounce. (default: 1)"), regionBuild(), pageSpnr));
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
        return java.util.Date.from(startdp.getValue().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Getter method for the end datepicker's value
     *
     * @return - The end datepicker's value
     */
    public Date getEndDate(){
        return java.util.Date.from(enddp.getValue().atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Checks all checkboxes
     */
    public void resetCheckBoxes() {
        male.setSelected(true);
        female.setSelected(true);
        ageRange1.setSelected(true);
        ageRange2.setSelected(true);
        ageRange3.setSelected(true);
        ageRange4.setSelected(true);
        ageRange5.setSelected(true);
        low.setSelected(true);
        medium.setSelected(true);
        high.setSelected(true);
        news.setSelected(true);
        shopping.setSelected(true);
        socialMedia.setSelected(true);
        blog.setSelected(true);
        hobby.setSelected(true);
        travel.setSelected(true);
    }

    public void setCheckBoxes(String[] gender, String[] age, String[] income, String[] contextList, Granularity granularity) {
        male.setSelected(false);
        female.setSelected(false);
        ageRange1.setSelected(false);
        ageRange2.setSelected(false);
        ageRange3.setSelected(false);
        ageRange4.setSelected(false);
        ageRange5.setSelected(false);
        low.setSelected(false);
        medium.setSelected(false);
        high.setSelected(false);
        news.setSelected(false);
        shopping.setSelected(false);
        socialMedia.setSelected(false);
        blog.setSelected(false);
        hobby.setSelected(false);
        travel.setSelected(false);

        if (granularity.equals(Granularity.Hour)) {
            hour.fire();
        } else if (granularity.equals(Granularity.Day)) {
            day.fire();
        } else if (granularity.equals(Granularity.Month)) {
            month.fire();
        }

        if (gender != null) {
            for (String genderVal : gender) {
                switch (genderVal) {
                    case ("Male"):
                        male.setSelected(true);
                        break;
                    case ("Female"):
                        female.setSelected(true);
                        break;
                }
            }
        }

        if (age != null) {
            for (String ageVal : age) {
                switch (ageVal) {
                    case ("<25"):
                        ageRange1.setSelected(true);
                        break;
                    case ("25-34"):
                        ageRange2.setSelected(true);
                        break;
                    case ("35-44"):
                        ageRange3.setSelected(true);
                        break;
                    case ("45-54"):
                        ageRange4.setSelected(true);
                        break;
                    case (">54"):
                        ageRange5.setSelected(true);
                        break;
                }
            }
        }

        if (income != null) {
            for (String incomeVal : income) {

                switch (incomeVal) {
                    case ("Low"):
                        low.setSelected(true);
                        break;
                    case ("Medium"):
                        medium.setSelected(true);
                        break;
                    case ("High"):
                        high.setSelected(true);
                        break;
                }
            }
        }

        if (contextList != null) {

            for (String contextVal : contextList) {
                switch (contextVal) {
                    case ("Shopping"):
                        shopping.setSelected(true);
                        break;
                    case ("Social Media"):
                        socialMedia.setSelected(true);
                        break;
                    case ("Blog"):
                        blog.setSelected(true);
                        break;
                    case ("Hobbies"):
                        hobby.setSelected(true);
                        break;
                    case ("Travel"):
                        travel.setSelected(true);
                        break;
                    case ("News"):
                        news.setSelected(true);
                        break;
                }
            }
        }
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
     * Getter method for the male filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getMaleFilter(){
        return male.isSelected();
    }

    /**
     * Getter method for the male filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getFemaleFilter(){
        return female.isSelected();
    }

    /**
     * Getter method for the first age range filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getAgeRange1Filter(){
        return ageRange1.isSelected();
    }

    /**
     * Getter method for the second age range filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getAgeRange2Filter(){
        return ageRange2.isSelected();
    }

    /**
     * Getter method for the third age range filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getAgeRange3Filter(){
        return ageRange3.isSelected();
    }

    /**
     * Getter method for the fourth age range filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getAgeRange4Filter(){
        return ageRange4.isSelected();
    }

    /**
     * Getter method for the fifth age range filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getAgeRange5Filter(){
        return ageRange5.isSelected();
    }

    /**
     * Getter method for the low income filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getLowIncomeFilter(){
        return low.isSelected();
    }

    /**
     * Getter method for the medium income filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getMediumIncomeFilter(){
        return medium.isSelected();
    }

    /**
     * Getter method for the high income filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getHighIncomeFilter(){
        return high.isSelected();
    }

    /**
     * Getter method for the news filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getNewsFilter(){
        return news.isSelected();
    }

    /**
     * Getter method for the shopping filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getShoppingFilter(){
        return shopping.isSelected();
    }

    /**
     * Getter method for the social media filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getsocialMediaFilter(){
        return socialMedia.isSelected();
    }

    /**
     * Getter method for the blog filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getBlogFilter(){
        return blog.isSelected();
    }

    /**
     * Getter method for the hobby filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getHobbyFilter(){
        return hobby.isSelected();
    }

    /**
     * Getter method for the travel filter
     *
     * @return - True or false depending on if the filter is selected
     */
    public boolean getTravelFilter(){
        return travel.isSelected();
    }

    /**
     * Setter method for the start date
     *
     * @param startDate - The date to set the start date to
     */
    public void setStartDate(LocalDate startDate){
        startdp.setValue(startDate);
    }

    /**
     * Setter method for the end date
     *
     * @param endDate - The date to set the end date to
     */
    public void setEndDate(LocalDate endDate){
        enddp.setValue(endDate);
    }

    /**
     * Getter method for the update button
     *
     * @return - The update button
     */
    public Button getUpdateButton(){
        return updateButton;
    }

    /**
     * Getter method for the reset button
     *
     * @return - The reset button
     */
    public Button getResetButton(){
        return resetButton;
    }

    /**
     * Getter method for the save button
     *
     * @return - The save button
     */
    public Button getSaveButton(){
        return saveButton;
    }

    /**
     * Getter method for the load button
     *
     * @return - The load button
     */
    public Button getLoadButton() {return loadButton;}
    /**
     * Fires the day radio button
     */
    public void fireDay(){
        day.fire();
    }

    /**
     * Resets the visit spinner
     */
    public void resetVisitSpnr(){
        visitSpnr.getValueFactory().setValue(15);
    }

    /**
     * Resets the page spinner
     */
    public void resetPageSpnr(){
        pageSpnr.getValueFactory().setValue(1);
    }

}
