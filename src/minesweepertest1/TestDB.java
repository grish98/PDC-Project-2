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

public class TestDB{
   public static void main(String[] args) {
    String dbURL = "jdbc:derby:C:\\Users\\grish\\OneDrive\\Documents\\NetBeansProjects\\MinesweeperTest1\\MineSweeperEDB;create=true";
    try (Connection conn = DriverManager.getConnection(dbURL)) {
        if (conn != null) {
            System.out.println("Connected to database!");
        }
    } catch (SQLException e) {
        System.err.println("Connection error: " + e.getMessage());
    }
}
}
