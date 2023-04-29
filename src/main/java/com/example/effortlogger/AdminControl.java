package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.IOException;

public class AdminControl {
    /**
     * Change the scene with this button
     */

    @FXML
    private TextField newUserNameTextField;

    @FXML
    private PasswordField newUserPasswordField;

    @FXML
    private ComboBox<String> comboBoxRoles;

    public void changeSceneMainMenu(ActionEvent event) throws IOException {
        Parent adminViewParent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }


    /*
    =========================
    =========================
    */
    public void changeAdminSceneNewUserButtonPushed(ActionEvent event) throws IOException {
        Parent adminNewUserViewParent = FXMLLoader.load(getClass().getResource("AdminControlNewUser.fxml"));
        Scene adminNewUserViewScene = new Scene(adminNewUserViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminNewUserViewScene);
        window.show();
    }

    public void changeSceneApplyButtonPushed(ActionEvent event) throws IOException {
        System.out.println(newUserNameTextField.getText());
        System.out.println(newUserPasswordField.getText());

        Parent adminViewParent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }
}
