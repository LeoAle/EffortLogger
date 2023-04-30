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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

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
        } else if (verifyCredentials("credentials.csv", username, password) == null) {
            errorMsg.setText("Incorrect password credentials.");
        } else if (verifyCredentials("credentials.csv", username, password).equals("User")) {
            try {
                changeEffortSceneButtonPushed(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (verifyCredentials("credentials.csv", username, password).equals("Admin")) {
            try {
                changeAdminSceneButtonPushed(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (verifyCredentials("credentials.csv", username, password).equals("Lead")){
            try {
                changeLeadSceneButtonPushed(event);
            } catch (IOException e) {
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
    private String decryptPassword(String encryptedPassword) {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
        return new String(decodedBytes);
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

    public String verifyCredentials(String fileName, String inputUserName, String inputPassword) {
        String line;
        String[] columns;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                columns = line.split(",");

                // Check if the line has the expected structure: "name,password,role,team number"
                if (columns.length != 4) {
                    continue;
                }

                String userName = columns[0].trim();
                String password = columns[1].trim();
                String role = columns[2].trim();
                password = decryptPassword(password);
                if (userName.equals(inputUserName) && isValidPassword(inputPassword) && password.equals(inputPassword)) {
                    return role;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     *
     * @param event
     * @throws IOException
     */
    public void changeAdminSceneButtonPushed(ActionEvent event) throws IOException {
        Parent adminViewParent = FXMLLoader.load(getClass().getResource("AdminControl.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }

    public void changeEffortSceneButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EffortConsole.fxml"));
        Parent adminViewParent = loader.load();
        EffortConsole effortConsoleController = loader.getController();
        effortConsoleController.setUsername(usernameField.getText());
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }
    public void changeLeadSceneButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TeamLeadControl.fxml"));
        Parent adminViewParent = loader.load();
        TeamLeadControl teamLeadController = loader.getController();
        teamLeadController.setUsername(usernameField.getText());
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }


}

