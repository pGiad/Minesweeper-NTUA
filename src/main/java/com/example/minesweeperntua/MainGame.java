package com.example.minesweeperntua;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainGame {
    private final MinesweeperApp minesweeperApp;
    private Tile[][] tiles;
    private int numOfOpenedTiles = 0;
    private int usedFlags = 0;
    private int tries = 0;
    private Timer timer;

    public MainGame(MinesweeperApp minesweeperApp) {
        this.minesweeperApp = minesweeperApp;
        this.tiles = new Tile[getMinesweeperApp().getGridSize()][getMinesweeperApp().getGridSize()];
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("You lost the game - timer ran out");
                // Game over - do what you need to do to end the game
            }
        }, minesweeperApp.getTime() * 1000L);
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
//            app.getScene().setRoot(GameOverUI.gameOverContent(app));
//            game lost...
            System.out.println("You lost the game");
            return;
        }

        tile.setOpen(true);
//        text.setVisible(true);
//        border.setFill(null);
//        print the number of neighbors using getBombNeighbors
        this.numOfOpenedTiles++;

        List<Tile> neighbors = getNeighbors(tile, minesweeperApp.getDifficulty());
        for (Tile neighbor : neighbors) {
            if (!neighbor.isBomb())
                open(neighbor);
        }

        this.tries++;

        if (numOfOpenedTiles == minesweeperApp.getGridSize() * minesweeperApp.getGridSize()
                - minesweeperApp.getBombs()) {
//            app.getScene().setRoot(WinUI.winContent(app));
//            game won
            System.out.println("You won the game");
        }
    }

    public void flag(Tile tile) {
        if (tile.isOpen() || usedFlags == minesweeperApp.getBombs()) {
            return;
        }

        if (tile.isSuperBomb() && this.tries < 5) {
            for (int i = 0; i < 16; i++) {
                tiles[tile.getX()][i].setOpen(true);
                tiles[i][tile.getY()].setOpen(true);
                if (!tiles[tile.getX()][i].isBomb()) {
                    this.numOfOpenedTiles++;
                    // show tile as expected
                } else {
                    // show bomb tile
                }
                if (!tiles[i][tile.getY()].isBomb()) {
                    this.numOfOpenedTiles++;
                    // show tile as expected
                } else {
                    // show bomb tile
                }
            }
            if (numOfOpenedTiles == minesweeperApp.getGridSize() * minesweeperApp.getGridSize()
                    - minesweeperApp.getBombs()) {
//            app.getScene().setRoot(WinUI.winContent(app));
//            game won
                System.out.println("You won the game");
            }
        }

        if (tile.isFlag()) {
            tile.setFlag(false);
            usedFlags--;
        } else {
            tile.setFlag(true);
            usedFlags++;
        }
    }
}
