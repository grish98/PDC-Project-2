/**
 * Manages the database connection for the Minesweeper game.
 * This class follows the Singleton pattern to ensure only one instance of the database connection exists.
 */
package minesweepertest1;

/**
 *
 * @author Grisham Balloo 20099072
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    
  
public class DataBaseManager {
    private static final String USER_NAME = "grisham";
    private static final String PASSWORD = "grisham";
    private static final String URL = "jdbc:derby:MineSweeperEDB;create=true";
    //for testing purposes:
    //private static final String URL = "jdbc:derby:MineSweeperEDB_Test;create=true"; 
    
    private Connection conn; // The database connection object

    // The single instance of DataBaseManager (Singleton pattern)
    private static DataBaseManager instance;

   /**
     * Private constructor to establish the database connection 
     */
    private DataBaseManager()  {
        establishConnection();
    }

    /**
     * If no instance exists, create one.
     * return The single instance of DataBaseManager.
     */
    public static synchronized DataBaseManager getInstance() {
    if (instance == null) {
        instance = new DataBaseManager();
    }
    return instance;
}

    
    /**
     * Tries to establish a new database connection. If a connection already exists, it returns the existing connection.
     * return The established connection, or null if the connection cannot be established.
     */
     public Connection getConnection() {
    try {
        System.out.println("Establishing a new database connection");
        conn = DriverManager.getConnection(URL);
    } catch (SQLException e) {
        if ("XSDB6".equals(e.getSQLState())) {
            System.out.println("The application is already running. Please close any other instances and try again.");
            System.exit(1);
        } else {
            System.err.println("Connection error: " + e.getMessage());
        }
        return null;  // Return null if connection cannot be established
    }
    return conn;
}

        private void establishConnection() {
    if (this.conn == null) {
        try {
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println(URL + " connected");
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 40000 && "XSDB6".equals(ex.getSQLState())) {
                System.out.println("The application is already running. Please close any other instances and try again.");
                System.exit(1);  // Exit the application gracefully
            } else {
                System.err.println("SQLException: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}

      //Closes the established database connections.
     
    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();  // Print full stack trace
            }
        }
    }
   //Overrides the clone method to prevent cloning of the database manager instance.
     @Override
        public Object clone() throws
            CloneNotSupportedException {
            throw new CloneNotSupportedException();
        }
}

