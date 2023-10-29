/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author grish
 */
public class CellMouseListener extends MouseAdapter {
         private final int x;
    private final int y;
    private final GameManager gameManager;
    private final BoardGUI boardgui;

    public CellMouseListener(int x, int y, GameManager gameManager, BoardGUI boardgui) {
        this.x = x;
        this.y = y;
        this.gameManager = gameManager;
        this.boardgui = boardgui;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            gameManager.RevealCell(x, y);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            gameManager.FlagCell(x, y);
        }
        boardgui.updateBoard();
    }
}
