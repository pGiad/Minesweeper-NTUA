package com.example.minesweeperntua;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class InitializeGame {
    private final MainGame game;

    public InitializeGame(MainGame game) {
        this.game = game;
    }

    public void init() {
        Tile[][] tiles = new Tile[game.getMinesweeperApp().getGridSize()][game.getMinesweeperApp().getGridSize()];
        for (int i = 0; i < game.getMinesweeperApp().getGridSize(); i++) {
            for (int j = 0; j < game.getMinesweeperApp().getGridSize(); j++) {
                tiles[i][j] = new Tile(i, j, false, false, false, false);
            }
        }

            try {
                Random random = new Random();
                PrintWriter writer = new PrintWriter("medialab/mines.txt");
                Set<String> usedCombinations = new HashSet<>();

                    int superBomb = random.nextInt(game.getMinesweeperApp().getBombs());

                    // Write as many lines as the number of bombs to the file
                    for (int i = 0; i < game.getMinesweeperApp().getBombs(); i++) {
                        int x, y;
                        String combination;
                        do {
                            x = random.nextInt(game.getMinesweeperApp().getGridSize());
                            y = random.nextInt(game.getMinesweeperApp().getGridSize());
                            combination = x + "," + y;
                        } while (usedCombinations.contains(combination));
                        usedCombinations.add(combination);
                        int superBombVal = (game.getMinesweeperApp().isSuperBomb()
                                && (i == superBomb)) ? 1 : 0;
                        writer.println(x + "," + y + "," + superBombVal);
                        tiles[x][y] = new Tile(x, y, true, false, false, game.getMinesweeperApp().isSuperBomb()
                                && (i == superBomb));
                    }
                    writer.close();
                } catch (IOException e) {
                System.out.println("An error occurred while creating mines.txt.");
                e.printStackTrace();
            }

            game.setTiles(tiles);
        }
}
