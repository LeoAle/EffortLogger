package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class TeamLeadControl {

    String username;
    public void setUsername(String username) {
        this.username = username;
    }
    public void changeEffortSceneButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EffortConsole.fxml"));
        Parent adminViewParent = loader.load();
        EffortConsole effortConsoleController = loader.getController();
        effortConsoleController.setUsername(username);
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }


    public void logoutButtonPushed(ActionEvent event) throws IOException {
        Parent adminViewParent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }
}
