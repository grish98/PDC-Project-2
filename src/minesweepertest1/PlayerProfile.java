/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

/**
 *
 *  * @author Grisham Balloo 20099072
 */
import java.util.Objects;


public class PlayerProfile {
    private String playerName;
  

    private int wins;
    private int losses;
    private int points;

    public PlayerProfile(String playerName) {
        this.playerName = playerName;
        
        this.wins = 0;
        this.losses = 0;
        this.points = 0;
    }

   
    @Override
    public String toString() {
        return playerName + "," + wins + "," + losses + "," + points;  // Custom format
    }

  public static PlayerProfile fromString(String data) {
    String[] parts = data.split(",");
    
    if (parts.length < 4) {
        throw new IllegalArgumentException("Invalid serialized data format for PlayerProfile: " + data);
    }

    String playerName = parts[0];
    
    PlayerProfile profile = new PlayerProfile(playerName);
    profile.wins = Integer.parseInt(parts[parts.length - 3]);
    profile.losses = Integer.parseInt(parts[parts.length - 2]);
    profile.points = Integer.parseInt(parts[parts.length - 1]);

    return profile;
}
  
  public static PlayerProfile FromData(String playerName, int wins, int losses,int points ) {
        PlayerProfile profile = new PlayerProfile(playerName);
        profile.wins = wins;
        profile.losses = losses;
        profile.points = points;

        return profile;
    }
   
    @Override
    public int hashCode() {
        return Objects.hash(playerName);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayerProfile other = (PlayerProfile) obj;
        return Objects.equals(playerName, other.playerName);
    }
    
    
    public int getWins() {
        return wins;
    }

    public void incrementWins() {
        this.wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void incrementLosses() {
        this.losses++;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public String getPlayerName() {
        return  playerName;
    } 
    
}
