package com.example.minesweeperntua;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.Instant;

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
        exitItem.setOnAction(event -> stage.close());
        applicationMenu.getItems().add(exitItem);

        // Details menu
        Menu detailsMenu = new Menu("Details");

        MenuItem roundsItem = new MenuItem("Rounds");
        roundsItem.setOnAction(event -> {
            MainMenu mainMenu = new MainMenu(mainGame.getMinesweeperApp());
            mainMenu.roundsPopUp(stage);
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
        bombsLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        flagsLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        timeLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        HBox hBox = new HBox(bombsLabel, flagsLabel, timeLabel);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        hBox.setPadding(new Insets(10));
        return hBox;
    }

    private GridPane createGameGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 400);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        Tile[][] tiles = mainGame.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setStyle("-fx-background-color: #700450; -fx-text-fill: #FFFFFF");
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
                        button.setText("💣");
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
}
