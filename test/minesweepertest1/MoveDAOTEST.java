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
import java.util.ArrayList;
import java.util.List;

public class MoveDAOTEST {
    
    private static MoveDAO dao;
    private static DataBaseManager dbManager;
    
    @Before
    public void setUp() {
        dbManager = DataBaseManager.getInstance();
        dao = new MoveDAO(dbManager);
    }
    
    @After
    public void tearDown() {
        dbManager.closeConnections();
    }

    @Test
    public void testEnsureTableExists() throws Exception {
        // Drop the MOVES table if it exists
        try (Connection conn = DataBaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE MOVELOG");
        } catch (Exception e) {
            // Ignore exception in case table doesn't exist
        }

        // Now, ensure table
        dao.ensureTableExists();

        // Check that table exists
        boolean tableExists = false;
        try (Connection conn = DataBaseManager.getInstance().getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "MOVELOG", null);
            tableExists = tables.next();
        }

        assertTrue(tableExists);
    }

    @Test
    public void testLoadMoveLog() {
        List<Move> moves = dao.loadMoveLog();
        assertNotNull(moves);
    }

   @Test
public void testLoadMoveLogWithLargeData() {
    final int NUM_MOVES = 100;
    List<Move> movesToSave = new ArrayList<>();

    // Insert NUM_MOVES moves
    for (int i = 0; i < NUM_MOVES; i++) {
        Move move = new Move("testPlayer", "gameId" + i, i, i, Move.MoveType.REVEAL);
        movesToSave.add(move);
    }
    
    dao.saveMoveLog(movesToSave);

    List<Move> loadedMoves = dao.loadMoveLog();
    assertNotNull(loadedMoves);
    assertEquals(NUM_MOVES, loadedMoves.size());
}
@Test
    public void testLoadMoveLogWhenEmpty() {
        // Ensure table is empty
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM MOVELOG");
        } catch (Exception e) {
            fail("Failed to clear the MOVES table.");
        }

        List<Move> moves = dao.loadMoveLog();
        assertNotNull(moves);
        assertTrue(moves.isEmpty());
    }

    @Test
public void testSaveMoveLog() {
    Move move = new Move("testPlayer", "testGameId", 1, 2, Move.MoveType.FLAG_TOGGLE);
    
    List<Move> movesToSave = new ArrayList<>();
    movesToSave.add(move);
    
    dao.saveMoveLog(movesToSave);

    // Now, load the move
    List<Move> loadedMoves = dao.loadMoveLog();
    assertNotNull(loadedMoves);
    assertTrue(containsMove(loadedMoves, move));
}

    private boolean containsMove(List<Move> moves, Move move) {
        for (Move m : moves) {
            if (m.getPlayerName().equals(move.getPlayerName()) &&
                m.getGameId().equals(move.getGameId()) &&
                m.getx() == move.getx() &&
                m.gety() == move.gety() &&
                m.getMoveType() == move.getMoveType()) {
                return true;
            }
        }
        return false;
    }
}
