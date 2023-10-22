/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author grish
 */
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

public class LeaderBoardDAOTEST {
    
    private static LeaderBoardDAO dao;
    private static DataBaseManager dbManager;
    
    @Before
    public void setUp() {
        dbManager = DataBaseManager.getInstance();
        dao = new LeaderBoardDAO(dbManager);
    }
    
    @After
    public void tearDown() {
        dbManager.closeConnections();
    }

    @Test
    public void testEnsureTableExists() throws Exception {
        // Drop the LEADERBOARD table if it exists
        try (Connection conn = DataBaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE LEADERBOARD");
        } catch (Exception e) {
            // Ignore exception in case table doesn't exist
        }

        // Now, ensure table
        dao.ensureTableExists();

        // Check that table exists
        boolean tableExists = false;
        try (Connection conn = DataBaseManager.getInstance().getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "LEADERBOARD", null);
            tableExists = tables.next();
        }

        assertTrue(tableExists);
    }

    @Test
    public void testLoadLeaderboard() {
        List<Leaderboard> leaderboard = dao.loadLeaderboard();
        assertNotNull(leaderboard);
    }

    @Test
    public void testLoadLeaderboardWhenEmpty() {
        // Ensure table is empty
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM LEADERBOARD");
        } catch (Exception e) {
            fail("Failed to clear the LEADERBOARD table.");
        }

        List<Leaderboard> leaderboard = dao.loadLeaderboard();
        assertNotNull(leaderboard);
        assertTrue(leaderboard.isEmpty());
    }

    @Test
    public void testSaveToLeaderboard() {
        Leaderboard newEntry = new Leaderboard("testPlayer", "easy", 1000L);
        dao.saveToLeaderboard(newEntry);

        // Now, load the leaderboard
        List<Leaderboard> leaderboard = dao.loadLeaderboard();
        assertNotNull(leaderboard);
        assertTrue(containsLeaderboardEntry(leaderboard, newEntry));
    }
    @Test
public void testOnlyTopThreeTimesAddedToLeaderboard() {
    // Create and save four leaderboard entries
    Leaderboard entry1 = new Leaderboard("testPlayer1", "easy", 1000L); // 1 second
    Leaderboard entry2 = new Leaderboard("testPlayer2", "easy", 2000L); // 2 seconds
    Leaderboard entry3 = new Leaderboard("testPlayer3", "easy", 3000L); // 3 seconds
    Leaderboard entry4 = new Leaderboard("testPlayer4", "easy", 4000L); // 4 seconds

    dao.saveToLeaderboard(entry1);
    dao.saveToLeaderboard(entry2);
    dao.saveToLeaderboard(entry3);
    dao.saveToLeaderboard(entry4);

    List<Leaderboard> leaderboard = dao.loadLeaderboard();

    // Only the top 3 times should be present
    assertTrue(leaderboard.size() <= 3);

    // The 4-second entry (entry4) should not be in the leaderboard
    assertFalse(containsLeaderboardEntry(leaderboard, entry4));
}

@Test
public void testLeaderboardEntriesSortedByTime() {
    // Create and save three leaderboard entries in a random order
    Leaderboard entry2 = new Leaderboard("testPlayer_2", "easy", 2000L); // 2 seconds
    Leaderboard entry1 = new Leaderboard("testPlayer_1", "easy", 1000L); // 1 second
    Leaderboard entry3 = new Leaderboard("testPlayer_3", "easy", 3000L); // 3 seconds

    dao.saveToLeaderboard(entry2);
    dao.saveToLeaderboard(entry1);
    dao.saveToLeaderboard(entry3);

    List<Leaderboard> leaderboard = dao.loadLeaderboard();

    // Ensure leaderboard entries are sorted by time, from fastest to slowest
    for (int i = 0; i < leaderboard.size() - 1; i++) {
        assertTrue(leaderboard.get(i).getTime() <= leaderboard.get(i + 1).getTime());
    }
}

    private boolean containsLeaderboardEntry(List<Leaderboard> leaderboard, Leaderboard entry) {
        for (Leaderboard e : leaderboard) {
            if (e.getPlayerName().equals(entry.getPlayerName()) &&
                e.getDifficulty().equals(entry.getDifficulty()) &&
                e.getTime().equals(entry.getTime())) {
                return true;
            }
        }
        return false;
    }
}
