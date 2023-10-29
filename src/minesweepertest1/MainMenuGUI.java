package minesweepertest1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
public class MainMenuGUI extends JFrame {
private GameManager gameManager;
private GameController controller;
//Main Menu Buttons
JButton newGameBtn = new JButton("New Game");
JButton loadGameBtn = new JButton("Load Game");
JButton leaderboardBtn = new JButton("Leaderboard");
JButton exitBtn = new JButton("Exit");
    


    public MainMenuGUI(GameManager gameManager, GameController controller) {
        this.gameManager = gameManager;
        this.controller = controller;
        //ask for player name
        String playerName = JOptionPane.showInputDialog(null, "Enter your name:", "Player Name", JOptionPane.QUESTION_MESSAGE);
    if (playerName == null || playerName.trim().isEmpty()) {
    System.exit(0);
        }
    //
    gameManager.NewPlayer(playerName);

    
    //
        setTitle("Minesweeper Main Menu - Welcome, " + playerName);
        setSize(300, 250);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window on the screen

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10)); 

        // New Game button
        
        newGameBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.startNewGame();
    }
    });
    panel.add(newGameBtn);
        // Load Game button
        
        loadGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPlayersGame();
            }
        });
        panel.add(loadGameBtn);

        // Leaderboard button
        
        leaderboardBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayLeaderboard();
            }
        });
        panel.add(leaderboardBtn);

        // Exit button
        
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(exitBtn);

        add(panel);
    }

    
    public DifficultySettings selectGameModeAndSize() {
    DifficultySettings chosenDifficulty = null;

    Object[] difficultyOptions = {"Easy", "Medium", "Hard", "Quit"};

    while (chosenDifficulty == null) {
        int choice = JOptionPane.showOptionDialog(
            this, 
            "Select game difficulty:", 
            "Difficulty Selection", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE,
            null,
            difficultyOptions,
            difficultyOptions[0]
        );

        switch (choice) {
            case 0:
                chosenDifficulty = DifficultySettings.EASY;
                break;
            case 1:
                chosenDifficulty = DifficultySettings.MEDIUM;
                break;
            case 2:
                chosenDifficulty = DifficultySettings.HARD;
                break;
            case 3:
            default:
                System.exit(0);
        }
    }

    int[][] configurations = chosenDifficulty.getConfigurations();
    Object[] sizeOptions = new String[configurations.length];
    for (int i = 0; i < configurations.length; i++) {
        sizeOptions[i] = configurations[i][0] + "x" + configurations[i][1] + " with " + configurations[i][2] + " mines";
    }

    int boardChoice = JOptionPane.showOptionDialog(
        this, 
        "Available sizes for " + chosenDifficulty + " mode:", 
        "Board Size Selection", 
        JOptionPane.DEFAULT_OPTION, 
        JOptionPane.QUESTION_MESSAGE,
        null,
        sizeOptions,
        sizeOptions[0]
    );

    if (boardChoice == -1) {
        // User closed the dialog without making a selection
        System.exit(0);
    }

    int[] selectedConfig = configurations[boardChoice];
    GameManager.BoardManager(selectedConfig[0], selectedConfig[1], selectedConfig[2]);

    return chosenDifficulty;
}

public void displayLeaderboard() {
    Object[] difficultyOptions = {"Easy", "Medium", "Hard", "Quit"};
    DifficultySettings chosenDifficulty = null;

    while (chosenDifficulty == null) {
        int choice = JOptionPane.showOptionDialog(
            this, 
            "View Leaderboard for which difficulty?", 
            "Leaderboard Selection", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE,
            null,
            difficultyOptions,
            difficultyOptions[0]
        );

        switch (choice) {
            case 0:
                chosenDifficulty = DifficultySettings.EASY;
                break;
            case 1:
                chosenDifficulty = DifficultySettings.MEDIUM;
                break;
            case 2:
                chosenDifficulty = DifficultySettings.HARD;
                break;
            case 3:
            default:
                return;  // exit the leaderboard
        }

        // Call the modified method from GameManager
        String leaderboard = gameManager.showLeaderboardForDifficulty(chosenDifficulty);
        
        // Display the string 'leaderboard' in your GUI
        JOptionPane.showMessageDialog(this, leaderboard, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }
}

 public void loadPlayersGame() {
    List<GameState> savedGamesList = gameManager.getSavedGames();

    if (savedGamesList.isEmpty()) {
        int option = JOptionPane.showConfirmDialog(null, 
                "No saved games found. Would you like to start a new game?",
                "No Saved Games",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            controller.startNewGame();
        } else {
            return; 
        }
    } else {
        GameState chosenGame = (GameState) JOptionPane.showInputDialog(
            this,
            "Select a game to load:",
            "Load Game",
            JOptionPane.QUESTION_MESSAGE,
            null,
            savedGamesList.toArray(),
            savedGamesList.get(0)
        );

        if (chosenGame != null) {
            controller.startLoadGame(chosenGame.getGameId());
        }
    }
}

 
}