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
        
        String insertSQL = "INSERT INTO LEADERBOARD (PLAYERNAME, DIFFICULTY, TIME) VALUES (?, ?, ?)";
        String selectSQL = "SELECT * FROM LEADERBOARD WHERE DIFFICULTY = ? ORDER BY TIME ASC";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setString(1, newEntry.getDifficulty());
            ResultSet rs = pstmt.executeQuery();

            List<Leaderboard> sameDifficulty = new ArrayList<>();
            while (rs.next()) {
                Leaderboard entry = Leaderboard.fromData(
                    rs.getString("PLAYERNAME"),
                    rs.getString("DIFFICULTY"),
                    rs.getLong("TIME")
                );
                sameDifficulty.add(entry);
            }
            
            if (sameDifficulty.size() < 3) {
                sameDifficulty.add(newEntry);
            } else {
                if (newEntry.getTime() < sameDifficulty.get(2).getTime()) {
                    sameDifficulty.remove(2);
                    sameDifficulty.add(newEntry);
                }
            }

            sameDifficulty.sort(Comparator.comparingLong(Leaderboard::getTime));

            for (Leaderboard entry : sameDifficulty) {
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSQL)) {
                    insertPstmt.setString(1, entry.getPlayerName());
                    insertPstmt.setString(2, entry.getDifficulty());
                    insertPstmt.setLong(3, entry.getTime());
                    insertPstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving to leaderboard: " + e.getMessage());
        }
    }

    public static List<Leaderboard> loadLeaderboard() {
        List<Leaderboard> leaderboard = new ArrayList<>();
        String query = "SELECT * FROM LEADERBOARD";

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

