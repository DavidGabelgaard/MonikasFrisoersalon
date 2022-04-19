package com.example.monikasfrisoersalon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

    @FXML
    public void initialize() {
        hairdresserCombobox.getItems().setAll("Annika", "Henriette", "Kasper", "Monika", "Susan");
        costumerNameTextField.setText(costumerName.getText());
        phoneNumberTextField.setText(phoneNumber.getText());
    }

    @FXML
    private void EditOrder() {
        hairdresser.setVisible(false);
        hairdresser.setDisable(true);
        costumerName.setVisible(false);
        costumerName.setDisable(true);
        phoneNumber.setVisible(false);
        phoneNumber.setDisable(true);

        hairdresserCombobox.setVisible(true);
        hairdresserCombobox.setDisable(false);
        costumerNameTextField.setVisible(true);
        costumerNameTextField.setDisable(false);
        phoneNumberTextField.setVisible(true);
        phoneNumberTextField.setDisable(false);

        editButton.setVisible(false);
        editButton.setDisable(true);
        saveButton.setVisible(true);
        saveButton.setDisable(false);
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


        hairdresser.setVisible(true);
        hairdresser.setDisable(false);
        costumerName.setVisible(true);
        costumerName.setDisable(false);
        phoneNumber.setVisible(true);
        phoneNumber.setDisable(false);

        hairdresserCombobox.setVisible(false);
        hairdresserCombobox.setDisable(true);
        costumerNameTextField.setVisible(false);
        costumerNameTextField.setDisable(true);
        phoneNumberTextField.setVisible(false);
        phoneNumberTextField.setDisable(true);

        editButton.setVisible(true);
        editButton.setDisable(false);
        saveButton.setVisible(false);
        saveButton.setDisable(true);
    }
}
