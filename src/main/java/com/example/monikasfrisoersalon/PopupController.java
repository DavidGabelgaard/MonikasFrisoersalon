package com.example.monikasfrisoersalon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;

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

    public Treatments treatments;

    @FXML
    public void initialize() {
        hairdresserCombobox.getItems().setAll("Annika", "Henriette", "Kasper", "Monika", "Susan");
        costumerNameTextField.setText(costumerName.getText());
        phoneNumberTextField.setText(phoneNumber.getText());


      //  time.setText("Tid: " + treatments.getTime().toString());
      //  price.setText("Pris: " + treatments.getPrice() + " kr");
        totalPrice.setText(price.getText());
    }
    @FXML
    private void EditOrder() {
        showAndHideInfo(false, true);
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

        showAndHideInfo(true, false);
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

        editButton.setVisible(second);
        editButton.setDisable(first);
        saveButton.setVisible(first);
        saveButton.setDisable(second);
    }

    public void Studierabat(ActionEvent event) {
    }
}
