/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

/**
 *
 * @author grish
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

    @Override
    public void ensureTableExists() {
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
             
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
            System.err.println("Error ensuring table exists:(profile) " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    public void savePlayerProfile(PlayerProfile profile) {
        if (profileExists(profile.getPlayerName())) {
            update(profile);
        } else {
            insert(profile);
        }
    }

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
    

