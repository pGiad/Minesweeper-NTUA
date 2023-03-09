# Minesweeper-NTUA
- Semester project for Multimedia class at ECE NTUA.
- In this project we had to implement a variation of the Minesweeper game, in which there is a super-mine and a timer. If the player flags a super-mine in the first 4 tries then all tiles in the same row and all the tiles in the same column with the super-mine are opened. If the timer ends the user loses the game.
- The player can create new scenarios and load scenarios that already exist in `medialab` directory. Scenarios define the difficulty of the game, the number of mines, the available time and the condition if there is a super-mine.
- Each time player starts a new game the mines are randomly generated in different positions. These positions can be found in the `mines.txt` file under the `medialab` directory. At this file user can also see whether a mine is super-mine or not.
- The last 5 rounds results can be found at the `rounds.txt` file under the `medialab`.
