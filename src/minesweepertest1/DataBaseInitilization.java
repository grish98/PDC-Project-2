/**
 * Responsible for initializing the database by ensuring that required tables exist.
 * Uses various DAO (Data Access Object) instances to manage different parts of the database.
 */
package minesweepertest1;

/**
 *
 * @author Grisham Balloo 20099072
 */

import java.util.Arrays;
import java.util.List;

public class DataBaseInitilization {
    private DataBaseManager dbManager; // The database manager instance to interact with the database.
    private List<DAO> daoList; // List of DAOs used to manage different tables in the database.

    /**
     * Constructor for DataBaseInitialization.
     * Initializes the dbManager and the list of DAOs.
     */

    public DataBaseInitilization() {
        this.dbManager = DataBaseManager.getInstance();
        daoList = Arrays.asList(
            new PlayerProfileDAO(dbManager),
            new GameStateDAO(dbManager),
            new LeaderBoardDAO(dbManager),
            new MoveDAO(dbManager));
            
}
    
    /**
     * Initializes the database by ensuring that all required tables  exist.
     * If a table does not exist, create it.
     */
    public void initializeDatabase() {
        for (DAO dao : daoList) {
            dao.ensureTableExists();
        }
    }
}
