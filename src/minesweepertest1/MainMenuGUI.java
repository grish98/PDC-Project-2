import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import minesweepertest1.GameManager;

public class MainMenuGUI extends JFrame {
private GameManager gameManager;
   
//Main Menu Buttons
JButton newGameBtn = new JButton("New Game");
JButton loadGameBtn = new JButton("Load Game");
JButton leaderboardBtn = new JButton("Leaderboard");
JButton exitBtn = new JButton("Exit");



    public MainMenuGUI(GameManager gameManager) {
        this.gameManager = gameManager;

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
                // Handle new game logic here
                selectGameModeAndSize();
            }
        });
        panel.add(newGameBtn);

        // Load Game button
        
        loadGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.loadPlayersGame();
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

    
    public void selectGameModeAndSize() {
       
    }

    public void displayLeaderboard() {
     
    }

  public static void main(String[] args) {
    GameManager gameManager = new GameManager();
    MainMenuGUI mainMenu = new MainMenuGUI(gameManager);
    mainMenu.setVisible(true);
}
}