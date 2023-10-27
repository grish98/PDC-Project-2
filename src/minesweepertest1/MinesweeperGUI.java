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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperGUI extends JFrame {
    private GameControllerGUI controller;
    private JButton[][] boardButtons;

 

    public void updateButton(int x, int y, String text) {
        boardButtons[x][y].setText(text);
    }

    private class CellButtonListener extends MouseAdapter {
        private int x, y;

        public CellButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

      
    
    
    
}
