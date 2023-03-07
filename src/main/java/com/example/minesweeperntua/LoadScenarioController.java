package com.example.minesweeperntua;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.*;

public class LoadScenarioController {
    private final MinesweeperApp minesweeperApp;

    public LoadScenarioController(MinesweeperApp minesweeperApp) {
        this.minesweeperApp = minesweeperApp;
    }

    public static class InvalidValueException extends Exception {
        public InvalidValueException(String message) {
            super(message);
        }
    }

    public static class InvalidDescriptionException extends Exception {
        public InvalidDescriptionException(String message) {
            super(message);
        }
    }

    @FXML
    public void onLoadButtonClick(int scenarioID) {
        try {
            int difficulty, bombs, time, superBomb;
            // Create file object
            File file = new File("medialab/SCENARIO-" + scenarioID + ".txt");

            // Create file input stream
            FileInputStream fis = new FileInputStream(file);
            FileInputStream fisCheckLines = new FileInputStream(file);

            // Create buffer
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            BufferedReader numOfLines = new BufferedReader(new InputStreamReader(fisCheckLines));

            // Check for valid number of lines
            String checkLines;
            for(int i = 0; i < 4; i++) {
                checkLines = numOfLines.readLine();
                if (checkLines == null) {
                    throw new InvalidDescriptionException("File should have exactly 4 lines");
                }
            }
            if (numOfLines.readLine() != null) {
                throw new InvalidDescriptionException("File should have exactly 4 lines");
            }

            // Read first line
            String line = br.readLine();

            // Check first line
            try {
                difficulty = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                throw new InvalidValueException("Invalid third line value: " + line);
            }
            if (!line.equals("1") && !line.equals("2")) {
                throw new InvalidValueException("Invalid first line value: " + line);
            }

            String line1 = line;
            // Read second line
            line = br.readLine();

            // Check second line
            try {
                bombs = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                throw new InvalidValueException("Invalid third line value: " + line);
            }
            if ((line1.equals("1") && (bombs < 9 || bombs > 11)) ||
                    (line1.equals("2") && (bombs < 35 || bombs > 45))) {
                throw new InvalidValueException("Invalid second line value: " + line);
            }

            // Read third line
            line = br.readLine();

            // Check third line
            try {
                time = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                throw new InvalidValueException("Invalid third line value: " + line);
            }
            if ((line1.equals("1") && (time < 120 || time > 180)) ||
                    (line1.equals("2") && (time < 240 || time > 360))) {
                throw new InvalidValueException("Invalid third line value: " + line);
            }

            // Read fourth line
            line = br.readLine();

            // Check fourth line
            try {
                superBomb = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                throw new InvalidValueException("Invalid third line value: " + line);
            }
            if ((line1.equals("1") && superBomb != 0) ||
                    (line1.equals("2") && (superBomb < 0 || superBomb > 1))) {
                throw new InvalidValueException("Invalid fourth line value: " + line);
            }

            // Close buffer and file input stream
            br.close();
            fis.close();

            minesweeperApp.setScenarioID(scenarioID);
            minesweeperApp.setDifficulty(difficulty);
            if (difficulty == 1) {
                minesweeperApp.setGridSize(9);
            } else {
                minesweeperApp.setGridSize(16);
            }
            minesweeperApp.setBombs(bombs);
            minesweeperApp.setTime(time);
            minesweeperApp.setSuperBomb(superBomb != 0);
            minesweeperApp.setLoadedScenario(true);

        } catch (FileNotFoundException e) {
            // Show an error message to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Scenario not found");
            alert.setContentText("The scenario with ID " + scenarioID + " was not found.");
            alert.showAndWait();
        } catch (InvalidValueException | InvalidDescriptionException | IOException e) {
            e.printStackTrace();
        }
    }
}