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

import java.util.Arrays;
import java.util.List;

public class DataBaseInitilization {
    private DataBaseManager dbManager;
    private List<DAO> daoList;

    public DataBaseInitilization() {
        this.dbManager = DataBaseManager.getInstance();
        daoList = Arrays.asList(
            new PlayerProfileDAO(dbManager),
            new GameStateDAO(dbManager),
            new LeaderBoardDAO(dbManager),
            new MoveDAO(dbManager));
            
}
    
    
    public void initializeDatabase() {
        for (DAO dao : daoList) {
            dao.ensureTableExists();
        }
    }
}
