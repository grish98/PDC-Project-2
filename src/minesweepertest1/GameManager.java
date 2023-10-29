/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @author Grisham Balloo 20099072


 */
package minesweepertest1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;


public class GameManager {

    private static Board board; 
    private static Timer timer;
    private static GameState currentGameState;
    private static PlayerProfile profile;
   
    
    static DifficultySettings difficulty ;
    private static final Scanner scanner = new Scanner(System.in);
    private static List<Move> moveLog = new ArrayList<>();
    
    private static final DataBaseManager dbManager = DataBaseManager.getInstance();
    private static final PlayerProfileDAO playerProfileDAO = new PlayerProfileDAO(dbManager);
    private final GameStateDAO gameStateDAO = new GameStateDAO(dbManager);
    private final MoveDAO moveDAO = new MoveDAO(dbManager);
    private final LeaderBoardDAO leaderboardDAO = new LeaderBoardDAO(dbManager);
    private boolean GameinProcess = false;
    private GameEventListener gameEventListener;

    //check win conditions
  public  boolean checkWin() 
  {
      return (board.allNonMineCellsRevealed() || board.allMinesFlagged());    
  }
  
public List<GameState> getSavedGames() {
        Map<String, GameState> savedGamesMap = gameStateDAO.loadGameState(profile.getPlayerName());
        return new ArrayList<>(savedGamesMap.values());
    }

    public GameState getSpecificGameState(String gameId) {
        List<GameState> savedGames = getSavedGames();
        return savedGames.stream()
            .filter(game -> game.getGameId().equals(gameId))
            .findFirst()
            .orElse(null);
    }

    public BoardGUI loadGame(String gameId) {
    GameState gameState = getSpecificGameState(gameId);
    if(gameState != null) {
    System.out.println("Retrieved Board State: " + gameState.getBoardState());
    }
    if(gameState != null) {
        GameinProcess = true;
        currentGameState = gameState;
        
        // Create a new Board object from the loaded GameState
        board = gameState.getBoardState(); 
        
        // Load and execute the saved moves
        loadAndExecuteMoveLog(currentGameState.getGameId());
        
        // Start the timer for the loaded game
        timer = new Timer();
        timer.start();
        
          BoardGUI boardGUI = new BoardGUI(this);  // Pass the GameManager instance to the BoardGUI
        boardGUI.setVisible(true);
        
        
        
        return boardGUI;  // Return the created BoardGUI object
    } else {
         
        return null; 
    }
}
    
    public boolean hasSavedGames() {
        return !getSavedGames().isEmpty();
    }
   //loads all saved moves from certain game and executed them to continue from saved state
    private  void loadAndExecuteMoveLog(String gameId) {
    List<Move> movesForGame;
        movesForGame = moveDAO.loadMoveLog().stream()
                .filter(move -> move.getGameId().equals(gameId))
                .collect(Collectors.toList());

    for (Move move : movesForGame) {
        executeMove(move);
    }
}// executes a move
   private  void executeMove(Move move) {
    int row = move.getx();
    int col = move.gety();

    switch (move.getMoveType()) {
        case REVEAL:
            board.revealCell(row, col, move.getGameId());
            break;
        case FLAG_TOGGLE:
            board.toggleCellFlag(row, col, move.getGameId());
            break;
    }
   }
//checks if game over coditions are met
    public boolean getGameOver() {
        return board.getGameover();
    }

    public void displayBoard(BoardGUI gui) {
    gui.updateBoard();
}
    public void displayBoardWithMines(BoardGUI gui) {
    for (int i = 0; i < board.getHeight(); i++) {
        for (int j = 0; j < board.getWidth(); j++) {
            Cell cell = board.getCell(i, j);
            if (cell.isMine()) {
                cell.reveal();
            }
        }
    }
    gui.updateBoard();
}

//    public  void endGame() {
//        
//        
//    if (checkWin()) {
//        // increate number of wins in player profile
//        profile.incrementWins();
//        System.out.println("Congratulations! You won!");
//        //stops the timer and saves thetime taken to comeplete teh game
//      timer.stop();
//       //System.out.println(timer.getDuration());
//      leaderboardDAO.saveToLeaderboard(SaveToLeaderboard());
//       
//      
//    } if(getGameOver()){
//        
//        profile.incrementLosses();
//        System.out.println("Sorry, you lost.");
//        timer.stop();
//       // System.out.println(timer.getDuration());
//    }
//    playerProfileDAO.savePlayerProfile(profile);
//    System.out.println("Thank you for playing!");
//    System.exit(0);
//    //board.displayBoardWithMines();
//        
//}
    
 public String endGame() {
    String message = "";

    if (checkWin()) {
        // Increment the number of wins in the player profile
        profile.incrementWins();
        message = "Congratulations! You won!";
        
        // Stop the timer and save the time taken to complete the game
        timer.stop();
        
        // Save to leaderboard
        leaderboardDAO.saveToLeaderboard(SaveToLeaderboard());
    } else if (getGameOver()) {
        board.RevealMines();
        profile.incrementLosses();
        message = "Sorry, you lost.";
        timer.stop();
    }

    // Save the player profile
    playerProfileDAO.savePlayerProfile(profile);
    
    return message;
}

    public BoardGUI newGame(MainMenuGUI mainMenu) {
    difficulty = mainMenu.selectGameModeAndSize();

    if (difficulty != null) {
        // Start the timer
        timer = new Timer();
        timer.start();
        GameinProcess = true;
        
        // Create and display the BoardGUI
        BoardGUI boardGUI = new BoardGUI(this);  // Pass the GameManager instance to the BoardGUI
        boardGUI.setVisible(true);

        // Now that the board is fully initialized, create the GameState object
        currentGameState = new GameState(profile.getPlayerName(), board);
        gameStateDAO.saveGameState(currentGameState);
        
        return boardGUI;  // Return the created BoardGUI object
    } else {
        return null;  // Return null or a default value if difficulty is null
    }
}



     
    public void RevealCell(int x, int y){
    if (checkWin() || getGameOver()) {
        return; // Ignore if game is already over
    }
    
    board.revealCell(x, y, currentGameState.getGameId());  // Pass gameId as an argument
    Move revealMove = new Move(profile.getPlayerName(), currentGameState.getGameId(), x-1, y-1, Move.MoveType.REVEAL);
    moveLog.add(revealMove);

    // Check end game conditions
    if (checkWin() || getGameOver()) {
        endTheGame();
    }
        }

   public void FlagCell(int x, int y){
    if (checkWin() || getGameOver()) {
        return; // Ignore if game is already over
    }

    board.toggleCellFlag(x, y, currentGameState.getGameId());
    Move flagMove = new Move(profile.getPlayerName(), currentGameState.getGameId(), x-1, y-1, Move.MoveType.FLAG_TOGGLE);
    moveLog.add(flagMove);

    // Check end game conditions
    if (checkWin() || getGameOver()) {
        endTheGame();
    }
}
       
       public void Save(){
           
       moveDAO.saveMoveLog(moveLog);
       
       }
       public static void  BoardManager(int width, int height, int totalMines){
       board = new Board(width, height, totalMines);
       }
       
       
   public void NewPlayer(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
        // Show an error message in the GUI
        JOptionPane.showMessageDialog(null, "Invalid player name. Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
        return;  // Exit the method if the player name is invalid.
    }

    // Attempt to retrieve the existing profile for the given player name.
    PlayerProfile existingProfile = playerProfileDAO.loadPlayerProfile(playerName);

    if (existingProfile == null) {
        profile = new PlayerProfile(playerName);
        playerProfileDAO.savePlayerProfile(profile);
        JOptionPane.showMessageDialog(null, "New player profile created for: " + playerName, "Info", JOptionPane.INFORMATION_MESSAGE);
    } else {
        // If the profile does exist, load it.
        profile = existingProfile;
        JOptionPane.showMessageDialog(null, "Existing player profile loaded for: " + playerName, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
    
    public Leaderboard SaveToLeaderboard(){
        
        System.out.println("profile is: " + profile);
        System.out.println("chosenDifficulty is: " + difficulty);
        System.out.println("timer is: " + timer);
    Leaderboard entry = new Leaderboard(profile.getPlayerName(), difficulty.toString(), timer.getDuration());
    return entry;
    }
    
    public String showLeaderboardForDifficulty(DifficultySettings difficulty) {
        List<Leaderboard> entries = LeaderBoardDAO.loadLeaderboard();

        // Filter by difficulty and sort by time
        List<Leaderboard> filteredEntries = entries.stream()
                .filter(entry -> entry.getDifficulty().equalsIgnoreCase(difficulty.toString()))
                .sorted(Comparator.comparingLong(Leaderboard::getTime))
                .collect(Collectors.toList());

        StringBuilder leaderboardMessage = new StringBuilder("Leaderboard for " + difficulty + ":\n");

        if (filteredEntries.isEmpty()) {
            leaderboardMessage.append("No records available for this difficulty.");
        } else {
            for (int i = 0; i < filteredEntries.size(); i++) {
                Leaderboard entry = filteredEntries.get(i);
                leaderboardMessage.append((i + 1) + ". " + entry.getPlayerName() + ": " + entry.getTime() + " seconds\n");
            }
        }

        
         return leaderboardMessage.toString();
    }
     public Board getBoard() {
        return board;
    }
     public boolean getGameinProcess(){
     return GameinProcess;
     }
     
     // Method to set the event listener
public void setGameEventListener(GameEventListener listener) {
    this.gameEventListener = listener;
}
     private void endTheGame() {
          System.out.println("End game method triggered!");
    String gameOutcomeMessage = endGame();
    if (gameEventListener != null) {
        gameEventListener.onGameEnd(gameOutcomeMessage);
    }
}


}
