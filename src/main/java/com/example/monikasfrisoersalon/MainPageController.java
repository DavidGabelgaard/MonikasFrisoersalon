package com.example.monikasfrisoersalon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;


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

    public AnchorPane task1;
    public AnchorPane task2;
    public AnchorPane task3;
    public AnchorPane task4;

    public Text TodaysDate;
    public Text dateAndMonth;


    public VBox[] days;

    public WorkDay currentWorkDay;





    public int currentYearDisplayed;
    public int currentMonthDisplayed;


    public ArrayList<Label> dates = new ArrayList<>();

    private int index;
    private int daysUntilMonthBeginsInCalender;

    private double xOffset;
    private double yOffset;


    public void initialize() {

        username.setText(Formatter.bigFirstLetter(Formatter.formatUserName(DBController.getActiveUser())));


        populateCalender(LocalDate.now().getYear() , LocalDate.now().getMonthValue());
        selectDateOfToday();

    }




    public WorkDay getWorkDayFromLabel(Label date) {

        int _day = Integer.parseInt( date.getText());
        int _month = currentMonthDisplayed;
        int _year = currentYearDisplayed;

        LocalDate _date = LocalDate.of(_year , _month , _day);

        return DBController.getWorkDayForSignedInUserWithDate(_date);
    }


    //region Animations













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
        TodaysDate.setText( Formatter.getDayFromLocalDate(LocalDate.of(currentYearDisplayed , currentMonthDisplayed , Integer.parseInt(date.getText()))));


        currentWorkDay = getWorkDayFromLabel((selectedLabel));
         updateDailyToDoList();
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


        for (int i = 0; i < currentWorkDay.getOrders().size() ; i++) {
            Label time = (Label) tasks[i].getChildren().get(0);
            time.setText(currentWorkDay.getOrders().get(i).getStartTime().toString());

            Text treatment = (Text) tasks[i].getChildren().get(1);

            if (currentWorkDay.getOrders().get(i).getTreatments().size() > 1) {
                treatment.setText("Se mere");
            } else {
                treatment.setText(currentWorkDay.getOrders().get(i).getTreatments().get(0).getName());
            }

        }


        int amountThatCantBeDisplayed = tasks.length - currentWorkDay.getOrders().size();

        if (amountThatCantBeDisplayed > 0) {
            for (int i = tasks.length - 1; i >= currentWorkDay.getOrders().size() ; i--) {
                tasks[i].setVisible(false);
            }
        }
    }

    public void populateCalender(int year , int month) {

        currentMonthDisplayed = month;
        currentYearDisplayed = year;

       days  = new VBox[] {MON, TUE, WED, THU, FRI, SAT, SUN};

        GenerateMonth generateMonth = new GenerateMonth ( year , month);

        DayOfWeek day = generateMonth.getStartDay();

        daysUntilMonthBeginsInCalender = day.getValue() - 1;

        for (int i = 1; i <= daysUntilMonthBeginsInCalender; i++) {

            int date =generateMonth.getDaysInTotalInLastMonth() - ( daysUntilMonthBeginsInCalender - i );

            insertDate(date , "previus-date", null);

        }

        for (int i = 0; i < generateMonth.getDays().size() ; i++) {
            insertDate(i + 1 , "regular-date" , generateMonth.getDays().get(i));
        }

        int daysLeft = 42 - ( daysUntilMonthBeginsInCalender + generateMonth.getDays().size());

        for (int i = 1; i <= daysLeft ; i++) {
            insertDate(i , "next-date" , null);
        }





    }

    public void insertDate(int date , String type , WorkDay workDay) {

        System.out.println(date + " " + type + " " + workDay);


        try {

                Label label = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Date.fxml")));

                label.setText(Integer.toString(date));

                styleLabelWithWorkDay(label , workDay , type);

                days [index % 7].getChildren().add(label);

                dates.add(label);

                index ++;

                if (index > 42) index = 0;


        } catch (IOException e) {
            e.printStackTrace();
        }


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


    public void OrderPress() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("PopUp.fxml"));
            Scene scene = new Scene(fxmlLoader.load() , 220 , 300);

            Stage s = new Stage();
            s.setScene(scene);
            s.setResizable(false);
            s.initStyle(StageStyle.TRANSPARENT);
            s.show();


            // this is new


        } catch (IOException e) {
            e.printStackTrace();
        }


    }














}
