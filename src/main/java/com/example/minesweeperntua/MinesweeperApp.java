package com.example.minesweeperntua;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MinesweeperApp extends Application {

    private int scenarioID;
    private int difficulty;
    private int gridSize;
    private int bombs;
    private int time;
    private boolean superBomb;
    private boolean loadedScenario = false;
    private Stage primaryStage;
    private GameUI gameUI;

    public int getScenarioID() {
        return scenarioID;
    }

    public void setScenarioID(int scenarioID) {
        this.scenarioID = scenarioID;
    }

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

    public GameUI getGameUI() {
        return gameUI;
    }

    public void setGameUI(GameUI gameUI) {
        this.gameUI = gameUI;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        primaryStage.setTitle("MediaLab Minesweeper");
        primaryStage.getIcons().add(new Image("mine.jpg"));
        mainMenu();
    }

    public void mainMenu() {
        MainMenu mainMenu = new MainMenu(this);
        mainMenu.mainMenuContent(primaryStage);
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
        setGameUI(gameUI);
    }

    public static void main(String[] args) {
        launch();
    }
}