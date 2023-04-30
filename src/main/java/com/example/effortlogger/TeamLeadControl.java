package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class TeamLeadControl {
    public void changeEffortSceneButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EffortConsole.fxml"));
        Parent adminViewParent = loader.load();

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
