package com.example.monikasfrisoersalon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class PopupController {

    public Text hairdresser;
    public Text costumerName;
    public Text phoneNumber;
    public Text orderID;
    public ComboBox hairdresserCombobox;
    public TextField costumerNameTextField;
    public TextField phoneNumberTextField;
    public Button editButton;
    public Button saveButton;
    public Text treatment;
    public Text time;
    public Text price;
    public Text totalPrice;
    public AnchorPane bestilling;
    public ComboBox services;

    public Treatments activeTreatment;
    public boolean studierabatActive;

    @FXML
    public void initialize() {
        hairdresserCombobox.getItems().setAll("Annika", "Henriette", "Kasper", "Monika", "Susan");
        addServices(DBController.getAllTreatments());
        costumerNameTextField.setText(costumerName.getText());
        phoneNumberTextField.setText(phoneNumber.getText());
        activeTreatment = DBController.getTreatments(1);
        treatment.setText(activeTreatment.getName());
        time.setText("Tid: " + activeTreatment.getTime());
        price.setText("Pris: " + activeTreatment.getPrice() + " kr");
        totalPrice.setText(activeTreatment.getPrice() + " kr");
        studierabatActive = false;
    }

    @FXML
    private void EditOrder() {
        showAndHideInfo(true, false);
    }

    @FXML
    private void SaveOrder() {
        if (hairdresserCombobox.getValue() != null) {
            hairdresser.setText(hairdresserCombobox.getValue().toString());
        }

        if (costumerNameTextField.getText().equals("")) {
            costumerName.setText("Ikke angivet");
            costumerName.setOpacity(0.5);
        } else {
            costumerName.setText(costumerNameTextField.getText());
            costumerName.setOpacity(1);
        }

        if (phoneNumberTextField.getText().equals("")) {
            phoneNumber.setText("Ikke angivet");
            phoneNumber.setOpacity(0.5);
        } else {
            phoneNumber.setText(phoneNumberTextField.getText());
            phoneNumber.setOpacity(1);
        }

        treatment.setText(services.getValue().toString());
        activeTreatment = DBController.getTreatmentFromName(treatment.getText());
        time.setText("Tid: " + activeTreatment.getTime());
        price.setText("Pris: " + activeTreatment.getPrice() + " kr");

        showAndHideInfo(false, true);
        updatePrice();
    }

    public void showAndHideInfo(boolean first, boolean second) {
        hairdresser.setVisible(second);
        hairdresser.setDisable(first);
        costumerName.setVisible(second);
        costumerName.setDisable(first);
        phoneNumber.setVisible(second);
        phoneNumber.setDisable(first);

        hairdresserCombobox.setVisible(first);
        hairdresserCombobox.setDisable(second);
        costumerNameTextField.setVisible(first);
        costumerNameTextField.setDisable(second);
        phoneNumberTextField.setVisible(first);
        phoneNumberTextField.setDisable(second);

        bestilling.setVisible(second);
        bestilling.setDisable(first);
        services.setVisible(first);
        services.setDisable(second);

        editButton.setVisible(second);
        editButton.setDisable(first);
        saveButton.setVisible(first);
        saveButton.setDisable(second);
    }

    public void Studierabat(ActionEvent event) {
        studierabatActive = !studierabatActive;
        updatePrice();
    }

    public void updatePrice() {
        if (studierabatActive) {
            totalPrice.setText(activeTreatment.getPrice() * 0.85 + " kr");
        } else if (!studierabatActive) {
            totalPrice.setText(activeTreatment.getPrice() + " kr");
        }
    }

    public void addServices(ArrayList<Treatments> list) {
        for (int i = 1; i < list.size(); i++) {
            services.getItems().add(DBController.getAllTreatments().get(i).name);
        }
    }


}
