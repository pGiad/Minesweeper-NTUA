package com.example.minesweeperntua;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GameOverUI {
    private final MainGame mainGame;

    public GameOverUI(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public void gameOver() {
        mainGame.getMinesweeperApp().getGameUI().showSolution();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("GAME OVER");

        ButtonType newGameButton = new ButtonType("New Game");
        ButtonType mainMenuButton = new ButtonType("Main Menu");

        alert.getButtonTypes().setAll(newGameButton, mainMenuButton);

        Platform.runLater(() -> {
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == newGameButton) {
                    mainGame.getMinesweeperApp().loadScenario(mainGame.getMinesweeperApp().getScenarioID());
                    mainGame.getMinesweeperApp().startGame();
                } else if (buttonType == mainMenuButton) {
                    mainGame.getMinesweeperApp().setLoadedScenario(false);
                    mainGame.getMinesweeperApp().mainMenu();
                }
            });
        });
    }
}
