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
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class LeaderBoardDAO implements DAO{
  private  static DataBaseManager dbManager = DataBaseManager.getInstance();

    public LeaderBoardDAO(DataBaseManager dbManager) {
        this.dbManager = dbManager;
        ensureTableExists();
    }

    @Override
    public void ensureTableExists() {
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
             
            ResultSet tables = conn.getMetaData().getTables(null, null, "LEADERBOARD", null);
            if (!tables.next()) {
                stmt.execute("CREATE TABLE LEADERBOARD ("
                        + "PLAYERNAME VARCHAR(255),"
                        + "DIFFICULTY VARCHAR(255),"
                        + "TIME BIGINT,"
                        + "PRIMARY KEY (PLAYERNAME, DIFFICULTY)"
                        + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error ensuring table exists:)leaderboard " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveToLeaderboard(Leaderboard newEntry) {
      if (newEntry == null) {
        // will not save if entry is null, null entry will be caused from a loaded game, loaded games will not count towards the leadeer board only new games
        
        return;
    }  
        
    String insertSQL = "INSERT INTO LEADERBOARD (PLAYERNAME, DIFFICULTY, TIME) VALUES (?, ?, ?)";
    String selectSQL = "SELECT * FROM LEADERBOARD WHERE DIFFICULTY = ? ORDER BY TIME ASC";
    String deleteSQL = "DELETE FROM LEADERBOARD WHERE PLAYERNAME = ? AND DIFFICULTY = ? AND TIME = ?";

    try (Connection conn = dbManager.getConnection()) {

        // Insert the new entry
        try (PreparedStatement insertPstmt = conn.prepareStatement(insertSQL)) {
            insertPstmt.setString(1, newEntry.getPlayerName());
            insertPstmt.setString(2, newEntry.getDifficulty());
            insertPstmt.setLong(3, newEntry.getTime());
            insertPstmt.executeUpdate();
        }

        // Check the leaderboard for this difficulty
        List<Leaderboard> timesForDifficulty = new ArrayList<>();
        try (PreparedStatement selectPstmt = conn.prepareStatement(selectSQL)) {
            selectPstmt.setString(1, newEntry.getDifficulty());
            ResultSet rs = selectPstmt.executeQuery();
            while (rs.next()) {
                Leaderboard entry = Leaderboard.fromData(
                    rs.getString("PLAYERNAME"),
                    rs.getString("DIFFICULTY"),
                    rs.getLong("TIME")
                );
                timesForDifficulty.add(entry);
            }
        }

        // If there are more than 3 times, remove the slowest one(s)
        while (timesForDifficulty.size() > 3) {
            Leaderboard slowest = timesForDifficulty.remove(timesForDifficulty.size() - 1);
            try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSQL)) {
                deletePstmt.setString(1, slowest.getPlayerName());
                deletePstmt.setString(2, slowest.getDifficulty());
                deletePstmt.setLong(3, slowest.getTime());
                deletePstmt.executeUpdate();
            }
        }

    } catch (SQLException e) {
        System.err.println("Error saving to leaderboard: " + e.getMessage());
    }
}

    public static List<Leaderboard> loadLeaderboard() {
        List<Leaderboard> leaderboard = new ArrayList<>();
        String query = "SELECT * FROM LEADERBOARD ORDER BY TIME ASC"; // Adding an ORDER BY clause

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Leaderboard entry = Leaderboard.fromData(
                    rs.getString("PLAYERNAME"),
                    rs.getString("DIFFICULTY"),
                    rs.getLong("TIME")
                );
                leaderboard.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
        }

        return leaderboard;
    }
}

