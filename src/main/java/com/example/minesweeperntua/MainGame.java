package com.example.minesweeperntua;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainGame {
    private final MinesweeperApp minesweeperApp;
    private Tile[][] tiles;
    private int numOfOpenedTiles = 0;
    private int usedFlags = 0;
    private int tries = 0;
    private boolean gameFinished = false;

    public MainGame(MinesweeperApp minesweeperApp) {
        this.minesweeperApp = minesweeperApp;
        minesweeperApp.setLoadedScenario(false);
        this.tiles = new Tile[getMinesweeperApp().getGridSize()][getMinesweeperApp().getGridSize()];
    }

    public MinesweeperApp getMinesweeperApp() {
        return minesweeperApp;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public int getNumOfOpenedTiles() {
        return numOfOpenedTiles;
    }

    public void setNumOfOpenedTiles(int numOfOpenedTiles) {
        this.numOfOpenedTiles = numOfOpenedTiles;
    }

    public int getUsedFlags() {
        return usedFlags;
    }

    public void setUsedFlags(int usedFlags) {
        this.usedFlags = usedFlags;
    }

    public int getTries() {
        return tries;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public int getBombNeighbors(List<Tile> neighbors) {
        int bombNeighbors = 0;

        for (Tile neighbor : neighbors) {
            if (neighbor.isBomb()) {
                bombNeighbors++;
            }
        }
        return bombNeighbors;
    }

    public List<Tile> getNeighbors(Tile tile, int difficulty) {
        List<Tile> neighbors = new ArrayList<>();

        int maxTile;
        if (difficulty == 1) {
            maxTile = 9;
        } else {
            maxTile = 16;
        }

        int[] points = new int[]{
                -1, -1,
                -1, 0,
                -1, 1,
                0, -1,
                0, 1,
                1, -1,
                1, 0,
                1, 1
        };

        for (int i = 0; i < points.length; i++) {
            int dx = points[i];
            int dy = points[++i];

            int newX = tile.getX() + dx;
            int newY = tile.getY() + dy;

            if (newX >= 0 && newX < maxTile && newY >= 0 && newY < maxTile) {
                neighbors.add(this.tiles[newX][newY]);
            }
        }
        return neighbors;
    }

    public void open(Tile tile) {
        if (tile.isOpen()) {
            return;
        }

        if (tile.isBomb()) {
            updateRounds(minesweeperApp.getBombs(), tries, minesweeperApp.getTime(), false);
            gameFinished = true;
            GameOverUI gameOverUI = new GameOverUI(this);
            gameOverUI.gameOver();
            return;
        }

        tile.setOpen(true);
        if (tile.isFlag()) {
            tile.setFlag(false);
            usedFlags--;
        }

        this.numOfOpenedTiles++;

        List<Tile> neighbors = getNeighbors(tile, minesweeperApp.getDifficulty());
        if (getBombNeighbors(neighbors) == 0) {
            for (Tile neighbor : neighbors) {
                if (!neighbor.isBomb())
                    open(neighbor);
            }
        }

        if (numOfOpenedTiles == minesweeperApp.getGridSize() * minesweeperApp.getGridSize()
                - minesweeperApp.getBombs()) {
            updateRounds(minesweeperApp.getBombs(), tries, minesweeperApp.getTime(), true);
            gameFinished = true;
            WinUI winUI = new WinUI(this);
            winUI.win();
        }
    }

    public void flag(Tile tile) {
        if (tile.isOpen() || (usedFlags == minesweeperApp.getBombs() && !tile.isFlag())) {
            return;
        }

        if (tile.isSuperBomb() && this.tries < 5) {
            for (int i = 0; i < 16; i++) {
                tiles[tile.getX()][i].setOpen(true);
                tiles[i][tile.getY()].setOpen(true);
                if (tiles[tile.getX()][i].isFlag()) {
                    tiles[tile.getX()][i].setFlag(false);
                    usedFlags--;
                }
                if (tiles[i][tile.getY()].isFlag()) {
                    tiles[i][tile.getY()].setFlag(false);
                    usedFlags--;
                }
                if (!tiles[tile.getX()][i].isBomb()) {
                    this.numOfOpenedTiles++;
                }
                if (!tiles[i][tile.getY()].isBomb()) {
                    this.numOfOpenedTiles++;
                }
            }
            if (numOfOpenedTiles == minesweeperApp.getGridSize() * minesweeperApp.getGridSize()
                    - minesweeperApp.getBombs()) {
                updateRounds(minesweeperApp.getBombs(), tries, minesweeperApp.getTime(), true);
                gameFinished = true;
                WinUI winUI = new WinUI(this);
                winUI.win();
            }
            return;
        }

        if (tile.isFlag()) {
            tile.setFlag(false);
            usedFlags--;
        } else {
            tile.setFlag(true);
            usedFlags++;
        }
    }

    public void updateRounds(int bombs, int tries, int time, boolean userWon) {
        try {
            String filename = "medialab/rounds.txt";
            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            LinkedList<String> lines = new LinkedList<>();

            String round;
            while ((round = bufferedReader.readLine()) != null) {
                lines.add(round);
            }

            bufferedReader.close();
            fileReader.close();

            if (lines.size() == 5) {
                lines.removeLast();
            }

            lines.addFirst(bombs + "," + tries + "," + time + "," + userWon);

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String l : lines) {
                bufferedWriter.write(l);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
