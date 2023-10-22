/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.Map;

public class GameStateDAOTest {
    
    private static GameStateDAO dao;
    private static DataBaseManager dbManager;
    
    @Before
    public void setUp() {
        dbManager = DataBaseManager.getInstance();
        dao = new GameStateDAO(dbManager);
    }
    
    @After
    public void tearDown() {
        dbManager.closeConnections();
    }

    @Test
    public void testEnsureTableExists() throws Exception {
        // Drop the GAMESTATE table if it exists
        try (Connection conn = DataBaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE GAMESTATE");
        } catch (Exception e) {
            
        }

       
        dao.ensureTableExists();

        // Check that table exists
        boolean tableExists = false;
        try (Connection conn = DataBaseManager.getInstance().getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "GAMESTATE", null);
            tableExists = tables.next();
        }

        assertTrue(tableExists);
    }

    @Test
    public void testSaveAndLoadGameState() {
        Board testBoard = new Board(5, 5, 5);
        GameState gameState = new GameState("testPlayer", testBoard);
        dao.saveGameState(gameState);

        // Now, load the game state
        Map<String, GameState> loadedGameStates = dao.loadGameState("testPlayer");
        assertNotNull(loadedGameStates);
        assertFalse(loadedGameStates.isEmpty());

        // Verify that the saved and loaded game states match
        GameState loadedGameState = loadedGameStates.get(gameState.getGameId());
        assertNotNull(loadedGameState);
        assertEquals(gameState.getGameId(), loadedGameState.getGameId());
        assertEquals(gameState.getPlayerName(), loadedGameState.getPlayerName());
        
    }
    
    
    public void testSaveGameStateWithDuplicate() {
        Board testBoard = new Board(5, 5, 5);
        GameState gameState = new GameState("duplicatePlayer", testBoard);
        dao.saveGameState(gameState);

        // Attempt to save the same game state again
        try {
            dao.saveGameState(gameState);
            fail("Expected an exception due to duplicate entry");
        } catch (Exception e) {
            // Expected an exception here due to the unique constraint of the table
        }
    }

    @Test
    public void testLoadGameStateWithNonExistentPlayer() {
        Map<String, GameState> loadedGameStates = dao.loadGameState("nonExistentPlayer");
        assertNotNull(loadedGameStates);
        assertTrue(loadedGameStates.isEmpty());
    }

    @Test
    public void testLoadGameStateWithMultipleEntries() {
        Board testBoard1 = new Board(5, 5, 5);
        GameState gameState1 = new GameState("Player2", testBoard1);
        dao.saveGameState(gameState1);

        Board testBoard2 = new Board(5, 5, 5);
        GameState gameState2 = new GameState("Player2", testBoard2);
        dao.saveGameState(gameState2);

        Map<String, GameState> loadedGameStates = dao.loadGameState("Player2");
        assertNotNull(loadedGameStates);
        assertEquals(2, loadedGameStates.size());

        assertTrue(loadedGameStates.containsKey(gameState1.getGameId()));
        assertTrue(loadedGameStates.containsKey(gameState2.getGameId()));
    }
}

    

