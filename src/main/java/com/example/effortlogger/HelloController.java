package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Base64;

public class HelloController {

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label errorMsg;

    @FXML
    /**
     *
     */
    void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            errorMsg.setText("Username is empty.");
        } else if (!(isValidPassword(password))) {
            errorMsg.setText("Incorrect password credentials.");
        } else {
            encryptPassword();
            try {
                changeEffortSceneButtonPushed(event);
            } catch (IOException e) {
                System.out.println("The error is on line 43 or something!!!!!!!!!");
                throw new RuntimeException(e);
            }
        }

    }

    /**
     *
     */
    private void encryptPassword() {
        byte[] bytes = passwordField.getText().getBytes();
        byte[] encryptedBytes = Base64.getEncoder().encode(bytes);

        String result = "Username: " + usernameField.getText() + "\nPassword (Encrypted): "
                + new String(encryptedBytes);

        // Show the result in a pop-up window
        Stage popupStage = new Stage();
        popupStage.setScene(new Scene(new Label(result), 250, 50));
        popupStage.show();
    }

    /**
     *
     * @param password
     * @return
     */
    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        return password.matches(pattern);


    }

    /**
     *
     * @param event
     * @throws IOException
     */
    public void  changeAdminSceneButtonPushed(ActionEvent event) throws IOException {
        Parent adminViewParent = FXMLLoader.load(getClass().getResource("AdminControl.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }
    public void  changeEffortSceneButtonPushed(ActionEvent event) throws IOException {
        Parent adminViewParent = FXMLLoader.load(getClass().getResource("EffortConsole.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }


}

