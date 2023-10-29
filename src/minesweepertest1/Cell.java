/**
 * Represents the game board containing a grid of cells.
 * Handles operations such as initializing the board, placing mines, 
 * revealing cells, and calculating adjacent mine counts.
 */
package minesweepertest1;

import java.util.StringTokenizer;

/**
 *
 * @author Grisham Balloo 20099072
 */
public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int neighboringMines;
    private int x, y;

    public Cell() {
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.neighboringMines = 0;
        this.x =0;
        this.y = 0;
    }

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        
        
        
       this. isMine = mine;
        
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void reveal() {
        this.isRevealed = true;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void toggleFlag() {
        this.isFlagged = !this.isFlagged;
    }

    public int getNeighboringMines() {
        return neighboringMines;
    }

    public void setNeighboringMines(int neighboringMines) {
        this.neighboringMines = neighboringMines;
    }
    
    // Setter for revealed attribute
    public void setRevealed(boolean revealed) {
        
        this.isRevealed = revealed;
        
    }

    // Setter for flagged attribute
    public void setFlagged(boolean flagged) {
        this.isFlagged = flagged;
    }

   @Override
public String toString() {
    return (isMine ? "1" : "0") + "," +
           (isRevealed ? "1" : "0") + "," +
           (isFlagged ? "1" : "0");
}


    public String display() {
        String str = null;
        if (isFlagged) {
            str = "F";
        } else if (!isRevealed) {
             str = "-";
        } else if (isMine) {
             str = "M";
        } else if (neighboringMines >= 0) {
            str = String.valueOf(neighboringMines);
        } else {
             str =  " ";
        }
        return str;
    }
    public static Cell fromString(String data) {
    String[] tokens = data.split(",");
    if (tokens.length != 3) {
        throw new IllegalArgumentException("Invalid cell data format.");
    }

    Cell cell = new Cell();
    cell.setMine("1".equals(tokens[0]));
    cell.setRevealed("1".equals(tokens[1]));
    cell.setFlagged("1".equals(tokens[2]));

    return cell;
}

    
}
