/*
 * Represents a player's move in the Minesweeper game.
 * Contains details about the type of move (reveal, flag, ), and the coordinates of the cell affected, player name and the games uniqui id
@author Grisham Balloo 20099072
 */ 

package minesweepertest1;

public class Move {
    private String playerName;
    private String gameId;
    private int x;
    private int y;
    private final MoveType moveType;
  
    public enum MoveType {
        REVEAL, FLAG_TOGGLE;
    }

    public Move(String playerName, String gameId, int x,int y, MoveType action) {
        this.playerName = playerName;
        this.gameId = gameId;
        this.x = x;
        this.y = y;
        this.moveType = action;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getGameId() {
        return gameId;
    }

    public int getx() {
        return x;
    }
public int gety() {
        return y;
    }
    public MoveType getMoveType() {
        return moveType;
    }

    public static Move fromString(String serializedData) {
        String[] tokens = serializedData.split(";");
        if (tokens.length != 5) {
            throw new IllegalArgumentException("Malformed serialized move data");
        }
        String playerName = tokens[0];
        String gameId = tokens[1];
        int x = Integer. parseInt(tokens[2]);
        int y = Integer. parseInt(tokens[3]);
        MoveType action = MoveType.valueOf(tokens[4]);
        return new Move(playerName, gameId, x,y, action);
    }

    
    public static Move fromData(String playerName, String gameId, int x, int y, String action) {
        MoveType moveType = MoveType.valueOf(action);
        return new Move(playerName, gameId, x, y, moveType);
    }
    
    @Override
    public String toString() {
        return playerName + ";" + gameId + ";" + x+";" +y+ ";" + moveType.name();
    }
}