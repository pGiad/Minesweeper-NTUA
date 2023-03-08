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

    /**
     * Constructs a new instance of the MainGame class with the specified Minesweeper instance.
     *
     * @param minesweeperApp the MinesweeperApp instance. Used to initialize the `minesweeperApp` field and the `tiles`
     *                       array with the size of the grid retrieved from `MinesweeperApp`.
     */
    public MainGame(MinesweeperApp minesweeperApp) {
        this.minesweeperApp = minesweeperApp;
        minesweeperApp.setLoadedScenario(false);
        this.tiles = new Tile[getMinesweeperApp().getGridSize()][getMinesweeperApp().getGridSize()];
    }

    /**
     * Returns the MinesweeperApp instance associated with this game. It is widely used from other classes to access the
     * fields of MinesweeperApp like the difficulty of the game, the number of mines, the time, if there is a super-mine
     * etc.
     *
     * @return the MinesweeperApp instance
     */
    public MinesweeperApp getMinesweeperApp() {
        return minesweeperApp;
    }

    /**
     * Returns the game board as a 2D array of Tiles. Tile class has important information about each tile like if it is
     * opened or flagged, if it is a mine or super-mine.
     *
     * @return the game board
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Sets the game board to the specified 2D array of Tiles.
     *
     * @param tiles the new game board
     */
    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    /**
     * Returns the number of flags used in the game.
     *
     * @return the number of flags used
     */
    public int getUsedFlags() {
        return usedFlags;
    }

    /**
     * Sets the number of flags used in the game.
     *
     * @param usedFlags the number of flags used
     */
    public void setUsedFlags(int usedFlags) {
        this.usedFlags = usedFlags;
    }

    /**
     * Returns the number of tries made (each successful left click is a try).
     *
     * @return the number of tries made
     */
    public int getTries() {
        return tries;
    }

    /**
     * Sets the number of tries made.
     *
     * @param tries the number of tries made
     */
    public void setTries(int tries) {
        this.tries = tries;
    }

    /**
     * Returns whether the game is finished or not.
     *
     * @return {@code true} if the game is finished, {@code false} otherwise
     */
    public boolean isGameFinished() {
        return gameFinished;
    }

    /**
     * Sets the game finished state.
     *
     * @param gameFinished {@code true} if the game is finished, {@code false} otherwise
     */
    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    /**
     * Returns the number of mine neighbors for the given list of tiles.
     *
     * @param neighbors the list of neighboring tiles
     * @return the number of mine neighbors for the given list of tiles
     */
    public int getBombNeighbors(List<Tile> neighbors) {
        int bombNeighbors = 0;

        for (Tile neighbor : neighbors) {
            if (neighbor.isBomb()) {
                bombNeighbors++;
            }
        }
        return bombNeighbors;
    }

    /**
     * Returns a list of neighboring tiles for the given tile and difficulty level.
     *
     * @param tile the tile to get neighbors for
     * @param difficulty the difficulty level (1 for easy, 2 for medium)
     * @return a list of neighboring tiles for the given tile and difficulty level
     */
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

    /**
     * Opens the given tile.
     *
     * @param tile the tile to open
     */
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

    /**
     * Toggles the flag state of a given tile, unless the tile is already open or the player
     * has already used all their flags and is trying to add another flag.
     *
     * @param tile The tile to toggle the flag state of.
     */
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

    /**
     * Updates the list of recent rounds in the rounds.txt file with a new entry representing the
     * current game, containing the number of mines, the number of tries, the time taken, and whether
     * the user won or not.
     *
     * @param bombs    The number of mines in the current game.
     * @param tries    The number of tries taken in the current game.
     * @param time     The time taken to complete the current game, in seconds.
     * @param userWon  True if the user won the current game, false otherwise.
     */
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
