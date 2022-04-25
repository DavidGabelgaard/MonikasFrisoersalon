package com.example.monikasfrisoersalon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class StartApplication extends Application {

    public static Stage stage = null;
    public static FXMLLoader currentFXMLLoader;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400 , 500 );
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        StartApplication.stage = stage;
        currentFXMLLoader = fxmlLoader;
        stage.show();
        scene.lookup("ImageView").requestFocus();
    }

    public static void login() {
        try {

            currentFXMLLoader = new FXMLLoader(StartApplication.class.getResource("Main_Page.fxml"));

            Scene scene = new Scene(currentFXMLLoader.load() , 1000 , 700);

            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {

                MainPageController mainPageController = currentFXMLLoader.getController();

                mainPageController.closePopUp();



            });


            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.getScene().setFill(Color.TRANSPARENT);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }








    public static void main(java.lang.String[] args) {

        launch();

    }
}
