/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

/**
 * Represents the data access object for managing the moves in the Minesweeper game.
 * This class is responsible for saving and loading moves from the database.
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
public final class MoveDAO implements DAO{
     private   DataBaseManager dbManager = DataBaseManager.getInstance();

    public MoveDAO(DataBaseManager dbManager) {
        this.dbManager = dbManager;
        ensureTableExists();
    }
    
    /**
     * Ensures the MOVELOG table exists in the database.
     * If the table does not exist, it creates one.
     */
    @Override
    public void ensureTableExists() {
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
             
            // Check if the table already exists. If not, create it.
            ResultSet tables = conn.getMetaData().getTables(null, null, "MOVELOG", null);
            if (!tables.next()) {
                stmt.execute("CREATE TABLE MOVELOG ("
                                + "PLAYERNAME VARCHAR(255),"
                                + "GAMEID VARCHAR(255),"
                                + "X INT,"
                                + "Y INT,"
                                + "ACTION VARCHAR(255)"
                                + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error ensuring table exists:(move) " + e.getMessage());
        }
    }
    
    
    /**
     * Saves a list of moves to the MOVELOG table in the database.
     * 
     *  moves List of moves to be saved.
     */
    public void saveMoveLog(List<Move> moves) {
    String insertSQL = "INSERT INTO MOVELOG ( PLAYERNAME, GAMEID, X, Y, ACTION) VALUES ( ?, ?, ?, ?, ?)";

    try (Connection conn = dbManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
        for (Move move : moves) {
           
            pstmt.setString(1, move.getPlayerName());
            pstmt.setString(2, move.getGameId());
            pstmt.setInt(3, move.getx());
            pstmt.setInt(4, move.gety());
            pstmt.setString(5, move.getMoveType().toString());
            pstmt.executeUpdate();
        }
    } catch (SQLException e) {
        System.err.println("Error saving move logs: " + e.getMessage());
    }
    }
    /**
     * Loads all moves from the mOVELOG table in the database.
     * 
     * return List of moves loaded from the database.
     */
    public List<Move> loadMoveLog() {
    List<Move> moves = new ArrayList<>();
    String query = "SELECT * FROM MOVELOG";

    try (Connection conn = dbManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
                Move move = Move.fromData(rs.getString("PLAYERNAME"),rs.getString("GAMEID"),
                                       rs.getInt("X"),rs.getInt("Y"),rs.getString("ACTION")
            );
            moves.add(move);
        }
    } catch (SQLException e) {
        System.err.println("Error loading move logs: " + e.getMessage());
    }

    return moves;
}
}
    
    

