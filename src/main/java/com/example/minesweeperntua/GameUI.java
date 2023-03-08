package com.example.minesweeperntua;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class GameUI extends BorderPane {
    private final MainGame mainGame;
    private final Stage stage;
    private Label bombsLabel;
    private Label flagsLabel;
    private Label timeLabel;

    public GameUI(MainGame mainGame, Stage stage) {
        this.stage = stage;
        this.mainGame = mainGame;
    }

    public void createBoard() {
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox(createMenuBar(), createTopPane());
        borderPane.setTop(vBox);
        borderPane.setCenter(createGameGrid());

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();

        createTimer(Duration.ofSeconds(mainGame.getMinesweeperApp().getTime()));
        createMenuBar();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Application menu
        Menu applicationMenu = new Menu("Application");

        MenuItem createItem = new MenuItem("Create");
        createItem.setOnAction(event -> {
            MainMenu mainMenu = new MainMenu(mainGame.getMinesweeperApp());
            mainMenu.createPopUp(stage);
        });
        applicationMenu.getItems().add(createItem);

        MenuItem loadItem = new MenuItem("Load");
        loadItem.setOnAction(event -> {
            MainMenu mainMenu = new MainMenu(mainGame.getMinesweeperApp());
            mainMenu.loadPopUp(stage);
        });
        applicationMenu.getItems().add(loadItem);

        MenuItem startItem = new MenuItem("Start");
        startItem.setOnAction(event -> {
            if (mainGame.getMinesweeperApp().isLoadedScenario()) {
                mainGame.getMinesweeperApp().startGame();
            } else {
                // Show an error message to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Scenario Loaded");
                alert.setContentText("Before you start a game you should load a scenario.");
                alert.showAndWait();
            }
        });
        applicationMenu.getItems().add(startItem);

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> {
            stage.close();
        });
        applicationMenu.getItems().add(exitItem);

        // Details menu
        Menu detailsMenu = new Menu("Details");

        MenuItem roundsItem = new MenuItem("Rounds");
        roundsItem.setOnAction(event -> {
            roundsPopUp();
        });
        detailsMenu.getItems().add(roundsItem);

        MenuItem solutionItem = new MenuItem("Solution");
        solutionItem.setOnAction(event -> {
            mainGame.updateRounds(mainGame.getMinesweeperApp().getBombs(), mainGame.getTries()
                    , mainGame.getMinesweeperApp().getTime(), false);
            GameOverUI gameOverUI = new GameOverUI(mainGame);
            gameOverUI.gameOver();
        });
        detailsMenu.getItems().add(solutionItem);

        // Add menus to menu bar
        menuBar.getMenus().addAll(applicationMenu, detailsMenu);

        return menuBar;
    }

    public void showSolution() {
        mainGame.setGameFinished(true);
        Tile[][] tiles = mainGame.getTiles();
        for (Tile[] value : tiles) {
            for (Tile tile : value) {
                tile.setOpen(true);
                if (tile.isFlag()) {
                    tile.setFlag(false);
                    mainGame.setUsedFlags(mainGame.getUsedFlags() - 1);
                }
            }
        }
        updateUI();
    }

    private void createTimer(Duration duration) {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(duration);
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            Duration timeLeft = Duration.between(Instant.now(), endTime);
            if (timeLeft.isZero() || timeLeft.isNegative() || mainGame.isGameFinished()) {
                timeline.stop();
                if (!mainGame.isGameFinished()) {
                    mainGame.setGameFinished(true);
                    mainGame.updateRounds(mainGame.getMinesweeperApp().getBombs(), mainGame.getTries()
                            , mainGame.getMinesweeperApp().getTime(), false);
                    GameOverUI gameOverUI = new GameOverUI(mainGame);
                    gameOverUI.gameOver();
                }
                return;
            }
            timeLabel.setText("Time Left: " + timeLeft.toSeconds());
        });
        timeline.getKeyFrames().addAll(kf, new KeyFrame(javafx.util.Duration.seconds(1)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    private HBox createTopPane() {
        bombsLabel = new Label("Mines: " + mainGame.getMinesweeperApp().getBombs());
        flagsLabel = new Label("Flags Used: " + mainGame.getUsedFlags() + "/" + mainGame.getMinesweeperApp().getBombs());
        timeLabel = new Label("Time Left: " + mainGame.getMinesweeperApp().getTime());
        HBox hBox = new HBox(bombsLabel, flagsLabel, timeLabel);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(10));
        return hBox;
    }

    private GridPane createGameGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 400);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Tile[][] tiles = mainGame.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        mainGame.open(tile);
                        if (!tile.isBomb()) {
                            mainGame.setTries(mainGame.getTries() + 1);
                        }
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        mainGame.flag(tile);
                    }
                    updateUI();
                });
                gridPane.add(button, i, j);
            }
        }
        return gridPane;
    }

    private void updateUI() {
        bombsLabel.setText("Mines: " + mainGame.getMinesweeperApp().getBombs());
        flagsLabel.setText("Flags Used: " + mainGame.getUsedFlags() + "/" + mainGame.getMinesweeperApp().getBombs());
        Tile[][] tiles = mainGame.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Button button = (Button) ((GridPane) ((BorderPane) bombsLabel.getParent().getParent().getParent()).getCenter())
                        .getChildren().get(i * tiles.length + j);
                Tile tile = tiles[i][j];
                if (tile.isOpen()) {
                    button.setDisable(true);
                    if (tile.isBomb()) {
                        button.setText("X");
                    } else {
                        int adjacentBombs = mainGame.getBombNeighbors(mainGame.getNeighbors(tile
                                , mainGame.getMinesweeperApp().getDifficulty()));
                        if (adjacentBombs > 0) {
                            button.setText(String.valueOf(adjacentBombs));
                        } else {
                            button.setText("");
                        }
                    }
                } else {
                    button.setText(tile.isFlag() ? "🚩" : "");
                }
            }
        }
    }

    private void roundsPopUp() {
        Stage popupStage = new Stage();
        popupStage.initOwner(stage);
        popupStage.initModality(Modality.APPLICATION_MODAL);

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
