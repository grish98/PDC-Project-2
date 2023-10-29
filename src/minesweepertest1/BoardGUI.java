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
   
    
    public BoardGUI(GameManager gameManager) {
        
        this.gameManager = gameManager;
        this.board = gameManager.getBoard(); // Get the board from the game manager
        int rows = board.getHeight();
        int columns = board.getWidth();

        setTitle("Minesweeper Game Board");
        setSize(1000, 1000);  
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only this window
        setLocationRelativeTo(null);  // Center the window on the screen

        boardButtons = new JButton[rows][columns];
        setLayout(new GridLayout(rows, columns));

        // Create buttons and add them to the GUI
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton button = new JButton();
                button.addMouseListener(new CellMouseListener(j, i, gameManager, this));
                add(button);
                boardButtons[i][j] = button;
            }
        }
        
        
    }
    
       public void updateBoard() {
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Cell cell = board.getCell(j, i);
                JButton button = boardButtons[i][j];
                
                if (cell.isRevealed()) {
                    if (cell.isMine()) {
                        button.setText("*");  // Display mine symbol
                        button.setBackground(Color.RED);
                    } else {
                        int neighboringMines = cell.getNeighboringMines();
                        if (neighboringMines > 0) {
                            button.setText(String.valueOf(neighboringMines));
                        } else {
                            button.setText("");
                        }
                        button.setBackground(Color.LIGHT_GRAY);
                    }
                } else if (cell.isFlagged()) {
                    button.setText("F");  // Display flag symbol
                    button.setBackground(Color.YELLOW);
                } else {
                    button.setText("");
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
    
    
