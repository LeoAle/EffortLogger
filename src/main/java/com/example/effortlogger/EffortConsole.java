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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.nio.file.StandardOpenOption;
import java.io.BufferedReader;
import java.io.FileReader;

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
    private boolean isRunning = false;
    private LocalDateTime startTime;
    private Duration elapsedTime;
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
    private String username;

    public void setUsername(String username) {
        this.username = username;
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



    public void startStopButtonPushed(ActionEvent event) throws IOException {
        if (isAllComboBoxesSelected()) {
            if (!isRunning) {
                // Start the stopwatch
                System.out.println("started");
                startTime = LocalDateTime.now();
                isRunning = true;
            } else {
                // Stop the stopwatch
                elapsedTime = Duration.between(startTime, LocalDateTime.now());
                isRunning = false;
                System.out.println("stopped");

                // Save the time in a variable and print it in hours, minutes, and seconds
                long hours = elapsedTime.toHours();
                long minutes = elapsedTime.toMinutes() % 60;
                long seconds = elapsedTime.getSeconds() % 60;
                System.out.printf("Elapsed time: %02d:%02d:%02d%n", hours, minutes, seconds);

                // Save the data to the corresponding CSV file
                saveDataToCSV();
            }
        } else {
            System.out.println("A field is empty. Please make sure all combo boxes have a selected value.");
        }
    }
    private boolean isAllComboBoxesSelected() {
        return projectComboBox.getValue() != null &&
                lifeCycleStepComboBox.getValue() != null &&
                effortCategoryComboBox.getValue() != null &&
                planComboBox.getValue() != null &&
                deliverableComboBox.getValue() != null;
    }

    private void saveDataToCSV() {
        String projectName = projectComboBox.getValue();
        String csvFilename = projectName + ".csv";
        String dataToSave = String.join(",",
                username,
                projectName,
                lifeCycleStepComboBox.getValue(),
                effortCategoryComboBox.getValue(),
                planComboBox.getValue(),
                deliverableComboBox.getValue(),
                String.format("%02d:%02d:%02d", elapsedTime.toHours(), elapsedTime.toMinutes() % 60, elapsedTime.getSeconds() % 60)
        );

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(csvFilename), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(dataToSave);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
