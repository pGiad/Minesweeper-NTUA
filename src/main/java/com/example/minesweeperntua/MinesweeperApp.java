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
    private boolean loadedScenario = false;
    private Stage primaryStage;

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

    public boolean isLoadedScenario() {
        return loadedScenario;
    }

    public void setLoadedScenario(boolean loadedScenario) {
        this.loadedScenario = loadedScenario;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(MinesweeperApp.class.getResource("hello-view.fxml"));
        this.primaryStage = stage;
        primaryStage.setTitle("MediaLab Minesweeper");
        MainMenu mainMenu = new MainMenu(this);
        mainMenu.mainMenuContent(primaryStage);
//        MainGame mainGame = new MainGame(this);
//        InitializeGame initializeGame = new InitializeGame(mainGame);
//        initializeGame.init();
//        System.out.println(mainGame.getBombNeighbors(mainGame.getNeighbors(mainGame.getTiles()[0][0], 2)));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
    }

    public void loadScenario(int scenarioID) {
        LoadScenarioController loadScenarioController = new LoadScenarioController(this);
        loadScenarioController.onLoadButtonClick(scenarioID);
    }

    public void createScenario(int scenarioID, int difficulty, int bombs, int time, int superBomb) {
        CreateScenarioController createScenarioController = new CreateScenarioController(this);
        createScenarioController.onCreateButtonClick(scenarioID, difficulty, bombs, time, superBomb);
    }

    public void startGame() {
        MainGame mainGame = new MainGame(this);
        InitializeGame initializeGame = new InitializeGame(mainGame);
        initializeGame.init();
        GameUI gameUI = new GameUI(mainGame, primaryStage);
        gameUI.createBoard();
    }

    public static void main(String[] args) {
        launch();
    }
}