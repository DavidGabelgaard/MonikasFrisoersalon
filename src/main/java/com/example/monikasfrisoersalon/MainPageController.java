package com.example.monikasfrisoersalon;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    public AnchorPane background_SeeAndBook;

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

    }



    public WorkDay getWorkDayFromLabel(Label date) {

        int _day = Integer.parseInt( date.getText());


        return g_currentMonth.getDays().get(_day -1 );
    }






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

    public void populateCalender(GenerateMonth generateMonth, HBox place) {


        days  = new VBox[7];

        for (int i = 0; i < 7; i++) {
            days[i] = (VBox) place.getChildren().get(i);
            days[i].getChildren().clear();
        }



        DayOfWeek day = generateMonth.getStartDay();

        daysUntilMonthBeginsInCalender = day.getValue() - 1;

        for (int i = 1; i <= daysUntilMonthBeginsInCalender; i++) {

            int date = generateMonth.getDaysInTotalInLastMonth() - ( daysUntilMonthBeginsInCalender - i );

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

        try {

            Label label = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Date.fxml")));

            label.setText(Integer.toString(date));

            styleLabelWithWorkDay(label , workDay , type);

            days [index % 7].getChildren().add(label);

            dates.add(label);

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
                    new KeyFrame(Duration.seconds(0.5f),
                            new KeyValue(scrollPane.hvalueProperty(), scrollPane.getHvalue() - 0.5d)),
                    new KeyFrame(Duration.seconds(0.1d)));
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
                    new KeyFrame(Duration.seconds(0.5f),
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

    public void OrderPress() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("PopUp.fxml"));
            Scene scene = new Scene(fxmlLoader.load() , 500 , 540);

            Stage s = new Stage();
            s.setScene(scene);
            s.setResizable(false);
            s.initStyle(StageStyle.TRANSPARENT);
            s.show();


            popUp = s;

            // this is new


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


        if (!creatingOrder && node.getId().equals("_bestilling") )
        height = 84;

            TranslateTransition transition = new TranslateTransition();

            transition.setDuration(Duration.seconds(0.3f));
            transition.setNode(background_SeeAndBook);
            transition.setAutoReverse(false);

            transition.setToY(height);


            transition.onFinishedProperty().set(e -> afterBackgroundMoveAnim() );
            transition.play();


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


    //region Visual



















}
