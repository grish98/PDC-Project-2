/*
 * 
 * Manages the game's core loop, user input, and interactions between various components like the UI and game logic.
 
 * @author Grisham Balloo 20099072

 */
package minesweepertest1;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.Timer;
/**
 *
 * @author grish
 */

public class GameController implements GameEventListener {

    private GameManager gameManager;
    private BoardGUI boardGUI;
    private MainMenuGUI mainMenu;
    
    public GameController() {
        this.gameManager = new GameManager();
        this.gameManager.setGameEventListener(this);
        this.mainMenu = new MainMenuGUI(this.gameManager,this);
        mainMenu.setVisible(true);
    }
    
    public void startNewGame() {
        
        boardGUI = gameManager.newGame(mainMenu);
        if (boardGUI == null) {
            JOptionPane.showMessageDialog(null, "Failed to initialize the game board.", "Error", JOptionPane.ERROR_MESSAGE);
           
        }
    }
    
     public void startLoadGame(String gameId) {
        
        boardGUI = gameManager.loadGame(gameId);
        if (boardGUI == null) {
            JOptionPane.showMessageDialog(null, "Failed to initialize the game board.", "Error", JOptionPane.ERROR_MESSAGE);
           
        }
    }
    
    @Override
    public void onGameEnd(String message) {
        System.out.println("onGameEnd called in GameController with message: " + message);
        if (boardGUI != null) {
            boardGUI.showGameOutcome(message);
        } else {
            System.out.println("boardGUI is null in GameController");
           
        }
    }
}

