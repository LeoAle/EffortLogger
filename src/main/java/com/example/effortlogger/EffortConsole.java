package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
import java.io.File;

public class EffortConsole {

    @FXML
    private ComboBox<String> projectLogsComboBox;
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
    @FXML
    private ComboBox<String> projectEditorComboBox;
    @FXML
    private ComboBox<String> lifeCycleStepEditorComboBox;
    @FXML
    private ComboBox<String> effortCategoryEditorComboBox;
    @FXML
    private ComboBox<String> planEditorComboBox;
    @FXML
    private ComboBox<String> deliverableEditorComboBox;
    @FXML
    private  Rectangle clockStatusColor;
    @FXML
    private Label clockStatus;


    private boolean isRunning = false;
    private LocalDateTime startTime;
    private Duration elapsedTime;
    private String username;

    //New GUI Elements
    public ComboBox effortLogEntryEditorComboBox;
    public TextField dateTextField;
    public TextField startTimeTextField;
    public TextField stoptimeTextField;
    public Button updateEntryButton;
    public Button SplitEntryButton;
    public Label attributeStatusLabel;
    public Button LogoutBtn;
    public Button printLogsButton;
    public Button deleteLogsButton;


    /**
     * Initializes the ComboBox objects in the user interface with the data obtained from a CSV file.
     * The ComboBox items are set from a 2D array of String values obtained by calling the parseCSV method.
     */
    public void initialize() {
        String[][] data = parseCSV("data.csv");

        if (data != null && data.length == 5) {
            projectComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[0])));
            lifeCycleStepComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[1])));
            effortCategoryComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[2])));
            planComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[3])));
            deliverableComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(data[4])));
            fillProjectLogsComboBox();
            fillEditorComboBoxes();
        }
    }
    public void fillEditorComboBoxes() {
        projectEditorComboBox.setItems(projectComboBox.getItems());
        lifeCycleStepEditorComboBox.setItems(lifeCycleStepComboBox.getItems());
        effortCategoryEditorComboBox.setItems(effortCategoryComboBox.getItems());
        planEditorComboBox.setItems(planComboBox.getItems());
        deliverableEditorComboBox.setItems(deliverableComboBox.getItems());
    }


    /**
     * Sets the username for the current user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Parses a CSV file and returns a 2D String array containing the data.
     *
     * @param filename the name of the CSV file to parse
     * @return a 2D String array containing the parsed data
     */
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
    public void fillProjectLogsComboBox() {
        projectLogsComboBox.setItems(projectComboBox.getItems());
    }

    /**
     * Changes the current scene to the "hello-view.fxml" file.
     *
     * @param event the event that triggered this method
     * @throws IOException if there is an error loading the FXML file
     */
    public void changeSceneButtonPushed(ActionEvent event) throws IOException {
        Parent adminViewParent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene adminViewScene = new Scene(adminViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(adminViewScene);
        window.show();
    }

    /**
     * Handles the event when the start/stop button is pushed. If all combo boxes have a selected value,
     * starts the stopwatch if it is not already running, or stops the stopwatch and saves the elapsed time to
     * a CSV file if it is running. If any combo box does not have a selected value, displays an error message.
     *
     * @param event the ActionEvent generated when the button is pushed
     * @throws IOException if there is an error saving data to the CSV file
     */
    public void startStopButtonPushed(ActionEvent event) throws IOException {
        if (isAllComboBoxesSelected()) {
            if (!isRunning) {
                // Start the stopwatch
                System.out.println("started");
                startTime = LocalDateTime.now();
                isRunning = true;
                //Change the color of the clock status and its message
                clockStatus.setText("Clock is Running");
                clockStatusColor.setFill(Color.GREEN);
                } else {
                // Stop the stopwatch
                elapsedTime = Duration.between(startTime, LocalDateTime.now());
                isRunning = false;
                System.out.println("stopped");
                //Change the color of the clock status and its message
                clockStatus.setText("Clock is Stopped");
                clockStatusColor.setFill(Color.RED);

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

    /**
     * Checks whether all the combo boxes have a selected value or not.
     *
     * @return true if all combo boxes have a selected value, false otherwise.
     */
    private boolean isAllComboBoxesSelected() {
        return projectComboBox.getValue() != null &&
                lifeCycleStepComboBox.getValue() != null &&
                effortCategoryComboBox.getValue() != null &&
                planComboBox.getValue() != null &&
                deliverableComboBox.getValue() != null;
    }
    /**
     * Saves the selected data to a CSV file with the format "projectName.csv".
     * The data to save includes the username, project name, life cycle step, effort category,
     * plan, deliverable, and elapsed time.
     */
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

    /**
     * This method is called when the user clicks the "Update Entry" button. It prints a message to the console and does not perform any other action.
     *
     * @param event the ActionEvent associated with the button click
     * @throws IOException if an IO error occurs while processing the button click
     */
    public void updateEntryButtonPushed(ActionEvent event) throws IOException{
        System.out.println("Update Entry Here!");
    }

    /**
     * This method is called when the user clicks the "Split Entry" button. It prints a message to the console and does not perform any other action.
     *
     * @param event the ActionEvent associated with the button click
     * @throws IOException if an IO error occurs while processing the button click
     */
    public void splitEntryButtonPushed(ActionEvent event) throws IOException{
        System.out.println("Split Entry Here!");
    }

    /**
     * This method is called when the user clicks the "Print Logs" button. It prints a message to the console and does not perform any other action.
     *
     * @param event the ActionEvent associated with the button click
     * @throws IOException if an IO error occurs while processing the button click
     */
    public void printLogsButtonPushed(ActionEvent event) throws IOException {
        if (projectLogsComboBox.getValue() == null) {
            System.out.println("ProjectLogsComboBox is empty. Please select a project.");
        } else {
            String projectName = projectLogsComboBox.getValue();
            String csvFileName = projectName + ".csv";
            File file = new File(csvFileName);

            if (!file.exists()) {
                System.out.println("There is nothing in this project: " + projectName);
            } else {
                try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
                    String line;
                    System.out.println("Logs for project: " + projectName);
                    System.out.println("----------------------------------------------------------------------------------------------------------------");
                    System.out.printf("%-10s | %-10s | %-20s | %-20s | %-10s | %-20s | %-20s%n",
                            "Username", "Project", "Life Cycle Step", "Effort Category", "Plan", "Deliverable", "Elapsed Time");
                    System.out.println("----------------------------------------------------------------------------------------------------------------");

                    while ((line = br.readLine()) != null) {
                        String[] columns = line.split(",");

                        if (columns.length != 7) {
                            continue;
                        }

                        String username = columns[0].trim();
                        String project = columns[1].trim();
                        String lifeCycleStep = columns[2].trim();
                        String effortCategory = columns[3].trim();
                        String plan = columns[4].trim();
                        String deliverable = columns[5].trim();
                        String elapsedTime = columns[6].trim();

                        System.out.printf("%-10s | %-10s | %-20s | %-20s | %-10s | %-20s | %-20s%n",
                                username, project, lifeCycleStep, effortCategory, plan, deliverable, elapsedTime);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading the CSV file: " + csvFileName);
                    e.printStackTrace();
                }
            }
        }
    }




    /**
     * This method is called when the user clicks the "Delete Logs" button. It prints a message to the console and does not perform any other action.
     *
     * @param event the ActionEvent associated with the button click
     * @throws IOException if an IO error occurs while processing the button click
     */
    public void deleteLogsButtonPushed(ActionEvent event) throws IOException {
        if (projectLogsComboBox.getValue() == null) {
            System.out.println("ProjectLogsComboBox is empty. Please select a project.");
        } else {
            String projectName = projectLogsComboBox.getValue();
            String csvFileName = projectName + ".csv";
            File file = new File(csvFileName);

            if (!file.exists()) {
                System.out.println("There is nothing in this project: " + projectName);
            } else {
                try {
                    if (file.delete()) {
                        System.out.println("All logs for project " + projectName + " have been deleted.");
                    } else {
                        System.out.println("An error occurred while deleting the logs for project " + projectName);
                    }
                } catch (Exception e) {
                    System.out.println("Error deleting the CSV file: " + csvFileName);
                    e.printStackTrace();
                }
            }
        }
    }



}
