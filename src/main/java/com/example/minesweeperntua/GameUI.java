package com.example.minesweeperntua;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.Instant;

public class GameUI extends BorderPane {
    private MainGame mainGame;
    private Stage stage;
    private Label bombsLabel;
    private Label flagsLabel;
    private Label timeLabel;

    public GameUI(MainGame mainGame, Stage stage) {
        this.stage = stage;
        this.mainGame = mainGame;
    }

    public void createBoard() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createTopPane());
        borderPane.setCenter(createGameGrid());

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();

        createTimer(Duration.ofSeconds(mainGame.getMinesweeperApp().getTime()));
    }

    private void createTimer(Duration duration) {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(duration);
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            Duration timeLeft = Duration.between(Instant.now(), endTime);
            if (timeLeft.isZero() || timeLeft.isNegative()) {
                System.out.println("Time's up!");
                // Handle game over here
                return;
            }
            timeLabel.setText("Time Left: " + timeLeft.toSeconds());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    private HBox createTopPane() {
        bombsLabel = new Label("Bombs: " + mainGame.getMinesweeperApp().getBombs());
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
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        mainGame.flag(tile);
                    }
                    updateUI();
                });
                gridPane.add(button, j, i);
            }
        }
        return gridPane;
    }

    private void updateUI() {
        bombsLabel.setText("Bombs: " + mainGame.getMinesweeperApp().getBombs());
        flagsLabel.setText("Flags Used: " + mainGame.getUsedFlags() + "/" + mainGame.getMinesweeperApp().getBombs());
        Tile[][] tiles = mainGame.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Button button = (Button) ((GridPane) ((BorderPane) bombsLabel.getParent().getParent()).getCenter())
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
                    button.setText(tile.isFlag() ? "F" : "");
                }
            }
        }
    }
}
