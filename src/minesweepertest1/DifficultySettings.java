package minesweepertest1;

/**
 * Represents the difficulty settings for a Minesweeper game.
 * Contains information on board dimensions and mine count based on difficulty.
 

 * @author Grisham Balloo 20099072
 */

   
public enum DifficultySettings {
    EASY(new int[][] {{8, 8, 10}, {10, 10, 15}}),
    MEDIUM(new int[][] {{16, 16, 40}, {18, 18, 60}}),
    HARD(new int[][] {{30, 16, 99}, {32, 32, 150}});

    private final int[][] configurations;

    DifficultySettings(int[][] configurations) {
        this.configurations = configurations;
    }

    // Getter and Setter methods 
    
    public int[][] getConfigurations() {
        return configurations;
    }

    public int getWidth(int configIndex) {
        return configurations[configIndex][0];
    }

    public int getHeight(int configIndex) {
        return configurations[configIndex][1];
    }

    public int getMines(int configIndex) {
        return configurations[configIndex][2];

    }
}





