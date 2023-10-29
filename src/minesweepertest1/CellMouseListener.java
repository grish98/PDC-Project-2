/**
 * Represents a mouse listener for individual cells in the Minesweeper game board.
 * This class captures left and right mouse clicks on cells, triggering reveal and flag actions respectively.
 */
package minesweepertest1;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author Grisham Balloo 20099072
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
    
    /**
     * Handles mouse click events on the cell.
     * Left click reveals and right click  toggle flag 
     */
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
