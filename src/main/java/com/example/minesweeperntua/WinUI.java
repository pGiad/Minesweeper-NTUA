package com.example.minesweeperntua;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class WinUI {
    private final MainGame mainGame;

    public WinUI(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public void win() {
        mainGame.getMinesweeperApp().getGameUI().showSolution();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Win");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! You Won the Game!");

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
