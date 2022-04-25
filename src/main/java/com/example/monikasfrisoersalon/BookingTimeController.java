package com.example.monikasfrisoersalon;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class BookingTimeController {
    @FXML
    public void selectTime(MouseEvent event) {
        MainPageController mainPageController =  StartApplication.currentFXMLLoader.getController();

            Label source = (Label) event.getSource();

            mainPageController.selectTime(source);



    }







}
