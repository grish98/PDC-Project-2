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
import java.sql.DriverManager;
import java.sql.SQLException;

    
  
public class DataBaseManager {
    private static final String USER_NAME = "grisham";
    private static final String PASSWORD = "grisham";
    private static final String URL = "jdbc:derby://localhost:1527/MineSweeperEDB; create=true";
    
    private Connection conn;

    
    private static DataBaseManager instance;

   
    private DataBaseManager() {
        establishConnection();
    }

    
    public synchronized static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return this.conn;
    }

    private void establishConnection() {
        try {
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println(URL + " connected");
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

     @Override
        public Object clone() throws
            CloneNotSupportedException {
            throw new CloneNotSupportedException();
        }
}

