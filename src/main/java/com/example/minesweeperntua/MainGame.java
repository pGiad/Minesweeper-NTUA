package com.example.minesweeperntua;

public class MainGame {
    private final MinesweeperApp minesweeperApp;
    private Tile[][] tiles;

    public MainGame(MinesweeperApp minesweeperApp) {
        this.minesweeperApp = minesweeperApp;
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

    public int getNeighbors(Tile tile, int difficulty) {
        int neighbors = 0;
        int maxTile;
        if (difficulty == 1) {
            maxTile = 9;
        } else {
            maxTile = 16;
        }

        for (int i = tile.getX() - 1; i <= tile.getX() + 1; i++) {
            for (int j = tile.getY() - 1; j <= tile.getY() + 1; j++) {
                if (i >= 0 && j >= 0 && i < maxTile && j < maxTile) {
                    if (this.tiles[i][j].isBomb() && this.tiles[i][j] != tile) {
                        neighbors++;
                    }
                }
            }
        }
        return neighbors;
    }
}
