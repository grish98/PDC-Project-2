/*
 * 
 * Manages the game's core loop, user input, and interactions between various components like the UI and game logic.
 
 * @author Grisham Balloo 20099072

 */
package minesweepertest1;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author grish
 */
public class GameController {

    private GameManager gameManager;
    private UserInterface userInterface;
   

    public GameController() {
        this.gameManager = new GameManager();
        this.userInterface = new UserInterface(gameManager);
    }
    
    // Starts the Minesweeper game, displaying the main menu and handling user choices.

   // public void startGame() {
       // gameManager.setupGame();
       // gameLoop();
       // gameManager.endGame();
   // }
//Runs the main game loop where players make moves and the game state is updated.
 public void gameLoop() {
    while (!gameManager.checkWin() && !gameManager.getGameOver()) {  // Continues as long as the game isn't won or lost
        gameManager.displayBoard();
        String action = userInterface.promptAction();
        userInterface.handleAction(action);
    }

    if (gameManager.checkWin()) {
        System.out.println("You win!");
        gameManager.displayBoardWithMines();
    } else if (gameManager.getGameOver()) {
        System.out.println("You Lose!");
        gameManager.displayBoardWithMines();
    }
}
    
   
    
}