/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

import java.sql.Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;

/**
 *
 * @author grish
 */
public class DataBaseManagerTEST {
     private static DataBaseManager dbManager;
    public DataBaseManagerTEST() {
    }
    
   
    @Before
    public void setUp() {
        dbManager = DataBaseManager.getInstance();
    }
    
    @After
    public void tearDown() {
        dbManager.closeConnections();
    }

    /**
     * Test of getInstance method, of class DataBaseManager.
     */
    @Test
    public void testGetInstance() {
        DataBaseManager firstInstance = DataBaseManager.getInstance();
        DataBaseManager secondInstance = DataBaseManager.getInstance();
        assertSame(firstInstance, secondInstance); // Ensure both instances are same
        
    }

    /**
     * Test of getConnection method, of class DataBaseManager.
     */
    @Test
    public void testGetConnection() {
        Connection conn = dbManager.getConnection();
        assertNotNull(conn); // Ensure connection is not null
        
    }

    /**
     * Test of closeConnections method, of class DataBaseManager.
     */
    @Test
public void testCloseConnections() {
    Connection conn = dbManager.getConnection();
    assertNotNull(conn); // Ensure connection is not null before closing

    dbManager.closeConnections(); // Close the connection

    try {
        assertTrue(conn.isClosed()); // Check if connection is closed
    } catch (SQLException e) {
        fail("Failed to check if connection is closed: " + e.getMessage());
    }
}

    /**
     * Test of clone method, of class DataBaseManager.
     */
    
   @Test(expected = CloneNotSupportedException.class)
    public void testClone() throws CloneNotSupportedException {
        dbManager.clone(); // This should throw a CloneNotSupportedException
    }
    
}
