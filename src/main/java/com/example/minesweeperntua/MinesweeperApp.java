package com.example.minesweeperntua;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class MinesweeperApp extends Application {

    private int difficulty;
    private int gridSize;
    private int bombs;
    private int time;
    private boolean superBomb;

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isSuperBomb() {
        return superBomb;
    }

    public void setSuperBomb(boolean superBomb) {
        this.superBomb = superBomb;
    }

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(MinesweeperApp.class.getResource("hello-view.fxml"));
//        CreateScenarioController createScenarioController = new CreateScenarioController(this);
//        createScenarioController.onCreateButtonClick(2, 1, 11, 120, 0);
        LoadScenarioController loadScenarioController = new LoadScenarioController(this);
        loadScenarioController.onLoadButtonClick(1);
        MainGame mainGame = new MainGame(this);
        InitializeGame initializeGame = new InitializeGame(mainGame);
        initializeGame.init();
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}