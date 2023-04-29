package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

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

    @FXML
    private Label adminErrorMsg;

    String fileName = "credentials.csv";

    public void changeSceneMainMenu(ActionEvent event) throws IOException {
        Parent adminViewParent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }

    /*
    ==============================
    Beginning of New User Scene
    ==============================
    */
    public void changeSceneAdminNewUser(ActionEvent event) throws IOException {
        Parent adminNewUserViewParent = FXMLLoader.load(getClass().getResource("AdminControlNewUser.fxml"));
        Scene adminNewUserViewScene = new Scene(adminNewUserViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminNewUserViewScene);
        window.show();
    }

    public void changeSceneAdminNewUserApplyButtonPushed(ActionEvent event) throws IOException {

        if (newUserNameTextField.getText().isEmpty()) {
            adminErrorMsg.setVisible(true);
            adminErrorMsg.setText("Name is empty.");
            return;
        }
        if (doesNameExist(newUserNameTextField.getText())) {
            adminErrorMsg.setVisible(true);
            adminErrorMsg.setText("Name already exists.");
            return;
        }
        if (newUserPasswordField.getText().isEmpty()) {
            adminErrorMsg.setVisible(true);
            adminErrorMsg.setText("Password is empty.");
            return;
        }
        if (!(isValidPassword(newUserPasswordField.getText()))) {
            adminErrorMsg.setVisible(true);
            adminErrorMsg.setText("Incorrect password credentials.");
            return;
        }

        writeToFile(newUserNameTextField.getText(), newUserPasswordField.getText(), comboBoxRoles.getValue());

        Parent adminViewParent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }
    /*
    ==============================
    End of New User Scene
    ==============================
    */

    /*
    ==============================
    Beginning of Helper Functions
    ==============================
    */
    public boolean doesNameExist(String newUserName)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] columns = line.split(",");

                // Check if the line has the expected structure: "name,password,role"
                if (columns.length != 3) {
                    continue;
                }

                if (newUserName.equals(columns[0].trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }

    public void writeToFile(String name, String password, String role) {
        String line = name + "," + encryptPassword(password) + "," + role;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.newLine();
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error appending data to file: " + e.getMessage());
        }
    }

    private String encryptPassword(String password) {
        byte[] bytes = password.getBytes();
        byte[] encryptedBytes = Base64.getEncoder().encode(bytes);
        return new String(encryptedBytes);
    }
    /*
    ==============================
    End of Helper Functions
    ==============================
    */
}
