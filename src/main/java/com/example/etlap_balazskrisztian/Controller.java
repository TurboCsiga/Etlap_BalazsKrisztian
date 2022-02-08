package com.example.etlap_balazskrisztian;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Controller {
    Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static Controller ujAblak(String view,
                                     String windowTitle,
                                     int width,
                                     int height) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(view));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        Controller controller = fxmlLoader.getController();
        controller.stage = stage;
        return controller;
    }

    protected void errorAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getClass().toString());
        alert.setContentText(e.getMessage());
        alert.show();
    }

    protected void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Figyelem");
        alert.setContentText(msg);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
    }
}
