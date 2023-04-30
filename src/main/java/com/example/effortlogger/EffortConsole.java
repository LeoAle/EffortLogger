package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class EffortConsole {

    @FXML
    private ComboBox<String> projectComboBox;

    @FXML
    private ComboBox<String> lifeCycleStepComboBox;

    @FXML
    private ComboBox<String> effortCategoryComboBox;

    @FXML
    private ComboBox<String> planComboBox;

    @FXML
    private ComboBox<String> deliverableComboBox;

    public void initialize() {
        String[][] data = parseCSV("data.csv");

        if (data != null && data.length == 5) {
            projectComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[0])));
            lifeCycleStepComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[1])));
            effortCategoryComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[2])));
            planComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[3])));
            deliverableComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[4])));
        }
    }

    private String[][] parseCSV(String filename) {
        String[][] data = new String[5][];

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                if (rowIndex >= 5) {
                    break; // Break the loop if rowIndex is equal to or greater than 5
                }
                String[] values = line.split(",\\s*");
                data[rowIndex] = values;
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }



    public void changeSceneButtonPushed(ActionEvent event) throws IOException {
        Parent adminViewParent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }

    public void startStopButtonPushed(ActionEvent event) throws IOException{
        System.out.println("Hello");
    }
}
