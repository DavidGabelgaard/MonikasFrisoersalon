package com.example.monikasfrisoersalon;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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
    public VBox searchVbox;
    public ComboBox services;
    public ComboBox day;
    public ComboBox month;
    public ComboBox year;


    public TextField bestillingsID;
    public TextField navn;
    public TextField telefonnummer;

    public boolean afsluttetCheck;


    public void close() {
        Stage s = (Stage) history.getScene().getWindow();
        s.close();
    }

    public void initialize() {
        afsluttetCheck = false;
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

    public void afsluttetCheck() {
        afsluttetCheck = !afsluttetCheck;
    }

    public void search() {
        searchVbox.getChildren().clear();
        int orderBestillingsID;
        if (bestillingsID.getText().equals("")) {
            orderBestillingsID = 0;
        } else {
            orderBestillingsID = parseInt(bestillingsID.getText());
        }

        String orderNavn = navn.getText();
        String orderTelefonnummer = telefonnummer.getText();

        ArrayList<Treatments> orderService = new ArrayList<>();
        if (services.getValue() == null) {
            orderService = null;
        } else {
            orderService.add(DBController.getTreatmentFromName(services.getValue().toString()));
        }

        LocalDate orderDato;
        if (day.getValue() == null || month.getValue() == null || year.getValue() == null) {
            orderDato = null;
        } else {
            orderDato = LocalDate.of(parseInt(year.getValue().toString()), numberOfMonth(month.getValue().toString()), parseInt(day.getValue().toString()));
        }

        Orders order = new Orders(orderService, orderDato, orderNavn, orderTelefonnummer, orderBestillingsID);

        ArrayList<Orders> result;

        if (afsluttetCheck) {
            result = DBController.searchInHistory(order);
        } else {
            result = DBController.searchInActive(order);
        }


        if (result != null) {
            addSearches(result);
        }


    }

    public void addSearches(ArrayList<Orders> list) {

        try {
            for (int i = 0; i < list.size(); i++) {

                AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SearchAnchorPane.fxml")));

                Orders order = list.get(i);
                Treatments service = order.getTreatments().get(0);

                Text searchBestillingsID = (Text) anchorPane.lookup("#searchBestillingsID");
                Text searchNavn = (Text) anchorPane.lookup("#searchNavn");
                Text searchTelefonnummer = (Text) anchorPane.lookup("#searchTelefonnummer");
                Text searchService = (Text) anchorPane.lookup("#searchService");
                Text searchDato = (Text) anchorPane.lookup("#searchDato");

                searchBestillingsID.setText(String.valueOf(order.getBookingID()));
                searchNavn.setText(order.getBookingName());
                searchTelefonnummer.setText(order.getBookingPhoneNumber());
                searchService.setText(service.getName());
                searchDato.setText(order.getDate().toString());

                searchVbox.getChildren().add(anchorPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
