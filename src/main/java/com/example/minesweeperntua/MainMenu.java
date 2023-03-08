package com.example.minesweeperntua;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainMenu {
    private final MinesweeperApp minesweeperApp;

    public MainMenu(MinesweeperApp minesweeperApp) {
        this.minesweeperApp = minesweeperApp;
    }

    public void mainMenuContent(Stage primaryStage) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);

        // Add title
        Label titleLabel = new Label("Minesweeper Game");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #700450");
        root.getChildren().add(titleLabel);

        HBox primaryButtons = new HBox();
        primaryButtons.setAlignment(Pos.CENTER);
        primaryButtons.setSpacing(30);

        // Add Load button
        Button loadButton = new Button("Load Scenario");
        loadButton.setStyle("-fx-background-color: #700450; -fx-text-fill: #FFFFFF; -fx-font-size: 15");
        loadButton.setOnAction(event -> {
            loadPopUp(primaryStage);
        });
        primaryButtons.getChildren().add(loadButton);

        // Add Create Scenario button
        Button createButton = new Button("Create Scenario");
        createButton.setStyle("-fx-background-color: #700450; -fx-text-fill: #FFFFFF; -fx-font-size: 15");
        createButton.setOnAction(event -> {
            createPopUp(primaryStage);
        });
        primaryButtons.getChildren().add(createButton);

        // Add Start Game button
        Button startGameButton = new Button("Start Game");
        startGameButton.setStyle("-fx-background-color: #700450; -fx-text-fill: #FFFFFF; -fx-font-size: 15");
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
        primaryButtons.getChildren().add(startGameButton);

        root.getChildren().add(primaryButtons);

        HBox secondaryButtons = new HBox();
        secondaryButtons.setAlignment(Pos.CENTER);
        secondaryButtons.setSpacing(60);

        // Add Rounds button
        Button roundsButton = new Button("Rounds");
        roundsButton.setStyle("-fx-background-color: #700450; -fx-text-fill: #FFFFFF; -fx-font-size: 15");
        roundsButton.setOnAction(event -> {
            roundsPopUp(primaryStage);
        });
        secondaryButtons.getChildren().add(roundsButton);

        // Add Exit button
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: #700450; -fx-text-fill: #FFFFFF; -fx-font-size: 15");
        exitButton.setOnAction(event -> {
            primaryStage.close();
        });
        secondaryButtons.getChildren().add(exitButton);

        root.getChildren().add(secondaryButtons);

        // Set scene and show primary window
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadPopUp(Stage stage) {
        Stage popupStage = new Stage();
        popupStage.initOwner(stage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Load Scenario");

        VBox popupRoot = new VBox();
        popupRoot.setAlignment(Pos.CENTER);
        popupRoot.setSpacing(30);

        // Add title label
        Label titleLabel = new Label("Load Scenario");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #700450");
        titleLabel.setAlignment(Pos.TOP_CENTER);
        popupRoot.getChildren().add(titleLabel);

        // Get list of scenario IDs from files in "medialab" directory
        List<Integer> scenarioIds = new ArrayList<>();
        File medialabDir = new File("medialab");
        File[] files = medialabDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.startsWith("SCENARIO-") && fileName.endsWith(".txt")) {
                    try {
                        int id = Integer.parseInt(fileName.substring(9, fileName.length() - 4));
                        scenarioIds.add(id);
                    } catch (NumberFormatException e) {
                        // Ignore files with invalid scenario IDs
                    }
                }
            }
        }

        // Add dropdown list of scenario IDs
        ObservableList<Integer> options = FXCollections.observableArrayList(scenarioIds);
        ComboBox<Integer> idDropdown = new ComboBox<>(options);
        idDropdown.setPromptText("Select scenario ID");
        idDropdown.setStyle("-fx-font-size: 14");
        popupRoot.getChildren().add(idDropdown);

        // Add "Load" button
        Button loadScenarioButton = new Button("Load");
        loadScenarioButton.setStyle("-fx-background-color: #700450; -fx-text-fill: #FFFFFF; -fx-font-size: 14");
        loadScenarioButton.setOnAction(loadEvent -> {
            Integer scenarioId = idDropdown.getValue();
            if (scenarioId != null) {
                minesweeperApp.loadScenario(scenarioId);
            } else {
                // Show an error message to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Scenario not selected");
                alert.setContentText("Please select a scenario to load.");
                alert.showAndWait();
            }
            popupStage.close();
        });
        popupRoot.getChildren().add(loadScenarioButton);

        // Set scene and show popup window
        Scene popupScene = new Scene(popupRoot, 400, 200);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    public void createPopUp(Stage stage) {
        Stage popupStage = new Stage();
        popupStage.initOwner(stage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Create Scenario");

        VBox popupRoot = new VBox();
        popupRoot.setAlignment(Pos.CENTER);
        popupRoot.setSpacing(15);

        // Add title label
        Label titleLabel = new Label("Create Scenario");
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #700450");
        popupRoot.getChildren().add(titleLabel);

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
        createScenarioButton.setStyle("-fx-background-color: #700450; -fx-text-fill: #FFFFFF; -fx-font-size: 14");
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
            } catch (NullPointerException e) {
                // Show an error message to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Empty Field");
                alert.setContentText("Please choose a value for all the fields.");
                alert.showAndWait();
            }
            popupStage.close();
        });
        popupRoot.getChildren().add(createScenarioButton);

        // Set scene and show popup window
        Scene popupScene = new Scene(popupRoot, 300, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    public void roundsPopUp(Stage stage) {
        Stage popupStage = new Stage();
        popupStage.initOwner(stage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Rounds");

        TableView<ObservableList<String>> table = new TableView<>();
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();


        try {
            BufferedReader reader = new BufferedReader(new FileReader("medialab/rounds.txt"));
            String line;
            int round = 1;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                ObservableList<String> row = FXCollections.observableArrayList();
                row.addAll(String.valueOf(round), fields[0], fields[1], fields[2],
                        (Objects.equals(fields[3], "true") ? "User" : "PC"));
                data.add(row);
                round++;
            }
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File not found");
            alert.setContentText("You have played no games so far.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the columns using the createColumn() method
        TableColumn<ObservableList<String>, String> roundColumn = createColumn("Round", 0);
        TableColumn<ObservableList<String>, String> bombsColumn = createColumn("Number of Mines", 1);
        TableColumn<ObservableList<String>, String> triesColumn = createColumn("Number of Tries", 2);
        TableColumn<ObservableList<String>, String> timeColumn = createColumn("Game Time", 3);
        TableColumn<ObservableList<String>, String> winnerColumn = createColumn("Winner", 4);

        table.setItems(data);
        table.getColumns().addAll(roundColumn, bombsColumn, triesColumn, timeColumn, winnerColumn);

        // Create a VBox to hold the table
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);
        vbox.setPrefWidth(450);
        vbox.setPrefHeight(200);

        // Create a new scene and set the stage
        Scene scene = new Scene(vbox, 450, 200);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
    private TableColumn<ObservableList<String>, String> createColumn(String columnName, int columnIndex) {
        TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER);
                }
            }
        });
        return column;
    }

}
