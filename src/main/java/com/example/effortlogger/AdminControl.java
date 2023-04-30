package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.io.*;
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
    private ComboBox<String> newUserComboBoxRoles;

    @FXML
    private Label adminUserNameErrorMsg;



    @FXML
    private Button searchUserName;
    @FXML
    private TextField modifyUserNameTextField;
    @FXML
    private Label modifyUserPasswordLabel;
    @FXML
    private PasswordField modifyUserPasswordField;
    @FXML
    private Label modifyUserComboBoxLabel;
    @FXML
    private ComboBox<String> modifyUserComboBoxRoles;
    @FXML
    private Button modifyUserApplyButton;

    @FXML
    private Label adminModifyNameErrorMsg;

    public Label newUserTeamNumberLabel;
    public TextField newUserTeamNumberTextField;
    @FXML
    private TextField modifyUserTeamNumberTextField;
    public Label modifyUserTeamNumberLabel;
    private final String fileName = "credentials.csv";

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
            adminUserNameErrorMsg.setVisible(true);
            adminUserNameErrorMsg.setText("Name is empty.");
            return;
        }
        if (doesNameExist(newUserNameTextField.getText())) {
            adminUserNameErrorMsg.setVisible(true);
            adminUserNameErrorMsg.setText("Name already exists.");
            return;
        }
        if (newUserPasswordField.getText().isEmpty()) {
            adminUserNameErrorMsg.setVisible(true);
            adminUserNameErrorMsg.setText("Password is empty.");
            return;
        }
        if (!(isValidPassword(newUserPasswordField.getText()))) {
            adminUserNameErrorMsg.setVisible(true);
            adminUserNameErrorMsg.setText("Incorrect password credentials.");
            return;
        }

        if (newUserTeamNumberTextField.getText().isEmpty()){
            adminUserNameErrorMsg.setVisible(true);
            adminUserNameErrorMsg.setText("Team Number not specified");
        }

        writeToFile(newUserNameTextField.getText(),
                    newUserPasswordField.getText(),
                    newUserComboBoxRoles.getValue(),
                    newUserTeamNumberTextField.getText());

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
    Beginning of Modify User Scene
    ==============================
    */
    public void changeSceneAdminModifyUser(ActionEvent event) throws IOException {
        Parent adminNewUserViewParent = FXMLLoader.load(getClass().getResource("AdminControlModifyUser.fxml"));
        Scene adminNewUserViewScene = new Scene(adminNewUserViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminNewUserViewScene);
        window.show();
    }

    public void checkUserName()  {
        if (modifyUserNameTextField.getText().isEmpty()) {
            adminModifyNameErrorMsg.setVisible(true);
            adminModifyNameErrorMsg.setText("Name is empty.");
            return;
        }
        if (!(doesNameExist(modifyUserNameTextField.getText()))) {
            adminModifyNameErrorMsg.setVisible(true);
            adminModifyNameErrorMsg.setText("Name does not exist.");
            return;
        }

        modifyUserApplyButton.setDisable(false);
        adminModifyNameErrorMsg.setVisible(false);
        searchUserName.setDisable(true);

        modifyUserPasswordLabel.setVisible(true);
        modifyUserPasswordField.setVisible(true);
        modifyUserComboBoxLabel.setVisible(true);
        modifyUserComboBoxRoles.setVisible(true);
        modifyUserTeamNumberLabel.setVisible(true);
        modifyUserTeamNumberTextField.setVisible(true);
    }

    public void changeSceneAdminModifyUserApplyButtonPushed(ActionEvent event) throws IOException {
        if (modifyUserPasswordField.getText().isEmpty()) {
            adminModifyNameErrorMsg.setVisible(true);
            adminModifyNameErrorMsg.setText("Password is empty.");
            return;
        }
        if (!(isValidPassword(modifyUserPasswordField.getText()))) {
            adminModifyNameErrorMsg.setVisible(true);
            adminModifyNameErrorMsg.setText("Incorrect password credentials.");
            return;
        }
        replaceNameInFile(modifyUserNameTextField.getText(),
                          modifyUserPasswordField.getText(),
                          modifyUserComboBoxRoles.getValue(),
                          modifyUserTeamNumberTextField.getText());

        Parent adminViewParent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }

    /*
    ==============================
    End of Modify User Scene
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

                // Check if the line has the expected structure: "name,password,role,team number"
                if (columns.length != 4) {
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

    public void writeToFile(String name, String password, String role, String teamNumber) {
        String csv_line = name + "," + encryptPassword(password) + "," + role +"," + teamNumber;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.newLine();
            writer.write(csv_line);
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

    private void replaceNameInFile(String name, String password, String role, String teamNumber) {
        String csv_line = name + "," + encryptPassword(password) + "," + role +"," + teamNumber;
        String temp_file_name = new String("credentials_temp.csv");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temp_file_name));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                // Check if the line has the expected structure: "name,password,role,team number"
                if (columns.length != 4) {
                    continue;
                }

                if (columns[0].trim().equals(name)) {
                    line = csv_line;
                }
                writer.write(line);
                writer.newLine();
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File oldFile = new File(fileName);
        oldFile.delete();

        File newFile = new File(temp_file_name);
        newFile.renameTo(oldFile);
    }
    /*
    ==============================
    End of Helper Functions
    ==============================
    */
}
