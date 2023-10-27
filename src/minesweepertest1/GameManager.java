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
import java.util.Observable;
import java.util.Scanner;
import java.util.stream.Collectors;



public class GameManager  {

    private static Board board; 
    private static Timer timer;
    private static GameState currentGameState;
    private static PlayerProfile profile;
   
    
    static DifficultySettings chosenDifficulty ;
    private static final Scanner scanner = new Scanner(System.in);
    private static List<Move> moveLog = new ArrayList<>();
    
    private static final DataBaseManager dbManager = DataBaseManager.getInstance();
    private static final PlayerProfileDAO playerProfileDAO = new PlayerProfileDAO(dbManager);
    private final GameStateDAO gameStateDAO = new GameStateDAO(dbManager);
    private final MoveDAO moveDAO = new MoveDAO(dbManager);
    private final LeaderBoardDAO leaderboardDAO = new LeaderBoardDAO(dbManager);
    
public  void setupGame() {
  
    boolean bool = true;
    while (bool) { 


// This loop ensures the main menu is always shown after an certain actions
        int choice = UserInterface.MainMenu();
        while(bool)
        switch (choice) {
            case 1:
                
                newGame();
                bool = false;
                break;
            case 2:
                // Load a game
                loadPlayersGame();
               bool = false;
                break;
            case 3:
                // Display the leaderboard
                UserInterface.displayLeaderboard();
                break;
            default:
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
                break;
        }
    
  
    }
    
   
}
    public  void displayBoard() 
    {
        board.displayBoard();
    }

    //check win conditions
  public  boolean checkWin() 
  {
      return (board.allNonMineCellsRevealed() || board.allMinesFlagged());    
  }
  
  // loads and display all saved games from a player, if none then player can choose to srtat a new game
   public void loadPlayersGame() {
     Map<String, GameState> savedGamesMap = gameStateDAO.loadGameState(profile.getPlayerName());
    List<GameState> savedGames = new ArrayList<>(savedGamesMap.values());
    
    if (savedGames.isEmpty()) {
        while(true) {
            System.out.println("No saved games found for player: " + profile.getPlayerName());
            System.out.println("Would you like to start a new game? (Y/N)");
            String response = scanner.nextLine().trim();
            if ("Y".equalsIgnoreCase(response)) {
                newGame();
                return;  // Return after starting a new game.
            } else if ("N".equalsIgnoreCase(response)) {
                return;  // Return to the main menu or caller method.
            } else {
                System.out.println("Invalid choice. Please select Y or N.");
            }
        }
    } else {
        while(true) {
            System.out.println("Select a game to load:");
            for (int i = 0; i < savedGames.size(); i++) {
                System.out.println((i + 1) + ". Game ID: " + savedGames.get(i).getGameId());
            }
            
            try {
                int gameChoice = scanner.nextInt() - 1;  // Offset by 1 for 0-based indexing
                scanner.nextLine();  // Clear the newline
                
                if (gameChoice >= 0 && gameChoice < savedGames.size()) {
                    currentGameState = savedGames.get(gameChoice);
                    board = currentGameState.getBoardState();
                    loadAndExecuteMoveLog(currentGameState.getGameId());
                    
                    if (board == null) {
                        System.out.println("Error: Loaded board is null.");
                        return;  // Return to the main menu or caller method.
                    }
                    break;  // Exit the loop after successfully loading the game.
                } else {
                    System.out.println("Invalid choice. Please select a valid game to load.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();  // Clear the invalid input
            }
        }
    }
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

    public void displayBoardWithMines() {
        board.displayBoardWithMines();
    }

    public  void endGame() {
        
        
    if (checkWin()) {
        // increate number of wins in player profile
        profile.incrementWins();
        System.out.println("Congratulations! You won!");
        //stops the timer and saves thetime taken to comeplete teh game
      timer.stop();
       //System.out.println(timer.getDuration());
      leaderboardDAO.saveToLeaderboard(SaveToLeaderboard());
       
      
    } if(getGameOver()){
        
        profile.incrementLosses();
        System.out.println("Sorry, you lost.");
        timer.stop();
       // System.out.println(timer.getDuration());
    }
    playerProfileDAO.savePlayerProfile(profile);
    System.out.println("Thank you for playing!");
    System.exit(0);
    board.displayBoardWithMines();
        
}
    //initilizes a new game
    public void newGame()
   {
    UserInterface.selectGameModeAndSize();
   currentGameState = new GameState(profile.getPlayerName(), board);
   gameStateDAO.saveGameState(currentGameState);
   //start the timer
   timer = new Timer();
   timer.start();
   
   
   }
    
           
       public void RevealCell(int x, int y){
       
          board.revealCell(x-1, y-1, currentGameState.getGameId());  // Pass gameId as an argument
            
            Move revealMove = new Move(profile.getPlayerName(), currentGameState.getGameId(), x-1, y-1, Move.MoveType.REVEAL);
            moveLog.add(revealMove);
            
       }
       
       public void FlagCell(int x, int y){
       
       board.toggleCellFlag(x-1, y-1,currentGameState.getGameId());
            
            Move flagMove = new Move(profile.getPlayerName(), currentGameState.getGameId(), x-1, y-1, Move.MoveType.FLAG_TOGGLE);
            moveLog.add(flagMove);
       }
       
       public void Save(){
           
       moveDAO.saveMoveLog(moveLog);
       
       }
       public static void  BoardManager(int width, int height, int totalMines){
       board = new Board(width, height, totalMines);
       }
       
    public static void NewPlayer() {
    
    String playerName = scanner.nextLine().trim();

    if (playerName.isEmpty()) {
        System.out.println("Invalid player name. Please enter a valid name.");
        return;  // Exit the method if the player name is invalid.
    }

    // Attempt to retrieve the existing profile for the given player name.
    PlayerProfile existingProfile = playerProfileDAO.loadPlayerProfile(playerName);

    if (existingProfile == null) {
       
        
        profile = new PlayerProfile(playerName);
        playerProfileDAO.savePlayerProfile(profile);
        System.out.println("New player profile created for: " + playerName);
    } else {
        // If the profile does exist, load it.
        profile = existingProfile;
        System.out.println("Existing player profile loaded for: " + playerName);
    }
}
    
    public Leaderboard SaveToLeaderboard(){
    Leaderboard entry = new Leaderboard(profile.getPlayerName(), chosenDifficulty.toString(), timer.getDuration());
    return entry;
    }
    
    public  static void showLeaderboardForDifficulty(DifficultySettings difficulty) {
    List<Leaderboard> entries = LeaderBoardDAO.loadLeaderboard();

    // Filter by difficulty and sort by time
    List<Leaderboard> filteredEntries = entries.stream()
    .filter(entry -> entry.getDifficulty().equalsIgnoreCase(difficulty.toString()))
    .sorted(Comparator.comparingLong(Leaderboard::getTime))
    .collect(Collectors.toList());

    System.out.println("Leaderboard for " + difficulty + ":");
    if (filteredEntries.isEmpty()) {
        System.out.println("No records available for this difficulty.");
    } else {
        for (int i = 0; i < filteredEntries.size(); i++) {
            Leaderboard entry = filteredEntries.get(i);
            System.out.println((i + 1) + ". " + entry.getPlayerName() + ": " + entry.getTime() + " seconds");
        }
    }
}
}
