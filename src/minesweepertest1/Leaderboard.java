//**
 //* Represents an entry in the Minesweeper leaderboard.
// * Contains player name, time taken to complete a game, and the difficulty of the game.
 
package minesweepertest1;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *  * @author Grisham Balloo 20099072
 */
public class Leaderboard {
    private String playerName;
    private String Difficulty;
    private Long Time;  // in seconds
    private Map<DifficultySettings, Long> leaderboard = new HashMap<>();
    // Constructors, getters, setters, and other methods

    Leaderboard(String playerName, String Difficulty, Long fastestTime) {
       this.playerName=playerName;
       this.Difficulty =Difficulty;
       this.Time=fastestTime;
    }

    String getPlayerName() {
        return playerName;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public Long getTime() {
        return Time;
    }
   
  
    
    public Map<DifficultySettings, Long> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Map<DifficultySettings, Long> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public void setRecordForDifficulty(DifficultySettings difficulty, long time) {
        leaderboard.put(difficulty, time);
    }

    public Long getRecordForDifficulty(DifficultySettings difficulty) {
        return leaderboard.get(difficulty);
    }
    
    @Override
    public String toString() {
    return this.playerName + "," + this.Difficulty + "," + this.Time;
}

public static Leaderboard fromString(String serializedLeaderboard) {
    String[] parts = serializedLeaderboard.split(",");
    if (parts.length == 3) {
        String playerName = parts[0];
        String difficulty = parts[1];
        long timeTaken = Long.parseLong(parts[2]);

        return new Leaderboard(playerName, difficulty, timeTaken);
    }
    return null;
}
}
