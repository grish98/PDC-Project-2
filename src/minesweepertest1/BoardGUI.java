/**
 * Represents the graphical user interface (GUI) of the Minesweeper game board.
 * The class manages the display of the board, interactions with cells, and other GUI components.
 */
package minesweepertest1;

/**
 *
 * @author Grisham Balloo 20099072
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



    public class BoardGUI extends JFrame {
    // Array of buttons representing cells on the game board.
    private JButton[][] boardButtons;
    // Reference to the game manager to control game logic.
    private GameManager gameManager;
    // Reference to the game board to access cell data.
    private Board board;
    // Button for saving the game.
    private JButton saveButton;
    // Label to display the timer.
    private JLabel timerLabel;

    /**
     * Constructor to initialize the game board GUI.
     * Sets up the layout, buttons, and interactions.
     * 
     */
    public BoardGUI(GameManager gameManager) {
        
        this.gameManager = gameManager;
        this.board = gameManager.getBoard(); // Get the board from the game manager
        int rows = board.getHeight();
        int columns = board.getWidth();

setTitle("Minesweeper Game Board");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set the layout manager first before adding any components
        setLayout(new BorderLayout());

        // Timer setup
        timerLabel = new JLabel("Time: 0 seconds");
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        javax.swing.Timer guiTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long time = gameManager.getTimerDuration();
                timerLabel.setText("Time: " + time + " seconds");
            }
        });
        guiTimer.start();

        // setup the board
        boardButtons = new JButton[rows][columns];
        JPanel boardPanel = new JPanel(new GridLayout(rows, columns));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton button = new JButton();
                button.addMouseListener(new CellMouseListener(j, i, gameManager, this));
                boardPanel.add(button);
                boardButtons[i][j] = button;
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Initialize the save button
        saveButton = new JButton("Save Game");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (gameManager.getGameOver()) { // Assuming there's a method like this to check game status
            JOptionPane.showMessageDialog(null, "Finished game cannot be saved.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            gameManager.Save();
            JOptionPane.showMessageDialog(null, "Game saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
            }
        });
        
        JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.add(saveButton);
        add(saveButtonPanel, BorderLayout.SOUTH);

        updateBoard();
            }
        
        
        
     /**
     * Updates the visual representation of the game board.
     * refreshes each cell based on its state 
     */
    
       public void updateBoard() {
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Cell cell = board.getCell(j, i);
                JButton button = boardButtons[i][j];
                
                if (cell.isRevealed()) {
                    
                    if (cell.isMine()) {
                        button.setText("*");
                        button.setBackground(Color.RED);
                    } else {
                        int neighboringMines = cell.getNeighboringMines();
                        if (neighboringMines >= 0) {
                            button.setText(String.valueOf(neighboringMines));
                        } else {
                            button.setText("");
                        }
                        button.setBackground(Color.LIGHT_GRAY);
                    }
                } else if (cell.isFlagged()) {
                    button.setText("F");
                    button.setBackground(Color.YELLOW);
                } else {
                    button.setText("");
                    button.setBackground(Color.GRAY);
                }
            }
        }
    }
       /**
     * displays the outcome of the game to the user.
     *  message indicating the game's outcome (win/lose).
     */
    public void showGameOutcome(String message) {
        System.out.println("showGameOutcome called with message: " + message);
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, message));
        
    }
    /**
     * Disables interactions with all cells on the board.
     * Used after the game has ended to prevent further interactions.
     */
    public void disableUserInteractions() {
    for (int i = 0; i < boardButtons.length; i++) {
        for (int j = 0; j < boardButtons[i].length; j++) {
            boardButtons[i][j].setEnabled(false);  // Disable each button
        }
    }
}
}
    
    
