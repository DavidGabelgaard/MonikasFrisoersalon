package com.example.monikasfrisoersalon;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class DateController {


    @FXML
    public void selectDate(MouseEvent actionEvent) {

        Label l = (Label) actionEvent.getSource();

        if (Objects.equals(l.getStyleClass().get(0), "previus-date") ) {
            System.out.println("its a previous date");
        } else if  ( Objects.equals(l.getStyleClass().get(0) , "next-date")) {
            System.out.println("its a date in the next month");
        } else {

            MainPageController mainPageController =  StartApplication.currentFXMLLoader.getController();

            mainPageController.selectDay(l);

        }
    }



}
