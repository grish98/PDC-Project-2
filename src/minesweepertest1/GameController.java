/**
 * Manages the game's core loop, user input, and interactions between various components
 * like the UI and game logic.
 * Implements the GameEventListener interface to react to game events.
 * */
package minesweepertest1;


import javax.swing.JOptionPane;

/**
 *
 * @author Grisham Balloo 20099072
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
    
    /**
     * Initiates a new game. If the game fails to initialize, an error message is displayed.
     */
    public void startNewGame() {
        
        boardGUI = gameManager.newGame(mainMenu);
        if (boardGUI == null) {
            JOptionPane.showMessageDialog(null, "Failed to initialize the game board.", "Error", JOptionPane.ERROR_MESSAGE);
           
        }
    }
    
    /**
     * Loads an existing game based on the provided game ID.
     * If the game fails to load, an error message is displayed.
     */
     public void startLoadGame(String gameId) {
        
        boardGUI = gameManager.loadGame(gameId);
        if (boardGUI == null) {
            JOptionPane.showMessageDialog(null, "Failed to initialize the game board.", "Error", JOptionPane.ERROR_MESSAGE);
           
        }
    }
    
     /**
     * Handles the end of the game. Displays the game outcome on the board GUI.
     *
     * 
     */
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

