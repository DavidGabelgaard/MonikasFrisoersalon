package com.example.monikasfrisoersalon;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.text.Normalizer;
import java.time.Duration;




import java.io.IOException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Integer.parseInt;


public class MainPageController {



    private Label selectedLabel;
    public AnchorPane topBar;
    public Text username;
    public VBox MON;
    public VBox TUE;
    public VBox WED;
    public VBox THU;
    public VBox FRI;
    public VBox SAT;
    public VBox SUN;

    public Label startWorkTime;
    public Label endWorkTime;

    public ComboBox treatments;


    public AnchorPane task1;
    public AnchorPane task2;
    public AnchorPane task3;
    public AnchorPane task4;
    public AnchorPane background_SeeAndBook;
    public AnchorPane orderPanel;

    public Text TodaysDate;
    public Text dateAndMonth;
    public Text month;
    public Text year;

    public ScrollPane scrollPane;

    public HBox h_currentMonth;
    public HBox h_NextMonth;
    public HBox h_lastMonth;

    public Polygon nextMonth;
    public Polygon lastMonth;

    public boolean creatingOrder = false;


    public VBox[] days;
    public WorkDay currentWorkDay;
    public Stage popUp;




    public int currentYearDisplayed;
    public int currentMonthDisplayed;

    public ArrayList<Treatments> allTreatments = new ArrayList<>();

    public ArrayList<Label> dates = new ArrayList<>();

    public GenerateMonth g_previousMonth;
    public GenerateMonth g_currentMonth;
    public GenerateMonth g_NextMonth;

    private int index;
    private int daysUntilMonthBeginsInCalender;

    private double xOffset;
    private double yOffset;


    public void initialize() {


        currentMonthDisplayed = LocalDate.now().getMonthValue();
        currentYearDisplayed = LocalDate.now().getYear();

        username.setText(Formatter.bigFirstLetter(Formatter.formatUserName(DBController.getActiveUser())));

        g_previousMonth = new GenerateMonth(LocalDate.now().getYear() , LocalDate.now().getMonthValue() - 1 );
        g_NextMonth = new GenerateMonth(LocalDate.now().getYear() , LocalDate.now().getMonthValue() + 1);
        g_currentMonth = new GenerateMonth(LocalDate.now().getYear() , LocalDate.now().getMonthValue());

        populateCalender(g_previousMonth , h_lastMonth);
        populateCalender(  g_NextMonth, h_NextMonth);
        populateCalender(g_currentMonth, h_currentMonth);

        selectDateOfToday();

        populateAndGetTreatments();
        populateHairdressers();

    }
    

    public WorkDay getWorkDayFromLabel(Label date) {

        int _day = parseInt( date.getText());


        return g_currentMonth.getDays().get(_day -1 );
    }


    //region Booking

    public HBox f_Table;
    public HBox e_Table;

    public ComboBox workers;

    public AnchorPane datePane;

    public LocalTime selectTime;
    public Label selectedLabelTime;
    public LocalDate date;

    public TextField dd;
    public TextField mm;
    public TextField yy;


    public TextField clientName;
    public TextField clientNumber;

    public Text treatmentTime;
    public Text treatmentPrice;
    public Label orderCreated;

    public AnchorPane earlyDay;
    public AnchorPane laterday;

    public ArrayList<LocalTime> f_Time = new ArrayList<>();
    public ArrayList<LocalTime> e_Time = new ArrayList<>();

    private boolean f_DayOpen = false;
    private boolean e_DayOpen = false;

    private boolean f_DayDisable = false;
    private boolean e_dayDisable = false;

    @FXML
    public void choseDisplayTime(MouseEvent event) {

        Node n = (Node) event.getSource();
        HBox container;
        boolean isOpen;

        ArrayList<LocalTime> times;

        if (n.getId().equals("earlyDay")) {

            if (!f_DayDisable) {
                container = f_Table;
                isOpen = f_DayOpen;
                f_DayOpen = !f_DayOpen;
                times = f_Time;

                displayTime(n , isOpen , times , container);
            }
        } else {
            if (!e_dayDisable) {
                container = e_Table;
                isOpen = e_DayOpen;
                e_DayOpen = !e_DayOpen;
                times = e_Time;

                displayTime(n , isOpen , times , container);
            }
        }


    }

    public void displayTime(Node n , boolean isOpen , ArrayList<LocalTime> times , HBox container  ) {
        AnchorPane a = (AnchorPane) n;

        FontAwesomeIconView fontAwesomeIconView = (FontAwesomeIconView) a.getChildren().get(1);


        if (!isOpen ) {
            // Open it

            fontAwesomeIconView.setGlyphName("ARROW_DOWN");

            UpdateTimesDisplayed(container , times);


        } else {
            //close it
            fontAwesomeIconView.setGlyphName("ARROW_UP");


            UpdateTimesDisplayed(container , new ArrayList<>() );

        }

        isOpen = !isOpen;

    }

    public void UpdateTimesDisplayed(HBox container , ArrayList<LocalTime> times) {

        VBox[] vBoxes =   { (VBox)container.getChildren().get(0) , (VBox)container.getChildren().get(1) , (VBox)container.getChildren().get(2) ,(VBox) container.getChildren().get(3) };



        int desiredSize;

        if (times.size() % 4 == 0) {
           desiredSize = (times.size() / 4 ) * 65;
        } else {
            desiredSize = ((times.size() / 4) + 1) * 65;
        }

        for (int i = 0; i < vBoxes.length; i++) {



            Timeline timeline = new Timeline();

            timeline.getKeyFrames().add(new KeyFrame(
                    javafx.util.Duration.seconds(0.25f) , new KeyValue(vBoxes[i].prefHeightProperty() , desiredSize)
            ));

            timeline.play();

            vBoxes[i].getChildren().clear();

            if (i == vBoxes.length - 1 ) {

            timeline.onFinishedProperty().set(e -> {

                for (int j = 0; j < times.size(); j++) {



                    try {
                        Label label = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Booking_time.fxml")));

                        label.setText(times.get(j).toString());

                        int index = (j % 4);

                        vBoxes[index].getChildren().add(label);


                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }

            }
            );
            }

        }

    }

    @FXML
    public void updateTreatmentComboBox() {
        updateTables();
        Treatments t =  allTreatments.get( getTreatmentIndexFromName( treatments.getValue().toString() ));
        treatmentTime.setText("Tid: " +  t.getTime().toString());
        treatmentPrice.setText("Pris: " +  t.getPrice() + "kr." );

    }

    @FXML
    public void updateTables() {

        selectTime = null;

        if (f_Time != null)
        f_Time.clear();

        if (e_Time != null)
        e_Time.clear();

        try {
            int d = parseInt(dd.getText());
            int m = parseInt(mm.getText());
            int y = parseInt(yy.getText());
        try {

            LocalDate date = LocalDate.of(y, m, d);

            validDate();

            String value;

            if (workers.getValue() != null) {
                value = workers.getValue().toString();
            } else {
                value = workers.getPromptText().toString();
            }

            if (!value.equals("ANY")) {

                WorkDay workDay = DBController.getWorkDayFromUserWithDate(date, workers.getValue().toString());

                for (ShiftTime shiftTime : workDay.getShiftTimes()) {

                    LocalTime time = shiftTime.getStartTime();

                    String treatmentString;

                    if (treatments.getValue() == null) {
                        treatmentString = treatments.getPromptText();
                    } else {
                        treatmentString = treatments.getValue().toString();
                    }


                    Duration treatmentDuration = Duration.between(LocalTime.of(0, 0),
                            allTreatments.get(getTreatmentIndexFromName(treatmentString)).getTime());



                    while (time.isBefore(shiftTime.getEndTime()) || time == shiftTime.getEndTime()) {

                        if (validateTimeForOneWorker(workDay, time, treatmentDuration)) {

                            insertDateChoicesToArrayList(time);

                        }

                        time = time.plusMinutes(30);

                    }

            }

        }

            if (f_Time.size() == 0) {
                disableTheBoxDueToNoTimesAvailable(earlyDay , f_DayOpen , f_Time , f_Table);
                f_DayDisable = true;
                f_DayOpen = false;
            } else if (f_DayDisable){
                reEnableTheBoxDueToNowThereBeingOptions(earlyDay);
                f_DayDisable = false;
            }

            if (e_Time.size() == 0) {
                disableTheBoxDueToNoTimesAvailable(laterday , e_DayOpen , e_Time , e_Table);
                e_dayDisable = true;
                e_DayOpen = false;
            } else if (e_dayDisable){
                reEnableTheBoxDueToNowThereBeingOptions(laterday);
                e_dayDisable = false;
            }


            if (f_DayOpen)
                UpdateTimesDisplayed(f_Table , f_Time);


            if (e_DayOpen)
                UpdateTimesDisplayed(e_Table , e_Time);




        } catch (DateTimeException e) {
            notAValidDate();
        }
        } catch (NumberFormatException e) {
            notAValidDate();
        }
    }

    public void reEnableTheBoxDueToNowThereBeingOptions(AnchorPane place) {
        Text text = (Text) place.getChildren().get(0);
        text.setFill(Paint.valueOf("0x000000ff"));
    }

    public void disableTheBoxDueToNoTimesAvailable (AnchorPane place , boolean panelOpen , ArrayList<LocalTime> time , HBox table   ) {

        if (panelOpen) {
            displayTime((Node) place , true , time , table );
        }
        Text text = (Text) place.getChildren().get(0);
        text.setFill(Paint.valueOf("0x3c3c3cff"));
    }

    public void insertDateChoicesToArrayList(LocalTime time){

        if (time.isBefore(LocalTime.of(12 , 0))) {
            f_Time.add(time);
        } else {
            e_Time.add(time);
        }
    }

    private void validDate() {
        datePane.setStyle("-fx-border-width: 3px; -fx-border-color: transparent; ");
    }

    private void notAValidDate() {
        datePane.setStyle("-fx-border-width: 3px; "+
                "-fx-border-color: red;");
    }

    public void selectTime(Label time) {

        if (selectedLabelTime != null) {
            selectedLabelTime.setStyle("");
        }

        selectedLabelTime = time;

        selectedLabelTime.setStyle("-fx-border-width: 5px; -fx-border-color: #ffffff");


        String stringTime =  time.getText();

        LocalTime localTime = Formatter.stringToLocalTime(stringTime);

        selectTime = localTime;

    }

    public boolean validateTimeForOneWorker( WorkDay workDay, LocalTime time , java.time.Duration treatmentDuration )  {
        LocalTime endTime = time.plus(treatmentDuration);

        for (int i = 0; i < workDay.getShiftTimes().size(); i++) {

            if (endTime.isAfter(workDay.getShiftTimes().get(i).getEndTime())) {
                return  false;
            }

        }



            for (int i = 0; i < workDay.getOrders().size(); i++) {


                if (time == workDay.getOrders().get(i).getStartTime()) {
                    return false;
                }

                if (time.isAfter(workDay.getOrders().get(i).getStartTime()) && time.isBefore(workDay.getOrders().get(i).getStartTime().plus(workDay.getOrders().get(i).getDuration()))) {
                    return false;
                }




                if (endTime.isAfter(workDay.getOrders().get(i).getStartTime()) && endTime.isBefore(workDay.getOrders().get(i).getStartTime().plus(workDay.getOrders().get(i).getDuration()))) {
                    return false;
                }

                if (time.isBefore(workDay.getOrders().get(i).getStartTime())) {
                    if (endTime.isAfter(workDay.getOrders().get(i).getStartTime().plus(workDay.getOrders().get(i).getDuration()))) {
                        return false;
                    }
                    if (endTime == workDay.getOrders().get(i).getStartTime().plus(workDay.getOrders().get(i).getDuration())) {
                        return false;
                    }

                }






            }

        return true;
    }

    public void updateDateFieldWithSelectedDate() {

        dd.setText( Integer.toString(  currentWorkDay.getDate().getDayOfMonth())  );
        mm.setText(Integer.toString(currentWorkDay.getDate().getMonthValue()));
        yy.setText(Integer.toString(currentWorkDay.getDate().getYear()));


    }

    public void populateHairdressers() {

        ArrayList<Worker> w = DBController.getAllWorkers();

        workers.getItems().add("ANY");

        assert w != null;
        for (Worker wor: w ) {
            workers.getItems().add(wor.getUserName());
        }
    }

    public void populateAndGetTreatments() {

        allTreatments = DBController.getAllTreatments();
        for (Treatments t : allTreatments) {
            if (t != null) {
                treatments.getItems().add(t.getName());
            }
        }
    }


    public int getTreatmentIndexFromName(String name) {



        for (int i = 1; i < allTreatments.size(); i++) {



            if ( allTreatments.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }


    public boolean validateOrder() {

        boolean ret = true;

        if (workers.getValue() == null || workers.getValue().equals("ANY")) {
            makeNodeRed( workers);
            ret = false;
        } else {
            makeNodeRegular(workers);
        }

        try {

        int d = parseInt(dd.getText());
        int m = parseInt(mm.getText());
        int y = parseInt(yy.getText());

        makeNodeRegular(datePane);

        try {

            date = LocalDate.of(y, m, d);

            validDate();

        } catch (DateTimeException e) {
            notAValidDate();
            ret = false;
        }

        } catch (NumberFormatException e) {
            makeNodeRed(datePane);
            ret = false;
        }

        if (clientName.getText().equals("")) {
            makeNodeRed(clientName);
            ret = false;
        } else {
            makeNodeRegular(clientName);
        }


        if (clientNumber.getText().equals("")) {
            makeNodeRed(clientNumber);
            ret = false;
        } else {
            makeNodeRegular(clientNumber);
        }

        // validate time



        return  ret;
    }

    @FXML
    public void createOrder() {
        if (validateOrder()) {

            Worker w = new Worker(
                    workers.getValue().toString(),
                    null
            );

            ArrayList<Treatments> t = new ArrayList<>();

            t.add(getTreatmentFromComboBox());


        Orders orders = new Orders(
                selectTime,
                Duration.between(LocalTime.of(0 , 0),  getTreatmentFromComboBox().getTime()),
                t,
                w,
                date,
                clientName.getText(),
                clientNumber.getText(),
                null
        );

        DBController.createOrder(orders);

            clearTextFields();

            insertOrderIntoJava();

            showOrderCreated();

        }
    }

    public void showOrderCreated() {
        FadeTransition fadeTransition = new FadeTransition();

        fadeTransition.setNode(orderCreated);
        fadeTransition.setDuration(javafx.util.Duration.seconds(2));

        fadeTransition.setAutoReverse(true);

        fadeTransition.setFromValue(0);

        fadeTransition.setToValue(1);

        fadeTransition.setCycleCount(2);

        fadeTransition.play();



    }

    public void insertOrderIntoJava() {
        updateTables();

        if (date.getMonthValue() == g_currentMonth.getMonth()) {
            g_currentMonth = new GenerateMonth(currentYearDisplayed , currentMonthDisplayed);
        }

        if (date.getMonthValue() == g_NextMonth.getMonth()) {
            if (currentMonthDisplayed == 12 ) {
                g_NextMonth = new GenerateMonth(currentYearDisplayed + 1 , 1);
            } else {
                g_NextMonth = new GenerateMonth(currentYearDisplayed , currentMonthDisplayed - 1);
            }
        }

        if (date.getMonthValue() == g_previousMonth.getMonth()) {

            if (currentMonthDisplayed == 1 ) {
                g_previousMonth = new GenerateMonth(currentYearDisplayed - 1 , 12 );
            } else {
                g_previousMonth = new GenerateMonth(currentYearDisplayed , currentMonthDisplayed - 1);
            }
        }


        updateDailyToDoList();


    }

    public void clearTextFields() {

        clientName.setText("");
        clientNumber.setText("");



    }

    public Treatments getTreatmentFromComboBox() {

        if (treatments.getValue() != null) {
           return  allTreatments.get(   getTreatmentIndexFromName(treatments.getValue().toString()));
        } else {
            return allTreatments.get( getTreatmentIndexFromName(treatments.getPromptText()) );
        }



    }

    public void makeNodeRed(Node node) {


    node.setStyle("-fx-border-width: 3px;" + "-fx-border-color: red; ");

    }

    public void makeNodeRegular(Node node) {
        node.setStyle("");
    }

    //endregion


    //region Calender - Month

    public void selectDateOfToday() {
        selectDay( dates.get(LocalDate.now().getDayOfMonth() - 1 + daysUntilMonthBeginsInCalender ));
    }

    public void selectDay(Label date) {
        if (selectedLabel != null && currentWorkDay != null) {
            selectedLabel.getStyleClass().remove(0);
            styleLabelWithWorkDay(selectedLabel , currentWorkDay , "" );
        }
        date.getStyleClass().remove(0);
        date.getStyleClass().add(0 , "selected-date");
        selectedLabel = date;

        dateAndMonth.setText(date.getText() + ". " + Formatter.getMonthFromInt(currentMonthDisplayed) );
        TodaysDate.setText( Formatter.getDayFromLocalDate(LocalDate.of(currentYearDisplayed , currentMonthDisplayed , parseInt(date.getText()))));


        currentWorkDay = getWorkDayFromLabel((selectedLabel));
        updateDailyToDoList();

        updateDateFieldWithSelectedDate();

        if (creatingOrder) {
            updateTables();
        }
    }

    public void updateDailyToDoList() {
        updateWorkTime();
        updateWorkOrders();

    }

    public void updateWorkTime() {
        if (currentWorkDay.getShiftTimes().size() == 0) {
            startWorkTime.setText("Fri");
            endWorkTime.setText("Fri");
        } else if (currentWorkDay.getShiftTimes().size() == 1) {
            startWorkTime.setText(currentWorkDay.getShiftTimes().get(0).getStartTime().toString());
            endWorkTime.setText(currentWorkDay.getShiftTimes().get(0).getEndTime().toString());
        } else {

            boolean foundTime = false;

            for (ShiftTime times : currentWorkDay.getShiftTimes()) {
                if (times.getStartTime().isBefore(LocalTime.now()) && times.getEndTime().isAfter(LocalTime.now())) {
                    startWorkTime.setText(times.getStartTime().toString());
                    endWorkTime.setText(times.getEndTime().toString());
                    foundTime = true;
                }
            }
            if (!foundTime) {
                startWorkTime.setText(currentWorkDay.getShiftTimes().get(currentWorkDay.getShiftTimes().size() - 1 ).getStartTime().toString());
                startWorkTime.setText(currentWorkDay.getShiftTimes().get(currentWorkDay.getShiftTimes().size() - 1 ).getEndTime().toString());
            }
        }
    }

    public void updateWorkOrders() {

        AnchorPane[] tasks = new AnchorPane[] {task1 , task2 , task3 , task4};

        for (AnchorPane t:
                tasks) {
            t.setVisible(true);
        }
            int size;
        if (currentWorkDay.getOrders().size() > 4 ) {
            size = 4;
        } else {
            size = currentWorkDay.getOrders().size();
        }

        currentWorkDay.sortOrders();

            for (int i = 0; i < size ; i++) {

            Label time = (Label) tasks[i].getChildren().get(0);
            time.setText(currentWorkDay.getOrders().get(i).getStartTime().toString());

            Text treatment = (Text) tasks[i].getChildren().get(1);

            if (currentWorkDay.getOrders().get(i).getTreatments().size() > 1) {
                treatment.setText("Se mere");
            } else {

                if (currentWorkDay.getOrders().get(i).getTreatments().get(0).getName().length() > 17  ) {
                    String s = currentWorkDay.getOrders().get(i).getTreatments().get(0).getName();
                    String[] ss =  s.split(" ");
                    treatment.setText(ss[0] + " +");
                } else {
                    treatment.setText(currentWorkDay.getOrders().get(i).getTreatments().get(0).getName());
                }
            }
        }

        int amountThatCantBeDisplayed = tasks.length - currentWorkDay.getOrders().size();

        if (amountThatCantBeDisplayed > 0) {
            for (int i = tasks.length - 1; i >= currentWorkDay.getOrders().size() ; i--) {
                tasks[i].setVisible(false);
            }
        }
    }

    public void populateCalender(GenerateMonth generateMonth, HBox place) {

        dates.clear();

        days  = new VBox[7];

        for (int i = 0; i < days.length; i++) {
            days[i] = (VBox) place.getChildren().get(i);
            days[i].getChildren().clear();
        }

        DayOfWeek day = generateMonth.getStartDay();

        daysUntilMonthBeginsInCalender = day.getValue() - 1;

        for (int i = 1; i <= daysUntilMonthBeginsInCalender; i++) {

            int date = generateMonth.getDaysInTotalInLastMonth() - ( daysUntilMonthBeginsInCalender - i );

            insertDate(date , "previus-date", null , place);

        }

        for (int i = 0; i < generateMonth.getDays().size() ; i++) {
            insertDate(i + 1 , "regular-date" , generateMonth.getDays().get(i) , place);
        }

        int daysLeft = 42 - ( daysUntilMonthBeginsInCalender + generateMonth.getDays().size());

        for (int i = 1; i <= daysLeft ; i++) {
            insertDate(i , "next-date" , null, place);
        }





    }

    public void insertDate(int date , String type , WorkDay workDay , HBox place) {

        try {

            Label label = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Date.fxml")));

            label.setText(Integer.toString(date));

            styleLabelWithWorkDay(label , workDay , type);

            days [index % 7].getChildren().add(label);

            if (place.getId().equals("h_currentMonth")) {
                dates.add(label);
            }

            index ++;

                if (index >= 42) index = 0;




        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @FXML
    public void seeLastMonth(MouseEvent event) {

        if (previousMonthAvailable) {

            deactivateButtons();

            Animation animation = new Timeline(
                    new KeyFrame(javafx.util.Duration.seconds(0.5f),
                            new KeyValue(scrollPane.hvalueProperty(), scrollPane.getHvalue() - 0.5d)),
                    new KeyFrame(javafx.util.Duration.seconds(0.1d)));
            animation.play();
            animation.setOnFinished(e -> lastMonthAnimFinished());


        }
    }

    private void lastMonthAnimFinished() {
        if (currentMonthDisplayed == 1) {
            currentMonthDisplayed = 12;
            currentYearDisplayed --;
        } else {
            currentMonthDisplayed--;
        }

        month.setText(Formatter.getMonthFromInt(currentMonthDisplayed));
        year.setText(Integer.toString(currentYearDisplayed));

        populateCalender(g_previousMonth , h_currentMonth);
        scrollPane.setHvalue(0.5d);


        populateCalender(g_currentMonth , h_NextMonth);


        activateButton(nextMonth);


        g_NextMonth = g_currentMonth;

        g_currentMonth = g_previousMonth;

        if (currentMonthDisplayed == 1) {
            g_previousMonth = new GenerateMonth(currentYearDisplayed - 1  , 12 );
        } else {
            g_previousMonth = new GenerateMonth(currentYearDisplayed, currentMonthDisplayed - 1);
        }
        populateCalender( g_previousMonth , h_lastMonth);

        activateButton(lastMonth);

    }


    @FXML
    public void seeNextMonth(MouseEvent event) {

        if (nextMonthAvailable) {

            deactivateButtons();

            Animation animation = new Timeline(
                    new KeyFrame(javafx.util.Duration.seconds( 0.5f  ),
                            new KeyValue(scrollPane.hvalueProperty(), scrollPane.getHvalue() + 0.5d)));
            animation.play();
            animation.setOnFinished(e -> nextMonthAnimFinished() );

        }
    }

    public void nextMonthAnimFinished() {
        if (currentMonthDisplayed == 12) {
            currentMonthDisplayed = 1;
            currentYearDisplayed ++;
        } else {
            currentMonthDisplayed ++;
        }

        month.setText(Formatter.getMonthFromInt(currentMonthDisplayed));
        year.setText(Integer.toString(currentYearDisplayed));

        populateCalender(g_NextMonth , h_currentMonth);
        scrollPane.setHvalue(0.5d);

        populateCalender(g_currentMonth , h_lastMonth);

        activateButton(lastMonth);








    }




    //endregion

    //region visualControllers

    public void makeStageDraggable(MouseEvent mouseEvent) {
        topBar.requestFocus();
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();

        topBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        topBar.setOnMouseDragged(event -> {
            StartApplication.stage.setX(event.getScreenX() - xOffset);
            StartApplication.stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void styleLabelWithWorkDay(Label date, WorkDay workDay, String type) {

        if (type.equals("")) {
            type = "regular-date";
        }




        if ( workDay != null && workDay.getShiftTimes().size() == 0 )
            type = "free-date";

        date.getStyleClass().add(0 , type);

    }

    @FXML
    public void minimize() {
        StartApplication.stage.setIconified(true);
    }
    @FXML
    public void close () {
        StartApplication.stage.close();
    }

    //endregion

    //region PopUp
    public int popUpIndex;

    @FXML
    public void OrderPress(MouseEvent event) {
        Node n = (Node) event.getSource();
        String string = n.getId();
        string = string.substring(4,5);
        int index = parseInt(string) - 1;
        popUpIndex = index;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("PopUp.fxml"));
            Scene scene = new Scene(fxmlLoader.load() , 500 , 540);

            Stage s = new Stage();
            s.setScene(scene);
            s.setResizable(false);
            s.initStyle(StageStyle.TRANSPARENT);
            s.show();


            popUp = s;




        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void historyPress() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("History.fxml"));
            Scene scene = new Scene(fxmlLoader.load() , 798 , 540);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closePopUp() {
        if (popUp != null) {
            popUp.close();
        }
    }


    //endregion

    //region Visual

    private boolean canChange = true;

    public void seeAndOrder(MouseEvent event) {

        if (canChange) {

            canChange = false;

        Node node = (Node) event.getSource();

        int height = 0;
        int blockPlace = 0;


        if (!creatingOrder && node.getId().equals("_bestilling") ) {
            height = 84;
            blockPlace = 479;
        }
            TranslateTransition transition = new TranslateTransition();

            transition.setDuration(javafx.util.Duration.seconds(0.3f));
            transition.setNode(background_SeeAndBook);
            transition.setAutoReverse(false);

            transition.setToY(height);

            TranslateTransition block = new TranslateTransition();

            block.setDuration(javafx.util.Duration.seconds(0.5f));
            block.setNode(orderPanel);
            block.setAutoReverse(false);
            block.setToY(blockPlace);


                transition.play();
                block.play();
                transition.onFinishedProperty().set(e -> afterBackgroundMoveAnim() );

        }
    }

        private void afterBackgroundMoveAnim() {
            canChange = true;
            creatingOrder = !creatingOrder;

        }


    public boolean nextMonthAvailable = true;
    public boolean previousMonthAvailable = true;

    @FXML
    public void hoverStart(MouseEvent event) {

        if (nextMonthAvailable && (( Polygon ) event.getSource()).getId().equals("nextMonth")) {
            Polygon polygon = (Polygon) event.getSource();

            polygon.setFill(Paint.valueOf("0x5b524aff"));
        }

       if (previousMonthAvailable && (( Polygon ) event.getSource()).getId().equals("lastMonth")) {
            Polygon polygon = (Polygon) event.getSource();

            polygon.setFill(Paint.valueOf("0x5b524aff"));
        }


    }
    @FXML
    public void hoverEnd(MouseEvent event) {

        if (nextMonthAvailable && (( Polygon ) event.getSource()).getId().equals("nextMonth")) {
            Polygon polygon = (Polygon) event.getSource();

            polygon.setFill(Paint.valueOf("0x867155ff"));
        }

        if (previousMonthAvailable && (( Polygon ) event.getSource()).getId().equals("lastMonth")) {
            Polygon polygon = (Polygon) event.getSource();

            polygon.setFill(Paint.valueOf("0x867155ff"));
        }
    }




    public void deactivateButtons() {

        String color = "0xb99c78ff";

        nextMonthAvailable = false;
        previousMonthAvailable = false;

        nextMonth.setFill(Paint.valueOf(color));
        lastMonth.setFill(Paint.valueOf(color));

    }

    public void activateButton(Polygon button) {

        button.setFill(Paint.valueOf("0x867155ff"));

        if (button == nextMonth) {
            nextMonthAvailable = true;
        } else {
            previousMonthAvailable = true;
        }



    }


    //endregion






















}
