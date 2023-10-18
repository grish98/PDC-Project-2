/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

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

/**
 *
 * @author grish
 */
public final class GameStateDAO implements DAO {

    private DataBaseManager dbManager;

    public GameStateDAO(DataBaseManager dbManager) {
        this.dbManager = dbManager;
        ensureTableExists();
    }
    
   @Override
     public void ensureTableExists() {
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
             
            // Check if the table already exists. If not, create it.
            ResultSet tables = conn.getMetaData().getTables(null, null, "GAMESTATE", null);
            if (!tables.next()) {
                stmt.execute("CREATE TABLE GAMESTATE ("
                        + "PLAYERNAME VARCHAR(255) PRIMARY KEY,"
                        + "GAMEID VARCHAR(255)"
                        +"BOARD_STATE VARCHAR(4000)"
                        + ")");
            }
        } catch (Exception e) {
            System.err.println("Error ensuring table exists: " + e.getMessage());
        }
    }

   
   public void saveGameState(GameState gameState) {
        String insertSQL = "INSERT INTO GAMESTATE (GAME_ID, PLAYER_NAME, BOARD_STATE) VALUES (?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, gameState.getGameId());
            pstmt.setString(2, gameState.getPlayerName());
            pstmt.setString(3, gameState.getBoardState().toString());
           
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving GameState: " + e.getMessage());
        }
    }

    public Map<String, GameState> loadGameState(String playerName) {
        Map<String, GameState> gameStatesMap = new HashMap<>();
        String query = "SELECT * FROM GAMESTATE WHERE PLAYER_NAME = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                
                
                String name = rs.getString("PLAYER_NAME");
                String ID = rs.getString("GAME_ID");
                String board = rs.getString("BOARD_STATE");
                
                
              GameState gameState = GameState.fromData(name, ID, board);

               gameStatesMap.put(gameState.getGameId(), gameState);
            }
        } catch (SQLException e) {
            System.err.println("Error loading GameStates: " + e.getMessage());
        }

        return gameStatesMap;
    }
}
