package com.example.monikasfrisoersalon;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class HistoryController {

    public AnchorPane history;
    public VBox vbox;
    public ComboBox services;
    public ComboBox day;
    public ComboBox month;
    public ComboBox year;

    public TextField bestillingsID;
    public TextField navn;
    public TextField telefonnummer;



    public void close() {
        Stage s = (Stage) history.getScene().getWindow();
        s.close();
    }

    public void initialize() {
        addServices(DBController.getAllTreatments());
        month.getItems().addAll("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        for (int i = 1; i <= 31; i++) {
            day.getItems().add(i);
        }
        year.getItems().setAll("2017", "2018", "2019", "2020", "2021", "2022");
    }

    public void addServices(ArrayList<Treatments> list) {
        for (int i = 1; i < list.size(); i++) {
            services.getItems().add(DBController.getAllTreatments().get(i).name);
        }
    }

    public void updateDay() {
        day.getItems().clear();

        String chosenMonth = month.getValue().toString();

        if (chosenMonth.equals("Februar")) {
            for (int i = 1; i <= 28; i++) {
                day.getItems().add(i);
            }
        } else if (chosenMonth.equals("April") || chosenMonth.equals("Juni") ||
                chosenMonth.equals("September") || chosenMonth.equals("November")) {
            for (int i = 1; i <= 30; i++) {
                day.getItems().add(i);
            }
        } else {
            for (int i = 1; i <= 31; i++) {
                day.getItems().add(i);
            }
        }
    }

    public int numberOfMonth(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }


    public void search() {
        int orderBestillingsID = parseInt(bestillingsID.getText());
        String orderNavn = navn.getText();
        String orderTelefonnummer = telefonnummer.getText();

        ArrayList<Treatments> orderService = new ArrayList<>();
        orderService.add(DBController.getTreatmentFromName(services.getValue().toString()));

        LocalDate orderDato = LocalDate.of(parseInt(year.getValue().toString()), numberOfMonth(month.getValue().toString()), parseInt(day.getValue().toString()));

        Orders order = new Orders(orderService, orderDato, orderNavn, orderTelefonnummer, orderBestillingsID);

    }
}
