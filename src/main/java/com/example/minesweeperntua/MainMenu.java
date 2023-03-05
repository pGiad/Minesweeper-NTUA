package com.example.minesweeperntua;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

import static javafx.application.Platform.exit;

public class MainMenu {
    private MinesweeperApp minesweeperApp;
    private Stage primaryStage;

    public MainMenu(MinesweeperApp minesweeperApp) {
        this.minesweeperApp = minesweeperApp;
    }

    public void mainMenuContent(Stage primaryStage) {
        this.primaryStage = primaryStage;
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        // Add title
        Label titleLabel = new Label("Minesweeper Game");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        root.getChildren().add(titleLabel);

        // Add Load button
        Button loadButton = new Button("Load Scenario");
        loadButton.setOnAction(event -> {
            Stage popupStage = new Stage();
            popupStage.initOwner(primaryStage);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            VBox popupRoot = new VBox();
            popupRoot.setAlignment(Pos.CENTER);
            popupRoot.setSpacing(10);

            // Add text input field
            TextField idField = new TextField();
            idField.setPromptText("Enter scenario ID");
            popupRoot.getChildren().add(idField);

            // Add "Load" button
            Button loadScenarioButton = new Button("Load");
            loadScenarioButton.setOnAction(loadEvent -> {
                String scenarioId = idField.getText();
                try {
                    minesweeperApp.loadScenario(Integer.parseInt(scenarioId));
                } catch (NumberFormatException e) {
                    // Show an error message to the user
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Scenario not found");
                    alert.setContentText("The scenario ID should be an integer.");
                    alert.showAndWait();
                }
                popupStage.close();
            });
            popupRoot.getChildren().add(loadScenarioButton);

            // Set scene and show popup window
            Scene popupScene = new Scene(popupRoot, 200, 100);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        });
        root.getChildren().add(loadButton);

        // Add Create Scenario button
        Button createButton = new Button("Create Scenario");
        createButton.setOnAction(event -> {
            Stage popupStage = new Stage();
            popupStage.initOwner(primaryStage);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            VBox popupRoot = new VBox();
            popupRoot.setAlignment(Pos.CENTER);
            popupRoot.setSpacing(10);

            // Add text input fields
            TextField idField = new TextField();
            idField.setPromptText("Enter scenario ID");
            popupRoot.getChildren().add(idField);

            List<Integer> difficultyValues = Arrays.asList(1, 2);
            ComboBox<Integer> difficultyComboBox = new ComboBox<>();
            difficultyComboBox.getItems().addAll(difficultyValues);
            difficultyComboBox.setPromptText("Select difficulty level");
            popupRoot.getChildren().add(difficultyComboBox);

            ComboBox<Integer> bombsComboBox = new ComboBox<>();
            bombsComboBox.setPromptText("Select number of mines");
            popupRoot.getChildren().add(bombsComboBox);

            ComboBox<Integer> timeComboBox = new ComboBox<>();
            timeComboBox.setPromptText("Select time in seconds");
            popupRoot.getChildren().add(timeComboBox);

            ComboBox<Integer> superBombComboBox = new ComboBox<>();
            superBombComboBox.setPromptText("Select if super-mine exists");
            popupRoot.getChildren().add(superBombComboBox);

            // Update fields when difficulty is changed
            difficultyComboBox.setOnAction(e -> {
                int difficulty = difficultyComboBox.getValue();
                bombsComboBox.getItems().clear();
                timeComboBox.getItems().clear();
                superBombComboBox.getItems().clear();
                if (difficulty == 1) {
                    for (int i = 9; i <= 11; i++) {
                        bombsComboBox.getItems().add(i);
                    }
                    for (int i = 120; i <= 180; i++) {
                        timeComboBox.getItems().add(i);
                    }
                    superBombComboBox.getItems().add(0);
                } else {
                    for (int i = 35; i <= 45; i++) {
                        bombsComboBox.getItems().add(i);
                    }
                    for (int i = 240; i <= 360; i++) {
                        timeComboBox.getItems().add(i);
                    }
                    superBombComboBox.getItems().add(0);
                    superBombComboBox.getItems().add(1);
                }
            });

            // Add "Create" button
            Button createScenarioButton = new Button("Create");
            createScenarioButton.setOnAction(loadEvent -> {
                String scenarioId = idField.getText();
                Integer difficulty = difficultyComboBox.getValue();
                Integer bombs = bombsComboBox.getValue();
                Integer time = timeComboBox.getValue();
                Integer superBomb = superBombComboBox.getValue();
                try {
                    minesweeperApp.createScenario(Integer.parseInt(scenarioId),
                            difficulty,
                            bombs,
                            time,
                            superBomb);
                } catch (NumberFormatException e) {
                    // Show an error message to the user
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Value");
                    alert.setContentText("All values should be integers.");
                    alert.showAndWait();
                }
                popupStage.close();
            });
            popupRoot.getChildren().add(createScenarioButton);

            // Set scene and show popup window
            Scene popupScene = new Scene(popupRoot, 300, 300);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        });
        root.getChildren().add(createButton);

        // Add Start Game button
        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(event -> {
            if (minesweeperApp.isLoadedScenario()) {
                minesweeperApp.startGame();
            } else {
                // Show an error message to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Scenario Loaded");
                alert.setContentText("Before you start a game you should load a scenario.");
                alert.showAndWait();
            }
        });
        root.getChildren().add(startGameButton);

        // Add Exit button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> {
            primaryStage.close();
        });
        root.getChildren().add(exitButton);

        // Set scene and show primary window
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
