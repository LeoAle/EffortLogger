package com.example.effortlogger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamLeadControl {

    String username;
    public void setUsername(String username) {
        this.username = username;
    }

    public static void main(String[] args) throws IOException {
        int teamNumber = 12;
        printTeamWork(teamNumber);
    }

    public static void printTeamWork(int teamNumber) throws IOException {
        List<String> teamUsernames = getUsernamesByTeam(teamNumber);
        List<String> projectNames = getProjectNames("data.csv");

        for (String projectName : projectNames) {
            System.out.println("Project: " + projectName);
            String projectFileName = projectName + ".csv";

            for (String username : teamUsernames) {
                System.out.println("Username: " + username);
                printUserWork(username, projectFileName);
            }
        }
    }

    public static List<String> getUsernamesByTeam(int teamNumber) throws IOException {
        List<String> usernames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("credentials.csv"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (Integer.parseInt(parts[3]) == teamNumber) {
                    usernames.add(parts[0]);
                }
            }
        }

        return usernames;
    }

    public static List<String> getProjectNames(String dataFileName) throws IOException {
        List<String> projectNames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFileName))) {
            String line = reader.readLine(); // Read the first line with project names
            String[] parts = line.split(",");

            for (String projectName : parts) {
                projectNames.add(projectName.trim());
            }
        }

        return projectNames;
    }

    public static void printUserWork(String username, String projectFileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(projectFileName))) {
            String line;

            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s | %-10s | %-10s | %-20s | %-20s | %-10s | %-20s | %-20s%n",
                    "Sr. No.", "Username", "Project", "Life Cycle Step", "Effort Category", "Plan", "Deliverable", "Elapsed Time");
            System.out.println("--------------------------------------------------------------------------------------------------------------------");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts[1].equals(username)) {
                    String srNo = parts[0].trim();
                    String project = parts[2].trim();
                    String lifeCycleStep = parts[3].trim();
                    String effortCategory = parts[4].trim();
                    String plan = parts[5].trim();
                    String deliverable = parts[6].trim();
                    String elapsedTime = parts[7].trim();

                    System.out.printf("%-5s | %-10s | %-10s | %-20s | %-20s | %-10s | %-20s | %-20s%n",
                            srNo, username, project, lifeCycleStep, effortCategory, plan, deliverable, elapsedTime);
                }
            }
        }
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
