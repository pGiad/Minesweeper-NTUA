package com.example.minesweeperntua;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class CreateScenarioController {
    private final MinesweeperApp minesweeperApp;

    public CreateScenarioController(MinesweeperApp minesweeperApp) { this.minesweeperApp = minesweeperApp; }

    public void onCreateButtonClick(int scenarioID, int difficulty, int bombs, int time, int superBomb) {
        try {
            String filename = "medialab/SCENARIO-" + scenarioID + ".txt";
            File file = new File(filename);

            if (file.exists()) {
                throw new FileAlreadyExistsException(filename + " already exists");
            }

            FileWriter writer = new FileWriter(file);
            writer.write(difficulty + "\n");
            writer.write(bombs + "\n");
            writer.write(time + "\n");
            writer.write(superBomb + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
