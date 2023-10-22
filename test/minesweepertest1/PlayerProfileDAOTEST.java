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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.DatabaseMetaData;
/**
 *
 * @author grish
 */
public class PlayerProfileDAOTEST {
    
   private static PlayerProfileDAO dao;
    private static DataBaseManager dbManager;
    
  
    
    @Before
    public void setUp() {
        
        dbManager = DataBaseManager.getInstance();
         dao = new PlayerProfileDAO(dbManager);
    }
    
    @After
    public void tearDown() {
        
         dbManager.closeConnections();
    }

    /**
     * Test of ensureTableExists method, of class PlayerProfileDAO.
     */
    @Test
   
    public void testEnsureTableExists() throws Exception {
        // Drop the PLAYERPROFILE table if it exists
        try (Connection conn = DataBaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE PLAYERPROFILE");
        } catch (Exception e) {
            // Ignore exception in case table doesn't exist
        }

        // Now, ensure table
        dao.ensureTableExists();

        // Check that table exists
        boolean tableExists = false;
        try (Connection conn = DataBaseManager.getInstance().getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "PLAYERPROFILE", null);
            tableExists = tables.next();
        }

        assertTrue(tableExists);

    }

    /**
     * Test of loadAllPlayerProfiles method, of class PlayerProfileDAO.
     */
    @Test
    public void testLoadAllPlayerProfiles() {
        List<PlayerProfile> profiles = dao.loadAllPlayerProfiles();
        assertNotNull(profiles);
    }

    
    @Test
public void testLoadAllPlayerProfilesWhenEmpty() {
    // Ensure table is empty
    try (Connection conn = dbManager.getConnection();
         Statement stmt = conn.createStatement()) {
        stmt.execute("DELETE FROM PLAYERPROFILE");
    } catch (Exception e) {
        fail("Failed to clear the PLAYERPROFILE table.");
    }

    List<PlayerProfile> profiles = dao.loadAllPlayerProfiles();
    assertNotNull(profiles);
    assertTrue(profiles.isEmpty());
}

@Test
public void testLoadAllPlayerProfilesWithLargeData() {
    final int NUM_PROFILES = 1000;

    // Insert NUM_PROFILES profiles
    for (int i = 0; i < NUM_PROFILES; i++) {
        PlayerProfile profile = new PlayerProfile("testPlayer" + i);
        dao.savePlayerProfile(profile);
    }

    List<PlayerProfile> profiles = dao.loadAllPlayerProfiles();
    assertNotNull(profiles);
    assertEquals(NUM_PROFILES, profiles.size());
}
    
    /**
     * Test of loadPlayerProfile method, of class PlayerProfileDAO.
     */
    @Test
    public void testLoadPlayerProfile() {
 // Insert a test profile first
        PlayerProfile profileToInsert = new PlayerProfile("testPlayer");
        dao.savePlayerProfile(profileToInsert);

        // Now, load the profile
        PlayerProfile loadedProfile = dao.loadPlayerProfile("testPlayer");
        assertNotNull(loadedProfile);
        assertEquals("testPlayer", loadedProfile.getPlayerName());
    }

    /**
     * Test of savePlayerProfile method, of class PlayerProfileDAO.
     */
    @Test
    public void testSavePlayerProfile() {
         PlayerProfile profile = new PlayerProfile("testPlayer2");
        dao.savePlayerProfile(profile);

        PlayerProfile loadedProfile = dao.loadPlayerProfile("testPlayer2");
        assertNotNull(loadedProfile);
        assertEquals("testPlayer2", loadedProfile.getPlayerName());
    }
    
    
    
    
}
