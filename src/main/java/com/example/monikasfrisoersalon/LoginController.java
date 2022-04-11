package com.example.monikasfrisoersalon;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    public TextField Username;
    public TextField Password;
    public AnchorPane topBar;
    public AnchorPane parrent;
    public AnchorPane ErrorBox;
    public AnchorPane ErrorBox1;

    private double xOffset;
    private double yOffset;

    private void wrongLoginInfo() {
        ErrorBox.setVisible(true);
        ErrorBox1.setVisible(false);
    }
    private void cantConnectToServer() {
        ErrorBox1.setVisible(true);
        ErrorBox.setVisible(false);
    }
    private void successfullyLoggedIn() {
        ErrorBox.setVisible(false);
        ErrorBox1.setVisible(false);

        StartApplication.login();

    }

    public void Login() {
        String username = Username.getText();
        String password = Password.getText();
        int result =  DBController.connectToDatabase(username , password);

        switch (result) {
            case 0 -> successfullyLoggedIn();
            case 1 -> wrongLoginInfo();
            case 2 -> cantConnectToServer();
        }
    }

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
    public void Minimize() {
        StartApplication.stage.setIconified(true);
    }
    public void Close() {
        StartApplication.stage.close();
    }
}

