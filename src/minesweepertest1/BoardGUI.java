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
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



    public class BoardGUI extends JFrame {
    private JButton[][] boardButtons;
    private GameManager gameManager;
    private Board board;
   private JButton saveButton;
    
    public BoardGUI(GameManager gameManager) {
        
        this.gameManager = gameManager;
        this.board = gameManager.getBoard(); // Get the board from the game manager
        int rows = board.getHeight();
        int columns = board.getWidth();

        setTitle("Minesweeper Game Board");
        setSize(1000, 1000);  
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only this window
        setLocationRelativeTo(null);  // Center the window on the screen
        
        // Initialize the save button and its action listener
        saveButton = new JButton("Save Game");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.Save();  // Assuming the GameManager class has a Save() method
            }
        });
        
        boardButtons = new JButton[rows][columns];
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(rows, columns));  // Create a panel for the board buttons

        // Add the buttons to the board panel
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton button = new JButton();
                button.addMouseListener(new CellMouseListener(j, i, gameManager, this));
                boardPanel.add(button);
                boardButtons[i][j] = button;
            }
        }

        add(boardPanel, BorderLayout.CENTER);  // Add the board panel to the center of the main frame
        
        JPanel saveButtonPanel = new JPanel();  // Create a panel for the save button
        saveButtonPanel.add(saveButton);
        add(saveButtonPanel, BorderLayout.SOUTH);  // Add the save button panel to the bottom of the main frame
         updateBoard();  // Call updateBoard() to set the initial appearance of buttons
            }
        
        
        
    
    
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
                    
                    button.setBackground(Color.GRAY);
                }
            }
        }
    }
       
    public void showGameOutcome(String message) {
        System.out.println("showGameOutcome called with message: " + message);
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, message));
        
    }
    
    public void disableUserInteractions() {
    for (int i = 0; i < boardButtons.length; i++) {
        for (int j = 0; j < boardButtons[i].length; j++) {
            boardButtons[i][j].setEnabled(false);  // Disable each button
        }
    }
}
}
    
    
