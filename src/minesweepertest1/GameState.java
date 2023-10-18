/*
 * 
    this method is used to convert the state of a game into a string format, each game also has a uniquie idenity attached to ii

 * @author Grisham Balloo 20099072
 */
package minesweepertest1;


import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
/**
 *
 * @author grish
 */
public class GameState {
    
    private String playerName;
    private String gameId;  // Unique identifier for the game
    private Board boardState;
    


    public GameState(String playerName, Board boardState) {
        
        this.playerName = playerName;
        this.boardState = boardState;
        this.gameId = UUID.randomUUID().toString(); // Generate a unique Game ID
    }

    // Serialization
    public String toString() {
        return playerName + ";" +gameId+";"+ boardState.toString();  // Custom format
    }

    // Deserialization
public static GameState fromString(String data) {
    StringTokenizer tokenizer = new StringTokenizer(data, ";");
    
    String playerName = tokenizer.nextToken();
    String gameId = tokenizer.nextToken();  // Extract gameId
    
    // Combine the remaining tokens to get the board data
    StringBuilder boardDataBuilder = new StringBuilder();
    while (tokenizer.hasMoreTokens()) {
        boardDataBuilder.append(tokenizer.nextToken());
        if (tokenizer.hasMoreTokens()) {
            boardDataBuilder.append(";");
        }
    }
    String boardData = boardDataBuilder.toString();
    
    GameState gameState = new GameState(playerName, Board.fromString(boardData));
    gameState.setGameId(gameId);  // Set the gameId for the created GameState object
    return gameState;
}

public static GameState fromData(String playerName, String gameId, String boardData) {
    GameState gameState = new GameState(playerName, Board.fromString(boardData));
    gameState.setGameId(gameId);
    return gameState;
}
    // Getters and Setters
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Board getBoardState() {
   
        return boardState;
    }

    public void setBoardState(Board boardState) {
        this.boardState = boardState;
    }
    public String getGameId() {
    return this.gameId;
}
    
    public void setGameId(String gameId) {
    this.gameId = gameId;
}
    
   
}