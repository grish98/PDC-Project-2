/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

/**
 * Represents the data access object for managing the player profiles in the Minesweeper game.
 * This class is responsible for saving, loading, updating, and checking existence of player profiles in the database.
 * 
 * @author Grisham Balloo 20099072
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public final class PlayerProfileDAO implements DAO{
    
   private  DataBaseManager dbManager = DataBaseManager.getInstance();

    PlayerProfileDAO(DataBaseManager dbManager) {
        this.dbManager = dbManager;
        ensureTableExists();
    }

    /**
     * Ensures the PLAYERPROFILE table exists in the database.
     * If the table does not exist, it creates one.
     */
    @Override
    public void ensureTableExists() {
        
   Connection conn = dbManager.getConnection();
    if (conn == null) {
        System.out.println("Cannot establish a connection to the database. Exiting application.");
        System.exit(1);  // Exit 
    }
    
    try (Statement stmt = conn.createStatement()) {
        // Check if the table already exists. If not, create it.
        ResultSet tables = conn.getMetaData().getTables(null, null, "PLAYERPROFILE", null);
        if (!tables.next()) {
            stmt.execute("CREATE TABLE PLAYERPROFILE ("
                    + "PLAYERNAME VARCHAR(255) PRIMARY KEY,"
                    + "WINS INT,"
                    + "LOSSES INT,"
                    + "POINTS INT"
                    + ")");
        }
    } catch (SQLException e) {
        System.out.println("Error ensuring table exists:(profile) " + e.getMessage());
    }
    }

    /**
     * Loads all player profiles from the PLAYERPROFILE table in the database.
     * 
     * return List of player profiles loaded from the database.
     */
   public List<PlayerProfile> loadAllPlayerProfiles() {
        List<PlayerProfile> profiles = new ArrayList<>();
        String query = "SELECT * FROM PLAYERPROFILE";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                
                String name = rs.getString("PLAYERNAME");
                int wins = rs.getInt("WINS");
                int loss = rs.getInt("LOSSES");
                int points = rs.getInt("POINTS");
                PlayerProfile profile = PlayerProfile.FromData(name, wins, loss, points);
                profiles.add(profile);
            }
        } catch (Exception e) {
            System.err.println("Error loading all player profiles: " + e.getMessage());
        }
        return profiles;
    }

    /**
     * Loads a specific player's profile from the PLAYERPROFILE table in the database.
     * 
     * 
     * return The player's profile.
     */
    public PlayerProfile loadPlayerProfile(String playerName) {
        PlayerProfile profile = null;
        String query = "SELECT * FROM PLAYERPROFILE WHERE PLAYERNAME = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("PLAYERNAME");
                int wins = rs.getInt("WINS");
                int loss = rs.getInt("LOSSES");
                int points = rs.getInt("POINTS");
                
               profile = PlayerProfile.FromData(name, wins, loss, points);
            }
        } catch (Exception e) {
            System.err.println("Error loading player profile: " + e.getMessage());
        }

        return profile;
    }

    /**
     * Saves a player's profile to the PLAYERPROFILE table in the database.
     * If the profile already exists, it updates the record; otherwise, it inserts a new record.
     * 
     * 
     */
    public void savePlayerProfile(PlayerProfile profile) {
        if (profileExists(profile.getPlayerName())) {
            update(profile);
        } else {
            insert(profile);
        }
    }

    /**
     * Checks if a player's profile exists in the PLAYERPROFILE table in the database.
     * 
     *.
     */
    private boolean profileExists(String playerName) {
        boolean exists = false;
        String query = "SELECT 1 FROM PLAYERPROFILE WHERE PLAYERNAME = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = true;
            }
        } catch (Exception e) {
            System.err.println("Error checking if profile exists: " + e.getMessage());
        }

        return exists;
    }

    /**
     * Inserts a new player's profile into the PLAYERPROFILE table in the database.
     * 
     * 
     */
    private void insert(PlayerProfile profile) {
        String query = "INSERT INTO PLAYERPROFILE (PLAYERNAME, WINS, LOSSES, POINTS) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
      
            pstmt.setString(1, profile.getPlayerName());
            pstmt.setInt(2, profile.getWins());
            pstmt.setInt(3, profile.getLosses());
            pstmt.setInt(4, profile.getPoints());
            
           

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error inserting player profile: " + e.getMessage());
        }
    }

    /**
     * Updates an existing player's profile in the PLAYERPROFILE table in the database.
     * 
     * 
     */
    private void update(PlayerProfile profile) {
        String query = "UPDATE PLAYERPROFILE SET WINS = ?, LOSSES = ?, POINTS = ? WHERE PLAYERNAME = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, profile.getWins());
            pstmt.setInt(2, profile.getLosses());
            pstmt.setInt(3, profile.getPoints());
             pstmt.setString(4, profile.getPlayerName());

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error updating player profile: " + e.getMessage());
        }
    }
}
    

